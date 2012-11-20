package com.nsoll.smt;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerSetActivity extends Activity {
	
	Request task;
	ListView list;
	SetAdapter adapter = new SetAdapter(this);

	private OnItemClickListener item_listener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			SetItem select_item = (SetItem) adapter.getItem(position);
			Toast.makeText(ManagerSetActivity.this, select_item.getCheckname(), 1000).show();
		}
	};
	
    public void onCreate(Bundle savedInstanceState) {
    	   	
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_managerset);
        
        Intent receivedIntent = getIntent();
        Integer no = receivedIntent.getIntExtra("no", 0);
        String name = receivedIntent.getStringExtra("name");
        
        String url = "http://smt.nsoll.com/m/managerset.smt?no=" + Integer.toString(no);
        
        list = (ListView)findViewById(R.id.managersetList);
        task = new Request();
        task.execute(url);

        // view subject
        String subject = name + " setting";
        TextView subjectView = (TextView) findViewById(R.id.managersetSubject);
        subjectView.setText(subject);
        
        // save button
        Button saveBtn = (Button) findViewById(R.id.managersetSave);        
        saveBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
		        final Integer itemCount = adapter.getCount();
		        SetItem select_item = (SetItem) adapter.getItem(2);
		        Boolean m_checked = select_item.getCheck();		        /*
		        for(Integer i=0; i<itemCount; i++ ){
		        	select_item = (SetItem) adapter.getItem(i);
			        String m_checkname;
			        Boolean m_checked;
		        	m_checkname = select_item.getCheckname();
		        	m_checked = select_item.getCheck();
		        	
		        }
		        */
				Toast.makeText(getApplicationContext(), Boolean.toString(m_checked), 1000).show();
			}
        	
        });
        
        
    }
       
    class Request extends AsyncTask<String, Void, String> {
    	StringBuilder output = new StringBuilder();
    	@Override
    	protected String doInBackground(String... urlStr) {    	
        	try {
        		HttpClient client = new DefaultHttpClient();
        		HttpGet get = new HttpGet(urlStr[0]);
        		HttpResponse response = client.execute(get);
        		HttpEntity resEntity = response.getEntity();
        		if(resEntity != null) {
        			output.append(EntityUtils.toString(resEntity));
        		}
        		
        	
        	} catch(Exception ex) {
        		Log.e("SmtHTTP", "Exception in processing response.", ex);
        		
        	}        	      	
            return output.toString();    			
    	}
    	
 		@Override
    	protected void onPostExecute(String result){
    		JSONObject jsonObject;
    		
    		try {
				jsonObject = new JSONObject(result);
				JSONObject json_checkList = new JSONObject(jsonObject.getString("check_list"));
		
				adapter.add(new SetItem("SMS", json_checkList.getBoolean("sms")));
				adapter.add(new SetItem("Push", json_checkList.getBoolean("push")));
				adapter.add(new SetItem("MyPeople", json_checkList.getBoolean("mypeople")));
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		list.setAdapter(adapter);
    	//	list.setTextFilterEnabled(true);
    		list.setOnItemClickListener(item_listener);
    		
    	}
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}

package com.nsoll.smt;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerListActivity extends Activity {

	Request task;
	ListView list;
	
	private OnItemClickListener item_listener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			TextView select_item = (TextView) view;
			Toast.makeText(ManagerListActivity.this, select_item.getText(), 1000).show();
		}
	};
	
    public void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managerlist);
        
        list = (ListView)findViewById(R.id.managerList);
        task = new Request();
        task.execute("http://smt.nsoll.com/m/managerlist.smt");
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
    		String[] manager_list = null;
    		
    		try {
				jsonObject = new JSONObject(result);
				JSONArray jArr = new JSONArray(jsonObject.getString("manager_list"));
				manager_list = new String[jArr.length()];
				for (int i=0; i < jArr.length(); i++) {
					manager_list[i] = jArr.getString(i);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManagerListActivity.this, R.layout.list_layout, manager_list);
       		list.setAdapter(adapter);
    		list.setTextFilterEnabled(true);
    		list.setOnItemClickListener(item_listener);
    	}
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}

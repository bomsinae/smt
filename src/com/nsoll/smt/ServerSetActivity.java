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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ServerSetActivity extends Activity {
	
	Request task;
	ListView list;
	SetAdapter adapter = new SetAdapter(this);

	private OnItemClickListener item_listener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			SetItem select_item = (SetItem) adapter.getItem(position);
			Toast.makeText(ServerSetActivity.this, select_item.getCheckname(), 1000).show();
		}
	};
	
    public void onCreate(Bundle savedInstanceState) {
    	   	
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_serverset);
        
        Intent receivedIntent = getIntent();
        String ip = receivedIntent.getStringExtra("ip");
        String url = "http://smt.nsoll.com/m/serverset.smt?ip=" + ip;
        
        list = (ListView)findViewById(R.id.serversetList);
        task = new Request();
        task.execute(url);

        String subject = ip + " setting";
        TextView subjectView = (TextView) findViewById(R.id.serversetSubject);
        subjectView.setText(subject);
        
        
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
		
				adapter.add(new SetItem("Ping", json_checkList.getBoolean("ping_check")));
				adapter.add(new SetItem("Port", json_checkList.getBoolean("port_check")));
				adapter.add(new SetItem("URL", json_checkList.getBoolean("url_check")));
				adapter.add(new SetItem("Disk Used", json_checkList.getBoolean("diskusage_check")));
				adapter.add(new SetItem("Disk Status", json_checkList.getBoolean("diskstatus_check")));
				adapter.add(new SetItem("Server Load", json_checkList.getBoolean("serverload_check")));
				adapter.add(new SetItem("Process Count", json_checkList.getBoolean("processcount_check")));
				adapter.add(new SetItem("Firewall", json_checkList.getBoolean("firewall_check")));
				adapter.add(new SetItem("Login Session", json_checkList.getBoolean("loginsession_check")));
				adapter.add(new SetItem("Listen Port", json_checkList.getBoolean("listenport_check")));
				adapter.add(new SetItem("Logfile", json_checkList.getBoolean("logfile_check")));
				adapter.add(new SetItem("Process Exceute", json_checkList.getBoolean("processexec_check")));
				adapter.add(new SetItem("tmp directory", json_checkList.getBoolean("tmp_check")));
				adapter.add(new SetItem("Traffic", json_checkList.getBoolean("traffic_check")));
				
				
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

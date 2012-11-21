package com.nsoll.smt;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
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

public class ServerSetActivity extends Activity {
	
	Request task;
	PostRequest postTask;
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
        final String ip = receivedIntent.getStringExtra("ip");
        String url = "http://smt.nsoll.com/m/serverset.smt?ip=" + ip;
        
        list = (ListView)findViewById(R.id.serversetList);
        task = new Request();
        task.execute(url);

        //
        String subject = ip + " Setting";
        TextView subjectView = (TextView) findViewById(R.id.serversetSubject);
        subjectView.setText(subject);
        
     // save button
        Button saveBtn = (Button) findViewById(R.id.serversetSave);        
        saveBtn.setOnClickListener(new OnClickListener() {
		
			public void onClick(View v) {
		        final Integer itemCount = adapter.getCount();		        
		        //Log.e("ADAPTER COUNT", Integer.toString(itemCount));
		        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		        
		        // action parameter
		        params.add(new BasicNameValuePair("url", "http://smt.nsoll.com/m/serverset.smt"));
		        params.add(new BasicNameValuePair("action", "onoff"));
		        params.add(new BasicNameValuePair("ip", ip));
		        for(Integer i=0; i<itemCount; i++ ){
		        	//Log.e("adapter seq", Integer.toString(i));
		        	SetItem select_item = (SetItem) adapter.getItem(i);
			        String m_keyname = select_item.getKeyname();
			        Boolean m_checked = select_item.getCheck();
			        params.add(new BasicNameValuePair(m_keyname, Boolean.toString(m_checked)));
			        //Log.e(Boolean.toString(m_checked), m_checkname);
		        }
		        
		        postTask = new PostRequest();
		        postTask.execute(params);
		        
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
    		try {
    			JSONObject jsonObject = new JSONObject(result);
				JSONArray jArr = new JSONArray(jsonObject.getString("check_list"));
				for (Integer i=0; i<jArr.length(); i++){
					JSONArray subjArr = new JSONArray(jArr.getString(i));
					String name = subjArr.getString(0);
					String keyname = subjArr.getString(1);
					Boolean checked = subjArr.getBoolean(2);
					adapter.add(new SetItem(name, keyname, checked));
				}
				/*
				adapter.add(new SetItem("Ping", "ping_check", json_checkList.getBoolean("ping_check")));
				adapter.add(new SetItem("Port", "port_check", json_checkList.getBoolean("port_check")));
				adapter.add(new SetItem("URL", "url_check", json_checkList.getBoolean("url_check")));
				adapter.add(new SetItem("Disk Used", "diskusage_check", json_checkList.getBoolean("diskusage_check")));
				adapter.add(new SetItem("Disk Status", "diskstatus_check", json_checkList.getBoolean("diskstatus_check")));
				adapter.add(new SetItem("Server Load", "serverload_check", json_checkList.getBoolean("serverload_check")));
				adapter.add(new SetItem("Process Count", "processcount_check", json_checkList.getBoolean("processcount_check")));
				adapter.add(new SetItem("Firewall", "firewall_check", json_checkList.getBoolean("firewall_check")));
				adapter.add(new SetItem("Login Session", "loginsession_check", json_checkList.getBoolean("loginsession_check")));
				adapter.add(new SetItem("Listen Port", "listenport_check", json_checkList.getBoolean("listenport_check")));
				adapter.add(new SetItem("Logfile", "logfile_check", json_checkList.getBoolean("logfile_check")));
				adapter.add(new SetItem("Process Exceute", "processexec_check", json_checkList.getBoolean("processexec_check")));
				adapter.add(new SetItem("tmp directory", "tmp_check", json_checkList.getBoolean("tmp_check")));
				adapter.add(new SetItem("Traffic", "traffic_check", json_checkList.getBoolean("traffic_check")));
				*/
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		
    		list.setAdapter(adapter);
    	//	list.setTextFilterEnabled(true);
    		list.setOnItemClickListener(item_listener);
    	}
    }
    
    class PostRequest extends AsyncTask<ArrayList<BasicNameValuePair>, Void, String> {
    	StringBuilder output = new StringBuilder();
		@Override
		protected String doInBackground(ArrayList<BasicNameValuePair>... params) {
			try {
		        HttpClient client = new DefaultHttpClient();
		        //get url and remove from params
		        String postUrl = params[0].remove(0).getValue();
		        HttpPost post = new HttpPost(postUrl);
		        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params[0], HTTP.UTF_8);
		        post.setEntity(ent);
		        HttpResponse responsePost = client.execute(post);
		        HttpEntity resEntity = responsePost.getEntity();
		        if(resEntity != null) {
		        	output.append(EntityUtils.toString(resEntity));
		        }
	        } catch(Exception ex) {
	        	Log.e("SmtHttp", "Exception in processing response.", ex);
	        }
			return output.toString();
		}
    	
		@Override
		protected void onPostExecute(String result) {
			JSONObject jsonObject = null;
			Boolean b_result = null;
			String response = null;
    		try {
				jsonObject = new JSONObject(result);
				b_result = jsonObject.getBoolean("result");
				
				if (b_result){
	    			String obj = jsonObject.getString("obj");
	    			response = "Set Complete : " + obj;
	    		} else {
	    			response = "Set Error.";
	    		}
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		Log.e("response", Boolean.toString(b_result));
    		
    		Toast.makeText(getBaseContext(), response, 1000).show();
			finish();
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}

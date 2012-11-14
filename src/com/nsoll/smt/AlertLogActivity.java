package com.nsoll.smt;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class AlertLogActivity extends Activity {
	Request task;
	TextView txtMsg;
	
    public void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertlog);
        
        txtMsg = (TextView)findViewById(R.id.alertLog);
        task = new Request();
        task.execute("http://smt.nsoll.com/m/alertlog.smt");
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
    		txtMsg.setText(result);
    	}
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}

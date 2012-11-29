
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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ServerListActivity extends Activity {

	public static final int REQUEST_CODE_ANOTHER = 1101;
	Request task;
	ListView list;
	ListAdapter adapter = new ListAdapter(this);
	
	private OnItemClickListener item_listener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			ListItem select_item = (ListItem) adapter.getItem(position);
			Intent intent = new Intent(getBaseContext(), ServerSetActivity.class);
			intent.putExtra("ip", select_item.getName());
			startActivityForResult(intent, REQUEST_CODE_ANOTHER);
			
		}
	};

    public void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_serverlist);
        
        list = (ListView)findViewById(R.id.serverList);
        task = new Request();
        task.execute("http://smt.nsoll.com/m/serverlist.smt");
 
        
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
    		Integer no = 0;
    		String name = null;
    		String subname = null;
    		String regdate = null;
    		
    		try {
				jsonObject = new JSONObject(result);
				JSONArray jArr = new JSONArray(jsonObject.getString("server_list"));
				for (int i=0; i < jArr.length(); i++) {
					name = jArr.getJSONObject(i).getString("ip");
					regdate = jArr.getJSONObject(i).getString("regdate");
					adapter.add(new ListItem(no, name, subname, regdate));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		list.setAdapter(adapter);
    		//list.setTextFilterEnabled(true);
    		list.setOnItemClickListener(item_listener);
            
    	}
    	
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    */
    
}

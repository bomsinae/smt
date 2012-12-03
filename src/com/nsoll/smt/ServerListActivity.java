
package com.nsoll.smt;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
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
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ServerListActivity extends Activity {

	public static final int REQUEST_CODE_ANOTHER = 1101;
	Request task;
	ListView list;
	ListAdapter adapter;
	public CookieManager cookieManager;
		
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
    
        CookieSyncManager.createInstance(this);
    	cookieManager = CookieManager.getInstance();
        
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	CookieSyncManager.getInstance().startSync();
    	String domain = getString(R.string.domain);
    	// 쿠키가 없다면 액티비티를 죽이고 로그인액티비티를 띄우자.
    	if (CookieManager.getInstance().getCookie(domain) == null){
        	Intent intent = new Intent(getBaseContext(), MainActivity.class);
    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
    		finish();
        } else {
        	adapter = new ListAdapter(this);
	        list = (ListView)findViewById(R.id.serverList);
	        task = new Request();
	        task.execute(domain+"/m/serverlist.smt");
        }
    }

    class Request extends AsyncTask<String, Void, String> {
    	StringBuilder output = new StringBuilder();
    	
    	@Override
    	protected String doInBackground(String... urlStr) {
    		// cookie
    		DefaultHttpClient client = new DefaultHttpClient();
    		CookieSyncManager.createInstance(getApplicationContext());
    		cookieManager = CookieManager.getInstance();
    		CookieStore cookieStore = new BasicCookieStore();
    		HttpContext localContext = new BasicHttpContext();
    		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    		String domain = getString(R.string.domain);
    		String[] keyValueSets = CookieManager.getInstance().getCookie(domain).split(";");
    		for(String cookie:keyValueSets)
    		{
    			String[] keyValue = cookie.split("=");
    			String key = keyValue[0];
    			String value = "";
    			if(keyValue.length>1) value = keyValue[1];
    			client.getCookieStore().addCookie(new BasicClientCookie(key, value));
    			Log.v("cookie", "key:"+key+";value:"+value);
    		}
        	try {
        		HttpGet get = new HttpGet(urlStr[0]);
        		
        		CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
        		List<Cookie> cookies = client.getCookieStore().getCookies();
        		List<?> cookieHeader = cookieSpecBase.formatCookies(cookies);
        		get.setHeader((Header) cookieHeader.get(0));
        				
        		HttpResponse response = client.execute(get, localContext);
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
    		Boolean b_result = null;
    		
    		try {
				jsonObject = new JSONObject(result);
				b_result = jsonObject.getBoolean("result");
				if (b_result) {
					JSONArray jArr = new JSONArray(jsonObject.getString("server_list"));
					for (int i=0; i < jArr.length(); i++) {
						name = jArr.getJSONObject(i).getString("ip");
						subname = jArr.getJSONObject(i).getString("name");
						regdate = jArr.getJSONObject(i).getString("regdate");
						adapter.add(new ListItem(no, name, subname, regdate));
					}
					
					list.setAdapter(adapter);
		    		list.setOnItemClickListener(item_listener);
				}
				else {
					Intent intent = new Intent(getBaseContext(), MainActivity.class);
		    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
		    		finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
            
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

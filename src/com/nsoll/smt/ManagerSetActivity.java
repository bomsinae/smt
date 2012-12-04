package com.nsoll.smt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerSetActivity extends Activity {
	
	public static final int REQUEST_CODE_ANOTHER = 1211;
	Request task;
	PostRequest postTask;
	ListView list;
	SetAdapter adapter;
	public CookieManager cookieManager;

	private OnItemClickListener item_listener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			SetItem select_item = (SetItem) adapter.getItem(position);
			//Toast.makeText(ManagerSetActivity.this, select_item.getCheckname(), 1000).show();
		}
	};
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_managerset);

        final String domain = getString(R.string.domain);
        
        CookieSyncManager.createInstance(this);
    	cookieManager = CookieManager.getInstance();
        
        
        
        // save button
        Button saveBtn = (Button) findViewById(R.id.managersetSave);        
        saveBtn.setOnClickListener(new OnClickListener() {
		
			public void onClick(View v) {
		        final Integer itemCount = adapter.getCount();		        
		        //Log.e("ADAPTER COUNT", Integer.toString(itemCount));
		        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		        
		        // action parameter
		        params.add(new BasicNameValuePair("url", domain+"/m/managerset.smt"));
		        params.add(new BasicNameValuePair("action", "onoff"));
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
        	adapter = new SetAdapter(this);
        	list = (ListView)findViewById(R.id.managersetList);
            task = new Request();
            task.execute(domain+"/m/managerset.smt");	
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
 			Boolean b_result = null;
    		try {
    			JSONObject jsonObject = new JSONObject(result);
    			b_result = jsonObject.getBoolean("result");
    			if (b_result) {
					JSONArray jArr = new JSONArray(jsonObject.getString("check_list"));
					for (Integer i=0; i<jArr.length(); i++){
						JSONArray subjArr = new JSONArray(jArr.getString(i));
						String name = subjArr.getString(0);
						String keyname = subjArr.getString(1);
						Boolean checked = subjArr.getBoolean(2);
						adapter.add(new SetItem(name, keyname, checked));
					}
					
			        // view subject
					String name = jsonObject.getString("userid");
			        String subject = name + " Setting";
			        TextView subjectView = (TextView) findViewById(R.id.managersetSubject);
			        subjectView.setText(subject);
			        
					list.setAdapter(adapter);
		    		list.setOnItemClickListener(item_listener);
				}
    			else {
    				// 서버로부터 false 받으면 로그인 다시 해라
    				Intent intent = new Intent(getBaseContext(), LoginActivity.class);
		    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
		    		finish();
    			}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		
    		
    	}
    }
        
    class PostRequest extends AsyncTask<ArrayList<BasicNameValuePair>, Void, String> {
    	StringBuilder output = new StringBuilder();
		@Override
		protected String doInBackground(ArrayList<BasicNameValuePair>... params) {
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
			    //get url and remove from params
		        String postUrl = params[0].remove(0).getValue();
		        HttpPost post = new HttpPost(postUrl);
		        
		        //
		        CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
        		List<Cookie> cookies = client.getCookieStore().getCookies();
        		List<?> cookieHeader = cookieSpecBase.formatCookies(cookies);
        		post.setHeader((Header) cookieHeader.get(0));
        		
		        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params[0], HTTP.UTF_8);
		        post.setEntity(ent);
		        HttpResponse responsePost = client.execute(post, localContext);
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
    		//Log.e("response", Boolean.toString(b_result));
    		
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

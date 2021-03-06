package com.nsoll.smt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

public class LoginActivity extends Activity {
	
	private EditText manageridEdit;
	private EditText passwordEdit;
	private Button loginBtn;
	private TextView loginResultView;
	private PostRequest postTask;
	
	public static HttpClient client = new DefaultHttpClient();
	public static Cookie cookie = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		manageridEdit = (EditText) findViewById(R.id.manageridEdit);
		passwordEdit = (EditText) findViewById(R.id.passwordEdit);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		loginResultView = (TextView) findViewById(R.id.loginResultView);

		// for push
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if("".equals(regId))
		      GCMRegistrar.register(this, "1003709731095");
		else
		      Log.d("regId ===>>", regId);
		
		
		// Button Action
		loginBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//토근 가져오기
				final String token = GCMRegistrar.getRegistrationId(getApplicationContext());
				ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				
				String domain = getString(R.string.domain);
				params.add(new BasicNameValuePair("url", domain+"/m/login.smt"));
				params.add(new BasicNameValuePair("action", "login"));
				params.add(new BasicNameValuePair("userid", manageridEdit.getText().toString()));
				params.add(new BasicNameValuePair("password", passwordEdit.getText().toString()));
				params.add(new BasicNameValuePair("token", token));
				params.add(new BasicNameValuePair("device", "android"));
				Log.v("token ===>>", token);
				
				postTask = new PostRequest();
				postTask.execute(params);
			}
		});
		
		
	}

	// 로그인화면에서 백버튼 누르면 종료할지 물어본다.
	public boolean onKeyDown(int keyCode, KeyEvent event){
		switch(keyCode){
	    	case KeyEvent.KEYCODE_BACK:
	    	String alertTitle = getResources().getString(R.string.app_name);
	    	String buttonMessage = getResources().getString(R.string.logout_msg);
	    	String buttonYes = getResources().getString(R.string.button_yes);
	    	String buttonNo = getResources().getString(R.string.button_no);
	         
	    	new AlertDialog.Builder(this)
	    	.setTitle(alertTitle)
	    	.setMessage(buttonMessage)
	    	.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
	       
	    		public void onClick(DialogInterface dialog, int which) {
	    		// TODO Auto-generated method stub
	    			moveTaskToBack(true);
	    			finish();
	    		}
	    	})
	    	.setNegativeButton(buttonNo, null)
	    	.show();
	    }
	    return true;
	}



	class PostRequest extends AsyncTask<ArrayList<BasicNameValuePair>, Void, String> {
    	StringBuilder output = new StringBuilder();
		@Override
		protected String doInBackground(ArrayList<BasicNameValuePair>... params) {
			try {
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
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		
    		String domain = getString(R.string.domain);
			List<Cookie> cookies =((DefaultHttpClient)client).getCookieStore().getCookies();
    		if (b_result && !cookies.isEmpty()){ // 로그인 성공
				CookieManager cookieManager = CookieManager.getInstance();
				CookieSyncManager.createInstance(getApplicationContext());
				cookieManager.setAcceptCookie(true);
				for (int i=0; i<cookies.size(); i++) {
					String cookieString = cookies.get(i).getName() + "="+ cookies.get(i).getValue();
					cookieManager.setCookie(domain, cookieString);
					cookieManager.setAcceptCookie(true);
					Log.e("cookie test : ", cookieString);
				}
				

				finish();
    		} else { // 로그인 실패시
    			passwordEdit.setText("");
    			loginResultView.setText("Login incorrect. Try again.");
    		}
    		Log.e("response", Boolean.toString(b_result));
    		    		
			
		}
    }
}

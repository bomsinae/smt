package com.nsoll.smt;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private EditText manageridEdit;
	private EditText passwordEdit;
	private Button loginBtn;
	private TextView loginResultView;
	private PostRequest postTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		manageridEdit = (EditText) findViewById(R.id.manageridEdit);
		passwordEdit = (EditText) findViewById(R.id.passwordEdit);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		loginResultView = (TextView) findViewById(R.id.loginResultView);
		
		
		// Button Action
		loginBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				
				params.add(new BasicNameValuePair("url", "http://smt.nsoll.com/m/login.smt"));
				params.add(new BasicNameValuePair("action", "login"));
				params.add(new BasicNameValuePair("userid", manageridEdit.getText().toString()));
				params.add(new BasicNameValuePair("password", passwordEdit.getText().toString()));
				
				postTask = new PostRequest();
				postTask.execute(params);
			}
		});
		
		
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
}

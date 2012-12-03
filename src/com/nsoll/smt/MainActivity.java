package com.nsoll.smt;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	public static final int REQUEST_CODE_ANOTHER = 1001;
	ListView list;
	MainMenuAdapter adapter = new MainMenuAdapter(this);
	public CookieManager cookieManager = CookieManager.getInstance();


	private OnItemClickListener item_listener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			MainItem select_item = (MainItem) adapter.getItem(position);
			if (select_item.getMenu() == "Server List"){
				Intent intent = new Intent(getBaseContext(), ServerListActivity.class);
	    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
			} 
			else if (select_item.getMenu() == "Manager List"){
				Intent intent = new Intent(getBaseContext(), ManagerListActivity.class);
	    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
			}
			else if (select_item.getMenu() == "Manager Setting"){
				Intent intent = new Intent(getBaseContext(), ManagerSetActivity.class);
	    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
			}
			else if (select_item.getMenu() == "Monitor Log"){
				Intent intent = new Intent(getBaseContext(), MonitorLogActivity.class);
	    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
			}
			else if (select_item.getMenu() == "Alert Log"){
				Intent intent = new Intent(getBaseContext(), AlertLogActivity.class);
	    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
			}

		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        list = (ListView) findViewById(R.id.mainList);
     
        CookieSyncManager.createInstance(this);
    	cookieManager = CookieManager.getInstance();

        adapter.add(new MainItem(0xff8EC7D0, "Server List"));
        //adapter.add(new MainItem(0xff467F88, "Manager List"));
        adapter.add(new MainItem(0xff467F88, "Manager Setting"));
        adapter.add(new MainItem(0xff8EC7D0, "Monitor Log"));
        adapter.add(new MainItem(0xff467F88, "Alert Log"));
        
        list.setAdapter(adapter);
        list.setTextFilterEnabled(true);
		list.setOnItemClickListener(item_listener);

    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	CookieSyncManager.getInstance().startSync();
    	String domain = getString(R.string.domain);
        if (CookieManager.getInstance().getCookie(domain) == null){
        	Intent intent = new Intent(getBaseContext(), LoginActivity.class);
    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
    	}

        
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menu_login:
    		Intent intent = new Intent(getBaseContext(), LoginActivity.class);
    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
    		break;
    	case R.id.menu_logout:
    		CookieSyncManager.createInstance(this);
    		cookieManager = CookieManager.getInstance();
    		cookieManager.removeAllCookie();
    		Log.v("cookie", "delete");
    		break;
    	}    		
    	return super.onOptionsItemSelected(item);
    }
    */
}

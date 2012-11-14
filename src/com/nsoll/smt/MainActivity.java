package com.nsoll.smt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	public static final int REQUEST_CODE_ANOTHER = 1001;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button serverList = (Button) findViewById(R.id.serverList);
        Button managerList = (Button) findViewById(R.id.managerList);
        Button monitorLog = (Button) findViewById(R.id.monitorLog);
        Button alertLog = (Button) findViewById(R.id.alertLog);
        
        //Server List
        serverList.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(getBaseContext(), ServerListActivity.class);
        		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
        		
        	}
        });
        
        // Manager List
        managerList.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(getBaseContext(), ManagerListActivity.class);
        		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
        	}
        });
        
        // Monitor Log
        monitorLog.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(getBaseContext(), MonitorLogActivity.class);
        		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
        	}
        });
        
        // Alert Log
        alertLog.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(getBaseContext(), AlertLogActivity.class);
        		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
        	}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

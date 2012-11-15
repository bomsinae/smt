package com.nsoll.smt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final int REQUEST_CODE_ANOTHER = 1001;
	ListView list;
	
	String[] menulist = {"Server List", "Manager List", "Monitor Log", "Alert Log"};
	
	private OnItemClickListener item_listener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			TextView select_item = (TextView) view;
			Toast.makeText(MainActivity.this, select_item.getText(), 1000).show();
			
			Intent intent = new Intent(getBaseContext(), ServerListActivity.class);
    		startActivityForResult(intent, REQUEST_CODE_ANOTHER);
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        list = (ListView) findViewById(R.id.mainList);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, menulist);
        list.setAdapter(adapter);
        list.setTextFilterEnabled(true);
		list.setOnItemClickListener(item_listener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

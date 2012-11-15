package com.nsoll.smt;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainItemView extends LinearLayout {

	private TextView m_head;
	private TextView m_menu;
	
	public MainItemView(Context context, MainItem item) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_main, this, true);
		
		m_head = (TextView) findViewById(R.id.mainHead);
		m_head.setBackgroundColor(item.getHeadColor());
		
		m_menu = (TextView) findViewById(R.id.mainMenu);
		m_menu.setText(item.getMenu());
	}
	
	
	public void setHeadColor(int color) {
		m_head.setBackgroundColor(color);
	}
	
	public void setMenu(String menu) {
		m_menu.setText(menu);
	}
}

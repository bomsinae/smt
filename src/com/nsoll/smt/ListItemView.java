package com.nsoll.smt;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListItemView extends LinearLayout {

	private TextView m_name;
	private TextView m_subname;
	private TextView m_regdate;
	
	public ListItemView(Context context, ListItem item) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_list, this, true);
		
		m_name = (TextView) findViewById(R.id.nameView);
		m_name.setText(item.getName());
		
		m_subname = (TextView) findViewById(R.id.subnameView);
		m_subname.setText(item.getSubname());
		
		m_regdate = (TextView) findViewById(R.id.regdateView);
		m_regdate.setText(item.getRegdate());
	}
	
	
	public void setName(String name) {
		m_name.setText(name);
	}
	
	public void setSubname(String subname) {
		m_subname.setText(subname);
	}
	
	public void setRegdate(String regdate) {
		m_regdate.setText(regdate);
	}
}

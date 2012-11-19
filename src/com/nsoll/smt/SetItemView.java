package com.nsoll.smt;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetItemView extends LinearLayout {

	private TextView m_checkname;
	private CheckBox m_checked;
	
	public SetItemView(Context context, SetItem item) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_set, this, true);
		
		m_checkname = (TextView) findViewById(R.id.checkName);
		m_checkname.setText(item.getCheckname());
		
		m_checked = (CheckBox) findViewById(R.id.check);
		m_checked.setChecked(item.getCheck());
	}
	
	public void setCheckname(String checkname) {
		m_checkname.setText(checkname);
	}
	
	public void setCheck(Boolean checked) {
		m_checked.setChecked(checked);
	}
}

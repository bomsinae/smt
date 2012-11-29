package com.nsoll.smt;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LogItemView extends LinearLayout {

	private TextView m_ip;
	private TextView m_logtime;
	private TextView m_msg;
	
	public LogItemView(Context context, LogItem item) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_log, this, true);
		
		m_ip = (TextView) findViewById(R.id.ipView);
		m_ip.setText(item.getIp());
		
		m_logtime = (TextView) findViewById(R.id.logtimeView);
		m_logtime.setText(item.getLogtime());
		
		m_msg = (TextView) findViewById(R.id.msgView);
		m_msg.setText(item.getMsg());
		}
	
	
	public void setIp(String ip) {
		m_ip.setText(ip);
	}
	
	public void setLogtime(String logtime) {
		m_logtime.setText(logtime);
	}
	
	public void setMsg(String msg) {
		m_msg.setText(msg);
	}
}

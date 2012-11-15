package com.nsoll.smt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LogAdapter extends BaseAdapter {
	
	private Context m_context;
	
	private List<LogItem> m_items = new ArrayList<LogItem>();
	
	public LogAdapter(Context context) {
		m_context = context;
	}
	
	public void add(LogItem data) {
		m_items.add(data);
		//notifyDataSetChanged();
	}

	public int getCount() {
		return m_items.size();
	}

	public Object getItem(int position) {
		return m_items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LogItemView itemView;
		if (convertView == null) {
			itemView = new LogItemView(m_context, m_items.get(position));
		} else {
			itemView = (LogItemView) convertView;
			
			itemView.setIp(m_items.get(position).getIp());
			itemView.setLogtime(m_items.get(position).getLogtime());
			itemView.setMsg(m_items.get(position).getMsg());
		}
		return itemView;
	}

}

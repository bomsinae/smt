package com.nsoll.smt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SmtAdapter extends BaseAdapter {
	
	private Context m_context;
	
	private List<ListItem> m_items = new ArrayList<ListItem>();
	
	public SmtAdapter(Context context) {
		m_context = context;
	}
	
	public void add(ListItem data) {
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
		ItemView itemView;
		if (convertView == null) {
			itemView = new ItemView(m_context, m_items.get(position));
		} else {
			itemView = (ItemView) convertView;
			
			itemView.setName(m_items.get(position).getName());
			itemView.setSubname(m_items.get(position).getSubname());
			itemView.setRegdate(m_items.get(position).getRegdate());
		}
		return itemView;
	}

}

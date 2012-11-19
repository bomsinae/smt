package com.nsoll.smt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SetAdapter extends BaseAdapter {
	
	private Context m_context;
	
	private List<SetItem> m_items = new ArrayList<SetItem>();
	
	public SetAdapter(Context context) {
		m_context = context;
	}
	
	public void add(SetItem data) {
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
		SetItemView itemView;
		if (convertView == null) {
			itemView = new SetItemView(m_context, m_items.get(position));
		} else {
			itemView = (SetItemView) convertView;
			
			itemView.setCheckname(m_items.get(position).getCheckname());
			itemView.setCheck(m_items.get(position).getCheck());
		}
		return itemView;
	}

}

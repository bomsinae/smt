package com.nsoll.smt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MainMenuAdapter extends BaseAdapter {
	
	private Context m_context;
	
	private List<MainItem> m_items = new ArrayList<MainItem>();
	
	public MainMenuAdapter(Context context) {
		m_context = context;
	}
	
	public void add(MainItem data) {
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
		MainItemView itemView;
		if (convertView == null) {
			itemView = new MainItemView(m_context, m_items.get(position));
		} else {
			itemView = (MainItemView) convertView;
			
			itemView.setHeadColor(m_items.get(position).getHeadColor());
			itemView.setMenu(m_items.get(position).getMenu());
		}
		return itemView;
	}

}

package com.nsoll.smt;

public class MainItem {

	private int m_headcolor = 0xffffffff;
	private String m_menu = null;
	
	public MainItem(int color, String menu) {
		m_headcolor = color;
		m_menu = menu;
	}

	public void setHeadColor(int color){
		m_headcolor = color;
	}
	
	public void setMenu(String menu){
		m_menu = menu;
	}
	
	public int getHeadColor(){
		return m_headcolor;
	}
	public String getMenu(){
		return m_menu;
	}		
}

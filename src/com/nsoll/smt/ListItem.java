package com.nsoll.smt;

public class ListItem {

	private String m_name = null;
	private String m_subname = null;
	private String m_regdate = null;
	
	public ListItem(String name, String subname, String regdate) {
		m_name = name;
		m_subname = subname;
		m_regdate = regdate;
	}

	public void setName(String name){
		m_name = name;
	}
	
	public void setSubname(String subname){
		m_subname = subname;
	}
	
	public void setRegdate(String regdate){
		m_regdate = regdate;
	}
	
	public String getName(){
		return m_name;
	}
	public String getSubname(){
		return m_subname;
	}
	public String getRegdate(){
		return m_regdate;
	}		
}

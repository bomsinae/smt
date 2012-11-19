package com.nsoll.smt;

public class SetItem {

	private String m_checkname = null;
	private Boolean m_checked = null;
	
	public SetItem(String checkname, Boolean checked) {
		m_checkname = checkname;
		m_checked = checked;
	}

	public void setCheckname(String checkname){
		m_checkname = checkname;
	}
	
	public void setCheck(Boolean check){
		m_checked = check;
	}
	
	public String getCheckname(){
		return m_checkname;
	}
	public Boolean getCheck(){
		return m_checked;
	}		
}

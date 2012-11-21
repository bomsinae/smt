package com.nsoll.smt;

public class SetItem {

	private String m_checkname = null;
	private String m_keyname = null;
	private Boolean m_checked = null;
	
	public SetItem(String checkname, String keyname, Boolean checked) {
		m_checkname = checkname;
		m_keyname = keyname;
		m_checked = checked;
	}

	public void setCheckname(String checkname){
		m_checkname = checkname;
	}
	public void setKeyname(String keyname){
		m_keyname = keyname;
	}
	public void setCheck(Boolean check){
		m_checked = check;
	}
	
	public String getCheckname(){
		return m_checkname;
	}
	public String getKeyname(){
		return m_keyname;
	}
	public Boolean getCheck(){
		return m_checked;
	}		
}

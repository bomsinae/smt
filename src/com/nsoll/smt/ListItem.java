package com.nsoll.smt;

public class ListItem {

	private Integer m_no = null;
	private String m_name = null;
	private String m_subname = null;
	private String m_regdate = null;
	
	public ListItem(Integer no, String name, String subname, String regdate) {
		m_no = no;
		m_name = name;
		m_subname = subname;
		m_regdate = regdate;
	}
	
	public void setNo(Integer no){
		m_no = no;
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
	
	public Integer getNo(){
		return m_no;
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

package com.nsoll.smt;

public class LogItem {

	private String m_ip = null;
	private String m_logtime = null;
	private String m_msg = null;
	
	public LogItem(String ip, String logtime, String msg) {
		m_ip = ip;
		m_logtime = logtime;
		m_msg = msg;
	}

	public void setIp(String ip){
		m_ip = ip;
	}
	
	public void setLogtime(String logtime){
		m_logtime = logtime;
	}
	
	public void setMsg(String msg){
		m_msg = msg;
	}
	
	public String getIp(){
		return m_ip;
	}
	public String getLogtime(){
		return m_logtime;
	}
	public String getMsg(){
		return m_msg;
	}		
}

package com.zkr.control;
/**
 * 记录执行cmd机器信息bean对象
 * @author lihongchen
 * 
 */
public class MachineInfoBean {

	private String id;
	private String m_user;
	private String m_pwd;
	private String m_ip;
	private String m_proLocat;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getM_user() {
		return m_user;
	}
	public void setM_user(String m_user) {
		this.m_user = m_user;
	}
	public String getM_pwd() {
		return m_pwd;
	}
	public void setM_pwd(String m_pwd) {
		this.m_pwd = m_pwd;
	}
	public String getM_ip() {
		return m_ip;
	}
	public void setM_ip(String m_ip) {
		this.m_ip = m_ip;
	}
	public String getM_proLocat() {
		return m_proLocat;
	}
	public void setM_proLocat(String m_proLocat) {
		this.m_proLocat = m_proLocat;
	}
}

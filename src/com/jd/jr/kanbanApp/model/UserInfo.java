package com.jd.jr.kanbanApp.model;

public class UserInfo {
	/**
	 * 用户账号
	 */
	private String name;
	/**
	 * 用户别名
	 */
	private String realName;
	/**
	 * 网关返回的唯一键
	 */
	private String ticket;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
}

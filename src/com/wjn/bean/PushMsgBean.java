package com.wjn.bean;

public class PushMsgBean {
	private String gatherId;
	private String username;
	private String time;
	private String text;
	public String getGatherId() {
		return gatherId;
	}
	public void setGatherId(String gatherId) {
		this.gatherId = gatherId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public PushMsgBean(String gatherId, String username, String time,
			String text) {
		super();
		this.gatherId = gatherId;
		this.username = username;
		this.time = time;
		this.text = text;
	}
	public PushMsgBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

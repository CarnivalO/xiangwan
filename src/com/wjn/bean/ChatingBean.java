package com.wjn.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class ChatingBean extends BmobObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String gatherBeanID;

	private BmobFile usericon;
	private String username;
	private String time;
	private String text;
	
	public BmobFile getUsericon() {
		return usericon;
	}
	public void setUsericon(BmobFile usericon) {
		this.usericon = usericon;
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

	
	public ChatingBean(String gatherBeanID, BmobFile usericon, String username,
			String time, String text) {
		super();
		this.gatherBeanID = gatherBeanID;
		this.usericon = usericon;
		this.username = username;
		this.time = time;
		this.text = text;
	}
	public ChatingBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChatingBean(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}
	public String getGatherBeanID() {
		return gatherBeanID;
	}
	public void setGatherBeanID(String gatherBeanID) {
		this.gatherBeanID = gatherBeanID;
	}
	@Override
	public String toString() {
		return "ChatingBean [gatherBeanID=" + gatherBeanID + ", usericon="
				+ usericon + ", username=" + username + ", time=" + time
				+ ", text=" + text + "]";
	}
	
	
}

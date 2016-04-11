package com.wyl.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


/**
 * 引入云服务器bmob
 * @author wangyinliang
 * 应用密钥
 * 7cfb170bbc62bff0c6e793580a0c9bf6
 */
public class UserBean extends BmobUser{
	private BmobFile usericon;
	private String ub_password;
	public BmobFile getUsericon() {
		return usericon;
	}

	public void setUsericon(BmobFile usericon) {
		this.usericon = usericon;
	}

	public String getUb_password() {
		return ub_password;
	}

	public void setUb_password(String ub_password) {
		this.ub_password = ub_password;
	}
}

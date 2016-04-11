 package com.wyl.application;

import com.wyl.bean.UserBean;

import cn.bmob.v3.Bmob;
import android.app.Application;
/**
 * 是Application的实体类，Application对象的生命周期上整个应用中最长的，
 * 它的生命周期等同于应用的生命周期
 * @author wangyinliang
 *
 */
public class GatherApplication extends Application {
	private UserBean user;
	//
	public static GatherApplication appli;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		appli = this;
		
		Bmob.initialize(this, "7cfb170bbc62bff0c6e793580a0c9bf6");
	}
	public UserBean getUser() {
		return user;
	}
	public void setUser(UserBean user) {
		this.user = user;
	}
}

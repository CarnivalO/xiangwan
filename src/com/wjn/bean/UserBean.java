package com.wjn.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


/**
 * 引入云服务器bmob
 * @author wangjianian
 * 应用密钥
 * 8dc0b8513a70a86b4f7f70a291703cf6
 */
public class UserBean extends BmobUser{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BmobFile usericon;
	private String ub_password;
	
	private List<String> myGathers;//我发布的活动
	private List<String> loveGatherId;//我收藏的
	private List<String> canJiaGatherId;//付款的活动id
	private List<String> orderIds;//订单号
	
	
	public List<String> getMyGathers() {
		return myGathers;
	}

	public void setMyGathers(List<String> myGathers) {
		this.myGathers = myGathers;
	}

	public List<String> getLoveGatherId() {
		if(loveGatherId == null){
			return new ArrayList<String>();
		}
		return loveGatherId;
	}

	public void setLoveGatherId(List<String> loveGatherId) {
		this.loveGatherId = loveGatherId;
	}

	public List<String> getCanJiaGatherId() {
		if(canJiaGatherId == null){
			return new ArrayList<String>();
		}
		return canJiaGatherId;
	}

	public void setCanJiaGatherId(List<String> canJiaGatherId) {
		this.canJiaGatherId = canJiaGatherId;
	}

	public List<String> getOrderIds() {
		if(orderIds == null){
			return new ArrayList<String>();
		}
		return orderIds;
	}

	public void setOrderIds(List<String> orderIds) {
		this.orderIds = orderIds;
	}

	public List<String> getYouhui() {
		if(youhui == null){
			return new ArrayList<String>();
		}
		return youhui;
	}

	public void setYouhui(List<String> youhui) {
		this.youhui = youhui;
	}

	private List<String> youhui;
	
	
	
	
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

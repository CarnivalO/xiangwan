package com.wjn.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

import com.baidu.mapapi.search.core.PoiInfo;
/**
 * 活动对象
 * @author xmwjn
 *
 */
public class GatherBean extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;//id  不需要用户手动输入
	
	private String gatherClass;//活动类型  用户选择 ````
	private String gatherTitle;//活动简介  用户输入 ````
	private String gatherTime;//活动开始时间   用户选择 ````
	private String gatherName;//活动名称   用户输入 ````
	private String gatherCity;//活动所在城市   用户选择/输入
	private String gatherSite;//活动集合地点   用户选择/输入 
	private String gatherRMB; //活动价格       用户输入 ````
	private String gatherIntro;//活动详细介绍  用户输入 ````
	private BmobFile gatherJPG;//活动图片    //用户上传````
	
	private PoiInfo location;//活动经纬度   用户选择/输入
	private int pingluns;//用户评论数量   //系统录入

	public PoiInfo getLocation() {
		return location;
	}
	public void setLocation(PoiInfo info) {
		this.location = info;
	}
	private List<String> paymentUserId;//参与付款的用户Id   系统录入
	private List<String> paymentUserName;//参与付款的用户Name   系统录入
	private List<String> startUserId;//活动开始 ，到活动现场的用户Id   系统录入
	private List<String> startUserName;
	private Boolean flag;// 当前活动状态(是否开始)   系统录入
	private String gatherUserId;//活动发起用户Id 用来查询用户表  系统自动获取填入
	private BmobFile gatherIcon;//发起活动者头像  系统自动获取填入
	private List<String> praiseUsers;//记录点赞的用户名  //系统录入
	private int praiseUserscount;//记录点赞的次数
	
	private BmobGeoPoint gpsAdd;//Bmob服务器需要通过位置对象进行距离判断
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGatherClass() {
		return gatherClass;
	}
	public void setGatherClass(String gatherClass) {
		this.gatherClass = gatherClass;
	}
	public String getGatherTitle() {
		return gatherTitle;
	}
	public void setGatherTitle(String gatherTitle) {
		this.gatherTitle = gatherTitle;
	}
	public String getGatherTime() {
		return gatherTime;
	}
	public void setGatherTime(String gatherTime) {
		this.gatherTime = gatherTime;
	}
	public String getGatherName() {
		return gatherName;
	}
	public void setGatherName(String gatherName) {
		this.gatherName = gatherName;
	}
	public String getGatherCity() {
		return gatherCity;
	}
	public void setGatherCity(String gatherCity) {
		this.gatherCity = gatherCity;
	}
	public String getGatherSite() {
		return gatherSite;
	}
	public void setGatherSite(String gatherSite) {
		this.gatherSite = gatherSite;
	}
	public String getGatherRMB() {
		return gatherRMB;
	}
	public void setGatherRMB(String gatherRMB) {
		this.gatherRMB = gatherRMB;
	}
	public String getGatherIntro() {
		return gatherIntro;
	}
	public void setGatherIntro(String gatherIntro) {
		this.gatherIntro = gatherIntro;
	}
	public BmobFile getGatherJPG() {
		return gatherJPG;
	}
	public void setGatherJPG(BmobFile gatherJPG) {
		this.gatherJPG = gatherJPG;
	}
	public List<String> getPaymentUserId() {
		if(paymentUserId == null){
			return new ArrayList<String>();
		}
		return paymentUserId;
	}
	public void setPaymentUserId(List<String> paymentUserId) {
		this.paymentUserId = paymentUserId;
	}
	public List<String> getStartUserId() {
		if(startUserId == null){
			return new ArrayList<String>();
		}
		return startUserId;
	}
	public void setStartUserId(List<String> startUserId) {
		this.startUserId = startUserId;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public String getGatherUserId() {
		return gatherUserId;
	}
	public void setGatherUserId(String gatherUserId) {
		this.gatherUserId = gatherUserId;
	}
	public BmobFile getGatherIcon() {
		return gatherIcon;
	}
	public void setGatherIcon(BmobFile gatherIcon) {
		this.gatherIcon = gatherIcon;
	}
	public List<String> getPraiseUsers() {
		if(praiseUsers == null){
			return new ArrayList<String>();
		}
		return praiseUsers;
	}
	public void setPraiseUsers(List<String> praiseUsers) {
		this.praiseUsers = praiseUsers;
		this.praiseUserscount = praiseUsers.size();
	}
	public GatherBean(String id, String gatherClass, String gatherTitle,
			String gatherTime, String gatherName, String gatherCity,
			String gatherSite, String gatherRMB, String gatherIntro,
			BmobFile gatherJPG, List<String> paymentUserId,
			List<String> startUserId, Boolean flag, String gatherUserId,
			BmobFile gatherIcon, List<String> praiseUsers) {
		super();
		this.id = id;
		this.gatherClass = gatherClass;
		this.gatherTitle = gatherTitle;
		this.gatherTime = gatherTime;
		this.gatherName = gatherName;
		this.gatherCity = gatherCity;
		this.gatherSite = gatherSite;
		this.gatherRMB = gatherRMB;
		this.gatherIntro = gatherIntro;
		this.gatherJPG = gatherJPG;
		this.paymentUserId = paymentUserId;
		this.startUserId = startUserId;
		this.flag = flag;
		this.gatherUserId = gatherUserId;
		this.gatherIcon = gatherIcon;
		this.praiseUsers = praiseUsers;
	}
	public GatherBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GatherBean(String gatherClass, String gatherTitle,
			String gatherTime, String gatherName, String gatherCity,
			String gatherSite, String gatherRMB, String gatherIntro,
			BmobFile gatherJPG, List<String> paymentUserId,
			List<String> startUserId, Boolean flag, String gatherUserId,
			BmobFile gatherIcon, List<String> praiseUsers) {
		super();
		this.gatherClass = gatherClass;
		this.gatherTitle = gatherTitle;
		this.gatherTime = gatherTime;
		this.gatherName = gatherName;
		this.gatherCity = gatherCity;
		this.gatherSite = gatherSite;
		this.gatherRMB = gatherRMB;
		this.gatherIntro = gatherIntro;
		this.gatherJPG = gatherJPG;
		this.paymentUserId = paymentUserId;
		this.startUserId = startUserId;
		this.flag = flag;
		this.gatherUserId = gatherUserId;
		this.gatherIcon = gatherIcon;
		this.praiseUsers = praiseUsers;
	}
	public BmobGeoPoint getGpsAdd() {
		return gpsAdd;
	}
	public void setGpsAdd(BmobGeoPoint gpsAdd) {
		this.gpsAdd = gpsAdd;
	}
	public int getPraiseUserscount() {
		return praiseUserscount;
	}
	public List<String> getPaymentUserName() {
		if(paymentUserName == null){
			return new ArrayList<String>();
		}
		return paymentUserName;
	}
	public void setPaymentUserName(List<String> paymentUserName) {
		this.paymentUserName = paymentUserName;
	}
	public List<String> getStartUserName() {
		if(startUserName == null){
			return new ArrayList<String>();
		}
		return startUserName;
	}
	public void setStartUserName(List<String> startUserName) {
		this.startUserName = startUserName;
	}
	public int getPingluns() {
		return pingluns;
	}
	public void setPingluns(int pingluns) {
		this.pingluns = pingluns;
	}
	
}

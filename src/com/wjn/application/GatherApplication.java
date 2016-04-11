 package com.wjn.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.bmob.pay.tool.BmobPay;
import com.wjn.base.BaseActivity;
import com.wjn.bean.GatherBean;
import com.wjn.bean.UserBean;
/**
 * Application��ʵ���࣬Application�������������������Ӧ������ģ�
 * �����������ڵ�ͬ��Ӧ�õ���������
 * �ٶȵ�ͼ��0jujoC6yx3kTuggCQRHyNbIG
 * @author wangjianian
 *
 */
public class GatherApplication extends Application {
	//�ٶȵ�ͼ�������
	private PoiInfo info;
	//��ǰ�����
	private List<GatherBean> gathers;
	private UserBean ub;
	private GatherBean item_showgather;
	private GatherBean notifgather;
	public static GatherApplication appli;
	//adapter�е�activity
	private Activity adapterAct;
	private Activity chatingAct;
	private List<LatLng> locations;
	private String moreName; 
	private String Classpager;
	
	//�ٶȶ�λ
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new BDLocationListener() {
		
		@Override
		public void onReceiveLocation(BDLocation location) {
			LatLng startKM = new LatLng(location.getLatitude(), location.getLongitude());
			//Log.i("Log", "��λ���"+location.getLongitude()+","+location.getLatitude());
			if(locations == null){
				locations = new ArrayList<LatLng>();
			}
			if(locations.size()>5){
				locations.clear();
			}
			locations.add(startKM);
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		appli = this;
		//��ʼ���ٶ�SDK��û�г�ʼ���޷����ذٶ�Ӧ��
		SDKInitializer.initialize(getApplicationContext());
		Bmob.initialize(this, "8dc0b8513a70a86b4f7f70a291703cf6");
		BmobPay.init(this,"8dc0b8513a70a86b4f7f70a291703cf6");
		 // ʹ�����ͷ���ʱ�ĳ�ʼ������
	    BmobInstallation.getCurrentInstallation(this).save();
	    // �������ͷ���
	    BmobPush.startWork(this, "8dc0b8513a70a86b4f7f70a291703cf6");
		locations = new ArrayList<LatLng>();
		initBaiDu();
		//BmobUpdateAgent.initAppVersion(this);
	}
	private void initBaiDu() {
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(myListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		int span = 200000;
		option.setScanSpan(span);
		option.setIsNeedAddress(false);
		option.setOpenGps(true);
		option.setLocationNotify(true);
		option.setIsNeedLocationDescribe(false);
		option.setIsNeedLocationPoiList(false);
		option.setIgnoreKillProcess(false);
		option.SetIgnoreCacheException(false);
		option.setEnableSimulateGps(false);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}
	public UserBean getUb() {
		if(ub == null){
			return BmobUser.getCurrentUser(getApplicationContext(),UserBean.class);
		}
		return ub;
	}
	public void setUb(UserBean ub) {
		this.ub = ub;
	}
	public BaseActivity getAdapterAct() {
		return (BaseActivity) adapterAct;
	}
	public void setAdapterAct(Activity adapterAct) {
		this.adapterAct = adapterAct;
	}
	public List<LatLng> getLocations() {
		if(locations == null){
			return new ArrayList<LatLng>();
		}
		return locations;
	}
	public void setLocations(List<LatLng> locations) {
		this.locations = locations;
	}
	public PoiInfo getInfo() {
		return info;
	}
	public void setInfo(PoiInfo info) {
		this.info = info;
	}
	public List<GatherBean> getGathers(){
		return gathers;
	}
	public void setGathers(List<GatherBean> gathers) {
		this.gathers = gathers;
	}
	public String getMoreName() {
		return moreName;
	}
	public void setMoreName(String moreName) {
		this.moreName = moreName;
	}
	public String getClasspager() {
		return Classpager;
	}
	public void setClasspager(String classpager) {
		Classpager = classpager;
	}
	public GatherBean getItem_showgather() {
		return item_showgather;
	}
	public void setItem_showgather(GatherBean item_showgather) {
		this.item_showgather = item_showgather;
	}
	public Activity getChatingAct() {
		return chatingAct;
	}
	public void setChatingAct(Activity chatingAct) {
		this.chatingAct = chatingAct;
	}
	public GatherBean getNotifgather() {
		return notifgather;
	}
	public void setNotifgather(GatherBean notifgather) {
		this.notifgather = notifgather;
	}
}

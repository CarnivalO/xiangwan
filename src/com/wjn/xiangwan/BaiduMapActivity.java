package com.wjn.xiangwan;

/**
 * 百度地图activity
 * 功能，检索
 */
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseInterface;


public class BaiduMapActivity extends BaseActivity implements BaseInterface{
	private MapView mMapView;
	private ImageView baiduMapsou;
	private EditText edit,city;
	private PoiSearch mPoiSearch;
	private OnGetPoiSearchResultListener poiListener;
	private BaiduMap mBaiduMap;
	private PoiResult mResult;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);  
		setContentView(R.layout.act_baidu);
		initViews();
		initDatas();
		initViewOper();
	}
	@Override
	public void initViews() {
		//获取id
		 mMapView = (MapView) findViewById(R.id.bmapView);  
		 baiduMapsou = imageById(R.id.act_baidu_sou);
		 edit = etById(R.id.act_baidu_edit);
		 city = etById(R.id.act_baidu_city);
		 
		 mPoiSearch = PoiSearch.newInstance();
	}

	@Override
	public void initDatas() {
		//初始化数据
		mBaiduMap = mMapView.getMap();
		poiListener = new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult result) {
				// 获取POI结果
				if(result!=null){
					//BaiduMapActivity.this.toastS(result.getAllPoi().get(0).name);

					mResult = result;
					mBaiduMap.clear();
					PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
					mBaiduMap.setOnMarkerClickListener(overlay);
					//设置PoiOverlay数据
					overlay.setData(result);
					//添加PoiOverlay到地图中
					overlay.addToMap();
					overlay.zoomToSpan();
					return;
				}else{
					BaiduMapActivity.this.toastS("查无数据！");
				}
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult result) {
				// 获取详情页检索
				
			}
		};
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
	}

	@Override
	public void initViewOper() {
		baiduMapsou.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPoiSearch.searchInCity((new PoiCitySearchOption())
						.city(city.getText().toString().trim()).keyword(edit.getText().toString().trim()).pageNum(0));
			}
		});
	}
	 @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
	        mMapView.onDestroy();  
	    }  
	    @Override  
	    protected void onResume() {  
	        super.onResume();  
	        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
	        mMapView.onResume();  
	        }  
	    @Override  
	    protected void onPause() {  
	        super.onPause();  
	        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
	        mMapView.onPause();  
	        }  
	    private class MyPoiOverlay extends PoiOverlay{
	    	public MyPoiOverlay(BaiduMap baiduMap){
	    		super(baiduMap);
	    	}
	    	private int mIndex;
	    	public boolean onPoiClick(int index){
	    		super.onPoiClick(index);
	    		mIndex = index;
	    		BaiduMapActivity.this.toastS(mResult.getAllPoi().get(index).name);
	    	
	    		//创建infowindow展示的view
	    		toastS("正在搜索...");
	    		Button button = new Button(getApplicationContext());
	    		button.setTextColor(Color.parseColor("#0000ff"));
	    		button.setBackgroundResource(R.drawable.but_selector);
	    		button.setText(mResult.getAllPoi().get(index).name+"\n\r"+"确认选择");
	    		//定义用于显示该infoWindow的坐标点
	    		LatLng pt = mResult.getAllPoi().get(index).location;
	    		//创建infowindow,传入view,地理坐标，y轴偏移
	    		InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
	    		//显示infowindow
	    		mBaiduMap.showInfoWindow(mInfoWindow);
	    		button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						GatherApplication.appli.setInfo(mResult.getAllPoi().get(mIndex));
						BaiduMapActivity.this.finish();
					}
				});
	    		return true;
	    	}
	    }
	    }

package com.wjn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.wjn.adapter.XListViewAdapter;
import com.wjn.application.GatherApplication;
import com.wjn.base.BaseFragment;
import com.wjn.base.BaseInterface;
import com.wjn.bean.GatherBean;
import com.wjn.view.XListView;
import com.wjn.view.XListView.IXListViewListener;
import com.wjn.xiangwan.HomeActivity;
import com.wjn.xiangwan.ItemShowActivity;
import com.wjn.xiangwan.R;



public class HomeFragment extends BaseFragment implements BaseInterface{
	private HomeActivity act;
	private XListView xList;
	private XListViewAdapter adapter;
	private List<GatherBean> gathers;
//	加载更多数据的计数器
	private int gatherCount = 0;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = (HomeActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//添加布局文件
		return inflater.inflate(R.layout.fragment_home,null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();
	}
	
	
	@Override
	public void initViews() {
	xList = (XListView)findViewById(R.id.fragment_home_xlist);
	//开启XlistView上拉加载更多
	xList.setPullLoadEnable(true);
	
	}

	@Override
	public void initDatas() {
		//向活动中添加数据
		gathers = GatherApplication.appli.getGathers();
		//判断活动是否为空
		if (gathers == null) {
			gathers = new ArrayList<GatherBean>();
		}
		//计算活动的数量
		gatherCount = gathers.size();
		GatherApplication.appli.setAdapterAct(act);
		adapter = new XListViewAdapter(gathers);
		xList.setAdapter(adapter);
	}

	@Override
	public void initViewOper() {
		xList.setXListViewListener(new IXListViewListener() {
//			下拉刷新
			public void onRefresh() {

//				去网络下载数据  刷新列表
				//获取主页数据
				BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
				//根据score字段降序显示数据
				query.order("-createdAt");
				//更新限制条数
				query.setLimit(10);
				//查询10条
				query.findObjects(act, new FindListener<GatherBean>() {
					
					@Override
					public void onSuccess(List<GatherBean> arg0) {
						if (arg0 == null) {
							act.toastS("都是最新的啦");
							return;
						}
						//获取当前数据条数
						gatherCount += arg0.size();
						GatherApplication.appli.setGathers(arg0);
						//数据更新
						gathers = arg0;
						//更新xlistiew中数据
						adapter.updateGathers(gathers);
						//刷新视图
						adapter.notifyDataSetChanged();
						//关闭刷新的视图
						xList.stopRefresh();
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						//关闭刷新的视图
						xList.stopRefresh();
					}
				});
			}
						//加载更多刷新（已经获取的数据算上）
			public void onLoadMore() {
				//获取主页数据
				BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
				//根据score字段降序显示数据
				query.order("-createdAt");
				//更新限制条数
				query.setLimit(10);
				query.setSkip(gatherCount); 
				query.findObjects(act, new FindListener<GatherBean>() {
					
					@Override
					public void onSuccess(List<GatherBean> arg0) {
						if (arg0 == null) {
							act.toastS("没有更多数据了");
							return;
						}
						gathers.addAll(arg0);
						//获取当前数据条数
						gatherCount += arg0.size();
						GatherApplication.appli.setGathers(gathers);
						//更新xlistiew中数据
						adapter.updateGathers(gathers);
						//刷新视图
						adapter.notifyDataSetChanged();
//						关闭加载更多的视图
						xList.stopLoadMore();
					}
					
					@Override
					public void onError(int arg0, String arg1) {
//						关闭加载更多的视图
						xList.stopLoadMore();
					}
				});
			}
		});
		xList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//跳转
				GatherApplication.appli.setItem_showgather(gathers.get(position-1));
				act.startActivity(new Intent(act,ItemShowActivity.class));
			}
			
		});
	}
}

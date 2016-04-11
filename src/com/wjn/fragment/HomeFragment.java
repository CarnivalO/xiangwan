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
//	���ظ������ݵļ�����
	private int gatherCount = 0;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = (HomeActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//��Ӳ����ļ�
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
	//����XlistView�������ظ���
	xList.setPullLoadEnable(true);
	
	}

	@Override
	public void initDatas() {
		//�����������
		gathers = GatherApplication.appli.getGathers();
		//�жϻ�Ƿ�Ϊ��
		if (gathers == null) {
			gathers = new ArrayList<GatherBean>();
		}
		//����������
		gatherCount = gathers.size();
		GatherApplication.appli.setAdapterAct(act);
		adapter = new XListViewAdapter(gathers);
		xList.setAdapter(adapter);
	}

	@Override
	public void initViewOper() {
		xList.setXListViewListener(new IXListViewListener() {
//			����ˢ��
			public void onRefresh() {

//				ȥ������������  ˢ���б�
				//��ȡ��ҳ����
				BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
				//����score�ֶν�����ʾ����
				query.order("-createdAt");
				//������������
				query.setLimit(10);
				//��ѯ10��
				query.findObjects(act, new FindListener<GatherBean>() {
					
					@Override
					public void onSuccess(List<GatherBean> arg0) {
						if (arg0 == null) {
							act.toastS("�������µ���");
							return;
						}
						//��ȡ��ǰ��������
						gatherCount += arg0.size();
						GatherApplication.appli.setGathers(arg0);
						//���ݸ���
						gathers = arg0;
						//����xlistiew������
						adapter.updateGathers(gathers);
						//ˢ����ͼ
						adapter.notifyDataSetChanged();
						//�ر�ˢ�µ���ͼ
						xList.stopRefresh();
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						//�ر�ˢ�µ���ͼ
						xList.stopRefresh();
					}
				});
			}
						//���ظ���ˢ�£��Ѿ���ȡ���������ϣ�
			public void onLoadMore() {
				//��ȡ��ҳ����
				BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
				//����score�ֶν�����ʾ����
				query.order("-createdAt");
				//������������
				query.setLimit(10);
				query.setSkip(gatherCount); 
				query.findObjects(act, new FindListener<GatherBean>() {
					
					@Override
					public void onSuccess(List<GatherBean> arg0) {
						if (arg0 == null) {
							act.toastS("û�и���������");
							return;
						}
						gathers.addAll(arg0);
						//��ȡ��ǰ��������
						gatherCount += arg0.size();
						GatherApplication.appli.setGathers(gathers);
						//����xlistiew������
						adapter.updateGathers(gathers);
						//ˢ����ͼ
						adapter.notifyDataSetChanged();
//						�رռ��ظ������ͼ
						xList.stopLoadMore();
					}
					
					@Override
					public void onError(int arg0, String arg1) {
//						�رռ��ظ������ͼ
						xList.stopLoadMore();
					}
				});
			}
		});
		xList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//��ת
				GatherApplication.appli.setItem_showgather(gathers.get(position-1));
				act.startActivity(new Intent(act,ItemShowActivity.class));
			}
			
		});
	}
}

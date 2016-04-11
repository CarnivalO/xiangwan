package com.wjn.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wjn.application.GatherApplication;
import com.wjn.base.BaseFragment;
import com.wjn.base.BaseInterface;
import com.wjn.xiangwan.GatherShowActivity;
import com.wjn.xiangwan.HomeActivity;
import com.wjn.xiangwan.R;

public class MoreFragment extends BaseFragment implements BaseInterface{
	private HomeActivity act;
	private EditText edit;
	private ImageView img;
	private Button free,remen,fujin;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = (HomeActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//��Ӳ����ļ�		
		return inflater.inflate(R.layout.fragment_more, null);
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
		edit = etById(R.id.fragment_more_edit);
		img = imageById(R.id.fragment_more_search);
		remen = butById(R.id.fragment_more_remen);
		fujin = butById(R.id.fragment_more_fujin);
		free = butById(R.id.fragment_more_free);
		
	}

	@Override
	public void initDatas() {
		
	}

	@Override
	public void initViewOper() {
		img.setOnClickListener(new OnClickListener() {
			//������ת������key�Ĳ�ͬ��ʾ��ͬҳ��
			@Override
			public void onClick(View v) {
				//��ȡ��������������ݣ���ת���չʾ����
				GatherApplication.appli.setClasspager(edit.getText().toString().trim());
				Intent intent = new Intent(act,GatherShowActivity.class);
				intent.putExtra("key", 3);
				act.startActivity(intent);
			}
		});
		//��������ת
		free.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(act,GatherShowActivity.class);
				intent.putExtra("key", 4);
				act.startActivity(intent);
			}
		});
		//���������ת
		remen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(act,GatherShowActivity.class);
				intent.putExtra("key", 5);
				act.startActivity(intent);
			}
		});
		//���������ת
		fujin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(act,GatherShowActivity.class);
				intent.putExtra("key", 6);
				act.startActivity(intent);
			}
		});
	}

}

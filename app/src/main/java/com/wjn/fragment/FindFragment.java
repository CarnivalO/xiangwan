package com.wyl.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyl.base.BaseFragment;
import com.wyl.base.BaseInterface;
import com.wyl.wb.HomeActivity;
import com.wyl.wb.R;

public class FindFragment extends BaseFragment implements BaseInterface{
	private HomeActivity home;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		home = (HomeActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_find, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();
	}
	
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initDatas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initViewOper() {
		// TODO Auto-generated method stub
		
	}

}

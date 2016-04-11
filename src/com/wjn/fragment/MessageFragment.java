package com.wjn.fragment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wjn.base.BaseFragment;
import com.wjn.base.BaseInterface;
import com.wjn.bean.GatherBean;
import com.wjn.bean.UserBean;
import com.wjn.xiangwan.HomeActivity;
import com.wjn.xiangwan.R;

public class MessageFragment extends BaseFragment implements BaseInterface{
	private HomeActivity act;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = (HomeActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message, null);
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
		
	}

	@Override
	public void initDatas() {
		
	}

	@Override
	public void initViewOper() {
		
	}

}

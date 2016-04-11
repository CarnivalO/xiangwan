package com.wjn.xiangwan;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseInterface;

public class MyPayActivity extends BaseActivity implements BaseInterface {
	
	private ListView list;
	private ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_pay);
		initViews();
		initDatas();
		initViewOper();
	}
	@Override
	public void initViews() {
		list = (ListView) findViewById(R.id.act_pay_list);
		
	}

	@Override
	public void initDatas() {
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,GatherApplication.appli.getUb().getCanJiaGatherId());
		
	}

	@Override
	public void initViewOper() {
		list.setAdapter(adapter);
	}

}

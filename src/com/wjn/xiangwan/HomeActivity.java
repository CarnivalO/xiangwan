package com.wjn.xiangwan;



import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjn.adapter.MyFragmentPagerAdapter;
import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseFragment;
import com.wjn.base.BaseInterface;
import com.wjn.fragment.HomeFragment;
import com.wjn.fragment.MessageFragment;
import com.wjn.fragment.MoreFragment;
import com.wjn.fragment.PersonalFragment;

public class HomeActivity extends BaseActivity implements BaseInterface, OnClickListener{
	//给选项卡的内容一个容器
	private LinearLayout[] linears = new LinearLayout[4];
	private ImageView[] images = new ImageView[4];
	private TextView[] texts= new TextView[4];
	private LinearLayout sendGather;
	
	//ID一个容器
	private int[] linearIds={R.id.act_home_home,R.id.act_home_message,R.id.act_home_personal,R.id.act_home_settings};
	private int[] imageIds= {R.id.act_home_home_iv,R.id.act_home_message_iv,R.id.act_home_personal_iv,R.id.act_home_settings_iv};
	private int[] textsIds = {R.id.act_home_home_tv,R.id.act_home_message_tv,R.id.act_home_personal_tv,R.id.act_home_settings_tv};
	
	//选项卡长度
	private static final int VIEWLENGTH = 4;
	//快关图片Id容器
	private int[] imagKs={R.drawable.home_home_liang,R.drawable.home_message_liang,R.drawable.home_geren_liang,R.drawable.home_settings_liang};
	private int[] imagGs = {R.drawable.home_home_bu,R.drawable.home_message_bu,R.drawable.home_geren_bu,R.drawable.home_settings_bu};
	
	//ViewPager
	private ViewPager pager;
	
	//私有化Fragment
	private List<BaseFragment> fragments;
	
	//退出标记
	private boolean flag = false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_home);
		initViews();
		initDatas();
		initViewOper();
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		for(int i=0;i<VIEWLENGTH;i++){
			linears[i] = linearById(linearIds[i]);
			images[i]=imageById(imageIds[i]);
			texts[i] = tvById(textsIds[i]);
			linears[i].setOnClickListener(this);
		}
		pager = (ViewPager) findViewById(R.id.act_home_viewpager);
		sendGather = linearById(R.id.act_home_sendgather);
	}
  
	@Override
	public void initDatas() {
		// TODO Auto-generated method stub
		fragments = new ArrayList<BaseFragment>();
		fragments.add(new HomeFragment());
		fragments.add(new MessageFragment());
		fragments.add(new PersonalFragment());
		fragments.add(new MoreFragment());
		pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
	}

	@Override
	public void initViewOper() {
		//viewPager的滑动事件
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				updataViews(arg0); 
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		sendGather.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(HomeActivity.this,SendGatherActivity.class));
			}
		});
		
	}
//主页设置点切换
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_home_home:
			updataViews(0);
			pager.setCurrentItem(0);
			break;
		case R.id.act_home_message:
			updataViews(1);
			pager.setCurrentItem(1);
			break;
		case R.id.act_home_personal:
			updataViews(2);
			pager.setCurrentItem(2);
			break;
		case R.id.act_home_settings:
			updataViews(3);
			pager.setCurrentItem(3);
			break;
		}
	}
//设置下拉列表
	private void updataViews(int id) {
		for(int i=0;i<VIEWLENGTH;i++){
			if(id==i){
				linears[i].setBackgroundColor(Color.parseColor("#000000"));
				images[i].setImageResource(imagKs[i]);
				texts[i].setTextColor(Color.parseColor("#ffffff"));
			}else{
				linears[i].setBackgroundColor(Color.parseColor("#272727"));
				images[i].setImageResource(imagGs[i]);
				texts[i].setTextColor(Color.parseColor("#5E5E5E"));
			}
		}
	}
	//设置双击back键退出
	@Override
	public void onBackPressed() {
		if(flag==true){
			super.onBackPressed();
		}else{
			HomeActivity.this.toastS("双击退出");
			flag = true
					;
			new Thread(){
				public void run(){
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					flag = false;
				}
			}.start();
		}
	}
	//分类检索的响应事件
	public void moreonClick(View v){
		String moreNameString = ((TextView)v).getText().toString().trim();
		GatherApplication.appli.setMoreName(moreNameString);
		Intent intent = new Intent(this,GatherShowActivity.class);
		intent.putExtra("key", 2);
		startActivity(intent);
	}
	public void logout(View v){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("注意");
		builder.setMessage("您确定取消吗?");
		builder.setPositiveButton("注销", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				HomeActivity.this.finish();
				Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.show();
	}
}

package com.wyl.wb;



import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyl.adapter.MyFragmentPagerAdapter;
import com.wyl.base.BaseActivity;
import com.wyl.base.BaseFragment;
import com.wyl.base.BaseInterface;
import com.wyl.fragment.FindFragment;
import com.wyl.fragment.HomeFragment;
import com.wyl.fragment.MoreFragment;
import com.wyl.fragment.MyFragment;

public class HomeActivity extends BaseActivity implements BaseInterface, OnClickListener{
	//给选项卡的内容一个容器
	private LinearLayout[] linears = new LinearLayout[4];
	private ImageView[] images = new ImageView[4];
	private TextView[] texts= new TextView[4];
	
	//ID一个容器
	private int[] linearIds={R.id.act_home_home,R.id.act_home_find,R.id.act_home_my,R.id.act_home_more};
	private int[] imageIds= {R.id.act_home_home_imageview,R.id.act_home_find_imageview,R.id.act_home_my_imageview,R.id.act_home_more_imageview};
	private int[] textsIds = {R.id.act_home_home_textview,R.id.act_home_find_textview,R.id.act_home_my_textview,R.id.act_home_more_textview};
	
	//选项卡长度
	private static final int VIEWLENGTH = 4;
	//快关图片Id容器
	private int[] imagKs={R.drawable.main_bottom_tab_k1,R.drawable.main_bottom_tab_k2,R.drawable.main_bottom_tab_k3,R.drawable.main_bottom_tab_k4};
	private int[] imagGs = {R.drawable.main_bottom_tab_g1,R.drawable.main_bottom_tab_g2,R.drawable.main_bottom_tab_g3,R.drawable.main_bottom_tab_g4};
	
	//ViewPager
	private ViewPager pager;
	
	//私有化Fragment
	private List<BaseFragment> fragments;
	
	//退出标记
	private boolean flage = false;
	
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
		
	}
  
	@Override
	public void initDatas() {
		// TODO Auto-generated method stub
		fragments = new ArrayList<BaseFragment>();
		fragments.add(new HomeFragment());
		fragments.add(new FindFragment());
		fragments.add(new MyFragment());
		fragments.add(new MoreFragment());
		pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
	}

	@Override
	public void initViewOper() {
		// TODO Auto-generated method stub
		//viewPager的滑动事件
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				updataViews(arg0); 
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.act_home_home:
			updataViews(0);
			pager.setCurrentItem(0);
			break;
		case R.id.act_home_find:
			updataViews(1);
			pager.setCurrentItem(1);
			break;
		case R.id.act_home_my:
			updataViews(2);
			pager.setCurrentItem(2);
			break;
		case R.id.act_home_more:
			updataViews(3);
			pager.setCurrentItem(3);
			break;
		}
	}

	private void updataViews(int id) {
		// TODO Auto-generated method stub
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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(flage==true){
			super.onBackPressed();
		}else{
			HomeActivity.this.toastS("双击退出");
			flage = true
					;
			new Thread(){
				public void run(){
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					flage = false;
				}
			}.start();
		}
	}

}

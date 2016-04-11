package com.wyl.wb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyl.base.BaseActivity;
import com.wyl.base.BaseInterface;


/**
 * 打开应用出现一个3秒的欢迎动画界面 在这三秒钟的时间里，要进行联网验证，获取手机信息（版本号。。），应用数据的加载
 * 
 * @author wyl 监听动画结束，出现图标，文字图片和登陆，注册文本。
 */
public class InitActivity extends BaseActivity implements BaseInterface{
	// 创建三秒的渐变动画，
	private AlphaAnimation mAnimation;
	// 欢迎图片
	private ImageView init_image;
	//飞机，玩帮
	private ImageView fj_image,wb_image;
	//登入注册
	private TextView longin,register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// wvc设计模式 (分层)
		initViews();
		initDatas();
		initViewOper();

	}

	// 实例化ui组件
	public void initViews() {
		// TODO Auto-generated method stub
		// 获取动画 通过动画的管理类
		mAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this,
				R.anim.init_alpha);
		// 得到图片对象
		init_image = imageById(R.id.act_main_image_1);
		fj_image = imageById(R.id.act_name_icon_Image);
		wb_image = imageById(R.id.act_name_wb_Image);
		longin = tvById(R.id.act_name_register_textview);
		register = tvById(R.id.act_name_longin_textview);

	}

	// 数据操作
	public void initDatas() {
		// TODO Auto-generated method stub

	}

	// 实现业务
	public void initViewOper() {
		// TODO Auto-generated method stub
		//创建一个渐变动画
		AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
		//设置动画播放的时间
		alpha.setDuration(3000);
		fj_image.startAnimation(alpha);
		wb_image.startAnimation(alpha);
		longin.startAnimation(alpha);
		register.startAnimation(alpha);
		
		init_image.startAnimation(mAnimation);
		mAnimation.setAnimationListener(new AnimationListener() {
			// 动画开始
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			// 动画重复
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			// 动画结束
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
					// TODO 登陆跳转
				//注册的点击事件
				longin.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//跳转到注册页面
						Intent intent = new Intent(InitActivity.this,Register.class);
						startActivity(intent);
						
						//淡入淡出activity特效
						overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
						InitActivity.this.finish();
					}
				});
				
				register.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//跳转到登陆界面
						Intent intent = new Intent(InitActivity.this,Login.class);
						startActivity(intent);
						
						//淡入淡出activity特效
//						overridePendingTransition(R.anim.zoomout,R.anim.zoomin); 
						overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
						InitActivity.this.finish();
					}
				});
				
			}
		});
	}
}

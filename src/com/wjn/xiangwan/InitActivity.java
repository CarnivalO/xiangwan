package com.wjn.xiangwan;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseInterface;
import com.wjn.bean.GatherBean;
import com.wjn.bean.UserBean;


/**
 * 打开应用出现一个3秒的欢迎动画界面 在这三秒钟的时间里，要进行联网验证，获取手机信息（版本号。。），应用数据的加载
 * 
 * @author wjn 监听动画结束，出现图标，文字图片和登陆，注册文本。
 */
public class InitActivity extends BaseActivity implements BaseInterface{
	// 创建动画，
	private AlphaAnimation mAnimation;
	// 欢迎图片
	private ImageView init_image;
	//登陆注册
	private TextView login,register;
	protected UserBean ub;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// mvc设计模式 (分层)
		initViews();
		//展示数据提前获取，判断当前应用版本与服务器是否相同，要求更新
		initDatas();
		initViewOper();
		//不管是否wifi
		BmobUpdateAgent.setUpdateOnlyWifi(false); 
		BmobUpdateAgent.update(this);
	}

	// 实例化ui组件
	public void initViews() {
		// TODO Auto-generated method stub
		// 获取动画 通过动画的管理类
		mAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this,R.anim.init_alpha);
		// 得到图片对象
		init_image = imageById(R.id.act_main_image_1);
		login = tvById(R.id.act_login);
		register = tvById(R.id.act_register);
	}

	// 模拟数据
	public void initDatas() {

	}

	public void initViewOper() {
		
		BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {

	        @Override
	        public void onClick(int status) {
	            switch (status) {
	            case UpdateStatus.Update:
	                Toast.makeText(InitActivity.this, "点击了立即更新按钮" , Toast.LENGTH_SHORT).show();
	                break;
	            case UpdateStatus.NotNow:
	                Toast.makeText(InitActivity.this, "点击了以后再说按钮" , Toast.LENGTH_SHORT).show();
					//跳转到登陆界面
					//Intent intent = new Intent(InitActivity.this,LoginActivity.class);
					ub = BmobUser.getCurrentUser(InitActivity.this,UserBean.class);
					if(ub!=null && ub.getUsername().length()>3){
					//	intent = new Intent(InitActivity.this,HomeActivity.class);
						GatherApplication.appli.setUb(ub);
					}
					//startActivity(intent);
					//InitActivity.this.finish();
	                break;
	            case UpdateStatus.Close://只有在强制更新状态下才会在更新对话框的右上方出现close按钮,如果用户不点击”立即更新“按钮，这时候开发者可做些操作，比如直接退出应用等
	                Toast.makeText(InitActivity.this, "点击了对话框关闭按钮" , Toast.LENGTH_SHORT).show();
	                break;
	            }
	        }
	    });
		
		
		 BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

		        @Override
		        public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
		        	if(updateStatus == UpdateStatus.Yes){
		        	}else{
						//跳转到登陆界面
						//Intent intent = new Intent(InitActivity.this,LoginActivity.class);
						ub = BmobUser.getCurrentUser(InitActivity.this,UserBean.class);
						if(ub!= null && ub.getUsername().length()>3){
						//	intent = new Intent(InitActivity.this,HomeActivity.class);
							GatherApplication.appli.setUb(ub);
						}
						//startActivity(intent);
						//InitActivity.this.finish();
		        	}
		        	//版本有更新
//		            if (updateStatus == UpdateStatus.Yes) {
//		            }else if(updateStatus == UpdateStatus.No){
//		                Toast.makeText(InitActivity.this, "版本无更新", Toast.LENGTH_SHORT).show();
//		            }else if(updateStatus==UpdateStatus.EmptyField){//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
//		                Toast.makeText(InitActivity.this, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
//		            }else if(updateStatus==UpdateStatus.IGNORED){
//		                Toast.makeText(InitActivity.this, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
//		            }else if(updateStatus==UpdateStatus.ErrorSizeFormat){
//		                Toast.makeText(InitActivity.this, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
//		            }else if(updateStatus==UpdateStatus.TimeOut){
//		                Toast.makeText(InitActivity.this, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
//		            }
		        }
		    });
		//创建动画
		AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
		//设置动画播放的时间
		alpha.setDuration(3000);
		login.startAnimation(alpha);
		register.startAnimation(alpha);
		
		init_image.startAnimation(mAnimation);
		mAnimation.setAnimationListener(new AnimationListener() {
			// 动画开始
			@Override
			public void onAnimationStart(Animation animation) {
				//获取主页数据
				BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
				//根据score字段降序显示数据
				query.order("-createdAt");
				//更新限制条数
				query.setLimit(10);
				query.findObjects(InitActivity.this, new FindListener<GatherBean>() {
					
					@Override
					public void onSuccess(List<GatherBean> arg0) {
						GatherApplication.appli.setGathers(arg0);
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						
					}
				});
			}

			// 动画重复
			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			// 动画结束
			@Override
			public void onAnimationEnd(Animation animation) {
				// 登陆跳转
				//注册的点击事件
				login.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//跳转到注册页面
						Intent intent = new Intent(InitActivity.this,LoginActivity.class);
						startActivity(intent);
						
						//淡入淡出activity特效
						overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
					}
				});
				register.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//跳转到注册页面
						Intent intent = new Intent(InitActivity.this,RegisterActivity.class);
						startActivity(intent);
						
						//淡入淡出activity特效
						overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
					}
				});
				
			}
		});
	}
}

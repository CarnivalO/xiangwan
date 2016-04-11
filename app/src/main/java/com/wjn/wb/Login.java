package com.wyl.wb;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import com.wyl.application.GatherApplication;
import com.wyl.base.BaseActivity;
import com.wyl.base.BaseInterface;
import com.wyl.bean.UserBean;
import com.wyl.utils.NetUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Login extends BaseActivity implements BaseInterface {
	// 定义出登陆界面需要操作的组件
	// 注册，登陆，用户名，密码
	private TextView register;
	private EditText name, password;
	private Button login;
	// 定义一个UserBean
	private UserBean user;
	private ImageView back;
	// 标记联网状态
	private Boolean isnet;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_login);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		user = GatherApplication.appli.getUser();
		if (user != null) {
			String strname = user.getUsername();
			String strpassword = user.getUb_password();
			if (strname != null) {
				name.setText(strname);
			}
			if (strpassword != null) {
				password.setText(strpassword);
			}
		}
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		// 初始化组件
		register = tvById(R.id.act_login_reaister_textview);
		name = etById(R.id.act_longin_deittext_name);
		password = etById(R.id.act_longin_edittext_password);
		login = butById(R.id.act_login_login_button);
		back = imageById(R.id.act_login_back_imageview);
		

	}
	
	@Override
	public void initDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViewOper() {
		// TODO Auto-generated method stub
		
		
		// 跳转到注册
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this, Register.class);
				startActivity(intent);
			}
		});
		
		//点击登陆
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isnet = NetUtils.isNet(Login.this);
				if (isnet) {
					Log.i("wyl", "ok");
					String strusername = name.getText().toString().trim();
					String strpassword = password.getText().toString().trim();
					if(TextUtils.isEmpty(strusername)&&TextUtils.isEmpty(strpassword)){
						Login.this.toastS("用户名/密码不能为空");
						return;
					}
					if(strusername.length()<6&&strpassword.length()<6){
						Login.this.toastS("用户名/密码过短~");
						return;
					}
//					UserBean user = new UserBean();
//					user.setUsername(strusername);
//					user.setPassword(strpassword);
					final ProgressDialog dialog = new ProgressDialog(Login.this);
					dialog.setMessage("正在登陆，请稍后");
					dialog.show();
					/**
					 * 因为使用了邮箱登录 手机登录所有不能在使用yp登录
					 */
					BmobUser.loginByAccount(Login.this, strusername, strpassword, new LogInListener<UserBean>() {
						@Override
						public void done(UserBean arg0, BmobException arg1) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							if(arg0!=null){
								Log.i("wyl", "登录成功");
								//保持user的实时性
								GatherApplication.appli.setUser(arg0);
								Intent intent = new Intent(Login.this,HomeActivity.class);
								startActivity(intent);
								Login.this.finish();
							}else if(arg1!=null){
								Log.i("wyl", arg1+"");
								Login.this.toastS("登录失败，请检查用户名和密码"+arg1);
							}
						}
					});
				}else{
					Login.this.toastS("当前网络不可用，请检查");
				}
			}
		});
		
		// 返回
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 还回
				Intent intent = new Intent(Login.this, InitActivity.class);
				startActivity(intent);
				// 淡入淡出activity特效
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
				Login.this.finish();
			}
		});

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 判断手机网络
		isnet = NetUtils.isNet(Login.this);
		if (!isnet) {
			Login.this.toastS("当前网络不可用，请检查");
		}
	}



}

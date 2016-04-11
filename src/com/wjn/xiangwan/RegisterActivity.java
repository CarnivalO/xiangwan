package com.wjn.xiangwan;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseInterface;
import com.wjn.bean.UserBean;
import com.wjn.utils.NetUtils;
import com.wjn.xiangwan.R;

/**
 * 注册界面
 * 
 * @author wangjianian 倒计时 并获取验证码 注册时显示dialog提示注册的耗时操作
 *         注册成功后还回登陆界面，同时关闭dialog和注册界面，并回显数据
 * 
 */
public class RegisterActivity extends BaseActivity implements BaseInterface {
	private EditText username, password, password2, phone, code;
	private Button register;
	private TextView code_tv;
	private ImageView back;
	// 标记联网状态
	private Boolean isnet;

	// 获取验证码
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.act_register);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		// 实例化组件
		username = etById(R.id.act_register_username);
		password = etById(R.id.act_register_password);
		password2 = etById(R.id.act_register_password2);
		register = butById(R.id.act_register_button);
		code_tv = tvById(R.id.act_regidter_code_text);
		phone = etById(R.id.act_register_phone);
		code = etById(R.id.act_register_code_edit);
		back = imageById(R.id.act_register_back);
	
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initViewOper() {
		// 注册点击事件
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击注册时判断手机网络
				isnet = NetUtils.isNet(RegisterActivity.this);
				if (isnet) {
					Log.i("wjn", "ok");
					// 当点击注册后，把组件中的值拿出进行判断
					String strName = username.getText().toString().trim();
					final String strPassword = password.getText().toString().trim();
					String strPassword2 = password2.getText().toString().trim();
					String strPhone = phone.getText().toString().trim();
					String strCode = code.getText().toString().trim();
					// 对用户输入进行判断
					if (!(strPassword.equals(strPassword2))) {
						RegisterActivity.this.toastS("密码不一致");
						return;
					}
		
					if (username.length() < 6) {
						RegisterActivity.this.toastS("用户名需大于6位");
						return;
					}
					if (strPhone.length() != 11) {
						RegisterActivity.this.toastS("手机号输入错误");
						return;
					}
					if (strCode == null) {
						RegisterActivity.this.toastS("手机还没有验证");
						return;
					}

					// 实例化一个BuserBean对象
					final UserBean user = new UserBean();
					user.setUsername(strName);
					user.setPassword(strPassword);
					user.setMobilePhoneNumber(strPhone);
					user.setEmail(strName);

					final ProgressDialog diglog = new ProgressDialog(RegisterActivity.this);
					diglog.show();

					// 短信验证验证码
					BmobSMS.verifySmsCode(RegisterActivity.this, strPhone, strCode,
							new VerifySMSCodeListener() {

								@Override
								public void done(BmobException ex) {
									// TODO Auto-generated method stub
									if (ex == null) {// 短信验证码验证成功
										Log.i("wjn", "验证通过");
										// 设置服务器端手机验证为true
										user.setMobilePhoneNumberVerified(true);
										// 账号注册
										user.signUp(RegisterActivity.this,new SaveListener() {
													// 注册成功
													@Override
													public void onSuccess() {
														// 销毁diglog
														UserBean ub = BmobUser.getCurrentUser(RegisterActivity.this,UserBean.class);
														ub.setUb_password(strPassword);
														GatherApplication.appli.setUb(ub);
														diglog.dismiss();
														//返回登陆
														Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
														startActivity(intent);
														RegisterActivity.this.finish();
													}

													// 注册失败
													@Override
													public void onFailure(int arg0,String arg1) {
														// 销毁diglog
														diglog.dismiss();
														RegisterActivity.this.finish();
													}
												});
									} else {// 验证失败
										Log.i("wjn","验证失败：code =" + ex.getErrorCode()+ ",msg = "+ ex.getLocalizedMessage());
										RegisterActivity.this.toastS("验证失败");
										// 销毁diglog
										
									}
								}
							});
				}else{
					RegisterActivity.this.toastS("当前网络不可用，请检查");
				}
			}
		});
		// 获取验证码
		code_tv.setOnClickListener(new OnClickListener() {
			// 判断是否输入手机号，手机号是否正确

			public void onClick(View v) {

				String strPhone = phone.getText().toString().trim();
				BmobSMS.requestSMSCode(RegisterActivity.this, strPhone, "享玩",
						new RequestSMSCodeListener() {
							@Override
							public void done(Integer arg0, BmobException arg1) {
								if (arg1 == null) {// 验证码发送成功
									// 开一个异步多线程，持续的进行更改主线程中的组件
									AsyncTask<Void, String, Void> task = new AsyncTask<Void, String, Void>() {
										// 在子线程中写一个for循环并设置线程等待，每循环一次等待一秒
										@Override
										protected Void doInBackground(
												Void... params) {
											// TODO Auto-generated method stub
											for (int i = 60; i > 0; i--) {
												// 持续的调用主线程中的onProgressUpdate方法，传递参数
												publishProgress(i + "s");
												try {
													// 线程等待
													Thread.sleep(1000);
												} catch (InterruptedException e) {
													RegisterActivity.this.toastS("短信接收异常~");
												}
											}
											return null;
										}

										// 开启子线程前的准备方法-ui线程
										@Override
										protected void onPreExecute() {
											// TODO Auto-generated method stub
											super.onPreExecute();
											code_tv.setTextColor(Color
													.parseColor("#29AAE7"));
											code_tv.setClickable(false);
										}

										// 子线程结束后执行的方法-ui线程
										@Override
										protected void onPostExecute(Void result) {
											// TODO Auto-generated method stub
											super.onPostExecute(result);
											code_tv.setTextColor(Color
													.parseColor("3ECEF0"));
											code_tv.setClickable(true);
										}

										// 主线程接收子线程消息，和操作主线程的方法
										@Override
										protected void onProgressUpdate(
												String... values) {
											// TODO Auto-generated method stub
											super.onProgressUpdate(values);
											code_tv.setText(values[0]);
										}

									};
									task.execute();

								} else {
									Log.i("wjn", "获取验证码失败，请重新验证~"+arg1);
								}
							}
						});

			}
		});
		// 返回
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this, InitActivity.class);
				startActivity(intent);
			}
		});

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 重新显示和第一次显示时判断手机网络
		isnet = NetUtils.isNet(RegisterActivity.this);
		if (!isnet) {
			RegisterActivity.this.toastS("当前网络不可用，请检查");
		}
	}
}

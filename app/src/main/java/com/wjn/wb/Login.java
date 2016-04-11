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
	// �������½������Ҫ���������
	// ע�ᣬ��½���û���������
	private TextView register;
	private EditText name, password;
	private Button login;
	// ����һ��UserBean
	private UserBean user;
	private ImageView back;
	// �������״̬
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
		// ��ʼ�����
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
		
		
		// ��ת��ע��
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this, Register.class);
				startActivity(intent);
			}
		});
		
		//�����½
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
						Login.this.toastS("�û���/���벻��Ϊ��");
						return;
					}
					if(strusername.length()<6&&strpassword.length()<6){
						Login.this.toastS("�û���/�������~");
						return;
					}
//					UserBean user = new UserBean();
//					user.setUsername(strusername);
//					user.setPassword(strpassword);
					final ProgressDialog dialog = new ProgressDialog(Login.this);
					dialog.setMessage("���ڵ�½�����Ժ�");
					dialog.show();
					/**
					 * ��Ϊʹ���������¼ �ֻ���¼���в�����ʹ��yp��¼
					 */
					BmobUser.loginByAccount(Login.this, strusername, strpassword, new LogInListener<UserBean>() {
						@Override
						public void done(UserBean arg0, BmobException arg1) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							if(arg0!=null){
								Log.i("wyl", "��¼�ɹ�");
								//����user��ʵʱ��
								GatherApplication.appli.setUser(arg0);
								Intent intent = new Intent(Login.this,HomeActivity.class);
								startActivity(intent);
								Login.this.finish();
							}else if(arg1!=null){
								Log.i("wyl", arg1+"");
								Login.this.toastS("��¼ʧ�ܣ������û���������"+arg1);
							}
						}
					});
				}else{
					Login.this.toastS("��ǰ���粻���ã�����");
				}
			}
		});
		
		// ����
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ����
				Intent intent = new Intent(Login.this, InitActivity.class);
				startActivity(intent);
				// ���뵭��activity��Ч
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
		// �ж��ֻ�����
		isnet = NetUtils.isNet(Login.this);
		if (!isnet) {
			Login.this.toastS("��ǰ���粻���ã�����");
		}
	}



}

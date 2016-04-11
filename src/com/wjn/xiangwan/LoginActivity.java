package com.wjn.xiangwan;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseInterface;
import com.wjn.bean.MyBmobInstallation;
import com.wjn.bean.UserBean;
import com.wjn.utils.NetUtils;

public class LoginActivity extends BaseActivity implements BaseInterface {
	// �������½������Ҫ���������
	// ע�ᣬ��½���û���������
	private TextView register;
	private EditText name, password;
	private Button login;
	// ����һ��UserBean
	private UserBean user;
	private ImageView back,clearusername,clearpd;
	// �������״̬
	private Boolean isnet;
	private CheckBox rem_pw,auto_login;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_login);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	protected void onStart() {
		super.onStart();
		user = GatherApplication.appli.getUb();
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
		// ��ʼ�����
		register = tvById(R.id.act_login_reaister_textview);
		name = etById(R.id.act_longin_edittext_name);
		password = etById(R.id.act_longin_edittext_password);
		login = butById(R.id.act_login_login_button);
		back = imageById(R.id.act_login_back_imageview);
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);  
	    auto_login = (CheckBox) findViewById(R.id.cb_auto);  
	    clearusername = imageById(R.id.act_login_clearusername);
	    clearpd = imageById(R.id.act_login_clearpd);
	    

	}
	
	@Override
	public void initDatas() {

	}

	@Override
	public void initViewOper() {
		name.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (name.hasFocus() == true) { 
				clearusername.setVisibility(View.VISIBLE);
				}
				if (name.hasFocus() == false) { 
				clearusername.setVisibility(View.INVISIBLE);
				}
			}
		});
		password.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (password.hasFocus() == true) { 
				clearpd.setVisibility(View.VISIBLE);
				}
				if (password.hasFocus() == false) { 
				clearpd.setVisibility(View.INVISIBLE);
				}
			}
		});
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(auto_login.isChecked()){
					rem_pw.setChecked(true);
				}
			}
		});
	
		clearusername.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				name.setText("");
			}
		});
		clearpd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				password.setText("");
			}
		});
		// ��ת��ע��
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
		
		//�����½
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isnet = NetUtils.isNet(LoginActivity.this);
				if (isnet) {
					Log.i("wjn", "ok");
					String strusername = name.getText().toString().trim();
					String strpassword = password.getText().toString().trim();
					if(TextUtils.isEmpty(strusername)&&TextUtils.isEmpty(strpassword)){
						LoginActivity.this.toastS("�û���/���벻��Ϊ��");
						return;
					}
					if(strusername.length()<6&&strpassword.length()<6){
						LoginActivity.this.toastS("�û���/�������~");
						return;
					}
//					UserBean user = new UserBean();
//					user.setUsername(strusername);
//					user.setPassword(strpassword);
					final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
					dialog.setMessage("���ڵ�½�����Ժ�");
					dialog.show();
					/**
					 * ��Ϊʹ���������¼ �ֻ���¼���в�����ʹ��yp��¼
					 */
					BmobUser.loginByAccount(LoginActivity.this, strusername, strpassword, new LogInListener<UserBean>() {
						@Override
						public void done(final UserBean user, BmobException e) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							if(user!=null){
								Log.i("wjn", "��¼�ɹ�");
								//����user��ʵʱ��
								GatherApplication.appli.setUb(user);
								Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
								startActivity(intent);
								BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
								query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(LoginActivity.this));
								query.findObjects(LoginActivity.this, new FindListener<MyBmobInstallation>() {

								    @Override
								    public void onSuccess(List<MyBmobInstallation> object) {
								        // TODO Auto-generated method stub
								        if(object.size() > 0){
								            MyBmobInstallation mbi = object.get(0);
								            mbi.setUid(user.getUsername());
								            mbi.update(LoginActivity.this,new UpdateListener() {

								                @Override
								                public void onSuccess() {
								                    // TODO Auto-generated method stub
								                    Log.i("Log","�豸��Ϣ���³ɹ�");
								                }

								                @Override
								                public void onFailure(int code, String msg) {
								                    // TODO Auto-generated method stub
								                    Log.i("Log","�豸��Ϣ����ʧ��:"+msg);
								                }
								            });
								        }else{
								        }
								    }

								    @Override
								    public void onError(int code, String msg) {
								        // TODO Auto-generated method stub
								    }
								});
								LoginActivity.this.finish();
							}else if(e!=null){
								Log.i("wjn", e+"");
								LoginActivity.this.toastS("��¼ʧ�ܣ������û���������");
							}
						}
					});
				}else{
					LoginActivity.this.toastS("��ǰ���粻���ã�����");
				}
			}
		});
		
		// ����
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ����
				Intent intent = new Intent(LoginActivity.this, InitActivity.class);
				startActivity(intent);
				// ���뵭��activity��Ч
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
				LoginActivity.this.finish();
			}
		});

	}
	@Override
	protected void onResume() {
		super.onResume();
		// �ж��ֻ�����
		isnet = NetUtils.isNet(LoginActivity.this);
		if (!isnet) {
			LoginActivity.this.toastS("��ǰ���粻���ã�����");
		}
	}



}

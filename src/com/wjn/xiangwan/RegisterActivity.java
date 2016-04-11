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
 * ע�����
 * 
 * @author wangjianian ����ʱ ����ȡ��֤�� ע��ʱ��ʾdialog��ʾע��ĺ�ʱ����
 *         ע��ɹ��󻹻ص�½���棬ͬʱ�ر�dialog��ע����棬����������
 * 
 */
public class RegisterActivity extends BaseActivity implements BaseInterface {
	private EditText username, password, password2, phone, code;
	private Button register;
	private TextView code_tv;
	private ImageView back;
	// �������״̬
	private Boolean isnet;

	// ��ȡ��֤��
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
		// ʵ�������
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
		// ע�����¼�
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ���ע��ʱ�ж��ֻ�����
				isnet = NetUtils.isNet(RegisterActivity.this);
				if (isnet) {
					Log.i("wjn", "ok");
					// �����ע��󣬰�����е�ֵ�ó������ж�
					String strName = username.getText().toString().trim();
					final String strPassword = password.getText().toString().trim();
					String strPassword2 = password2.getText().toString().trim();
					String strPhone = phone.getText().toString().trim();
					String strCode = code.getText().toString().trim();
					// ���û���������ж�
					if (!(strPassword.equals(strPassword2))) {
						RegisterActivity.this.toastS("���벻һ��");
						return;
					}
		
					if (username.length() < 6) {
						RegisterActivity.this.toastS("�û��������6λ");
						return;
					}
					if (strPhone.length() != 11) {
						RegisterActivity.this.toastS("�ֻ����������");
						return;
					}
					if (strCode == null) {
						RegisterActivity.this.toastS("�ֻ���û����֤");
						return;
					}

					// ʵ����һ��BuserBean����
					final UserBean user = new UserBean();
					user.setUsername(strName);
					user.setPassword(strPassword);
					user.setMobilePhoneNumber(strPhone);
					user.setEmail(strName);

					final ProgressDialog diglog = new ProgressDialog(RegisterActivity.this);
					diglog.show();

					// ������֤��֤��
					BmobSMS.verifySmsCode(RegisterActivity.this, strPhone, strCode,
							new VerifySMSCodeListener() {

								@Override
								public void done(BmobException ex) {
									// TODO Auto-generated method stub
									if (ex == null) {// ������֤����֤�ɹ�
										Log.i("wjn", "��֤ͨ��");
										// ���÷��������ֻ���֤Ϊtrue
										user.setMobilePhoneNumberVerified(true);
										// �˺�ע��
										user.signUp(RegisterActivity.this,new SaveListener() {
													// ע��ɹ�
													@Override
													public void onSuccess() {
														// ����diglog
														UserBean ub = BmobUser.getCurrentUser(RegisterActivity.this,UserBean.class);
														ub.setUb_password(strPassword);
														GatherApplication.appli.setUb(ub);
														diglog.dismiss();
														//���ص�½
														Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
														startActivity(intent);
														RegisterActivity.this.finish();
													}

													// ע��ʧ��
													@Override
													public void onFailure(int arg0,String arg1) {
														// ����diglog
														diglog.dismiss();
														RegisterActivity.this.finish();
													}
												});
									} else {// ��֤ʧ��
										Log.i("wjn","��֤ʧ�ܣ�code =" + ex.getErrorCode()+ ",msg = "+ ex.getLocalizedMessage());
										RegisterActivity.this.toastS("��֤ʧ��");
										// ����diglog
										
									}
								}
							});
				}else{
					RegisterActivity.this.toastS("��ǰ���粻���ã�����");
				}
			}
		});
		// ��ȡ��֤��
		code_tv.setOnClickListener(new OnClickListener() {
			// �ж��Ƿ������ֻ��ţ��ֻ����Ƿ���ȷ

			public void onClick(View v) {

				String strPhone = phone.getText().toString().trim();
				BmobSMS.requestSMSCode(RegisterActivity.this, strPhone, "����",
						new RequestSMSCodeListener() {
							@Override
							public void done(Integer arg0, BmobException arg1) {
								if (arg1 == null) {// ��֤�뷢�ͳɹ�
									// ��һ���첽���̣߳������Ľ��и������߳��е����
									AsyncTask<Void, String, Void> task = new AsyncTask<Void, String, Void>() {
										// �����߳���дһ��forѭ���������̵߳ȴ���ÿѭ��һ�εȴ�һ��
										@Override
										protected Void doInBackground(
												Void... params) {
											// TODO Auto-generated method stub
											for (int i = 60; i > 0; i--) {
												// �����ĵ������߳��е�onProgressUpdate���������ݲ���
												publishProgress(i + "s");
												try {
													// �̵߳ȴ�
													Thread.sleep(1000);
												} catch (InterruptedException e) {
													RegisterActivity.this.toastS("���Ž����쳣~");
												}
											}
											return null;
										}

										// �������߳�ǰ��׼������-ui�߳�
										@Override
										protected void onPreExecute() {
											// TODO Auto-generated method stub
											super.onPreExecute();
											code_tv.setTextColor(Color
													.parseColor("#29AAE7"));
											code_tv.setClickable(false);
										}

										// ���߳̽�����ִ�еķ���-ui�߳�
										@Override
										protected void onPostExecute(Void result) {
											// TODO Auto-generated method stub
											super.onPostExecute(result);
											code_tv.setTextColor(Color
													.parseColor("3ECEF0"));
											code_tv.setClickable(true);
										}

										// ���߳̽������߳���Ϣ���Ͳ������̵߳ķ���
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
									Log.i("wjn", "��ȡ��֤��ʧ�ܣ���������֤~"+arg1);
								}
							}
						});

			}
		});
		// ����
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
		// ������ʾ�͵�һ����ʾʱ�ж��ֻ�����
		isnet = NetUtils.isNet(RegisterActivity.this);
		if (!isnet) {
			RegisterActivity.this.toastS("��ǰ���粻���ã�����");
		}
	}
}

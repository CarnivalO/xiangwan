package com.wyl.wb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.wyl.application.GatherApplication;
import com.wyl.base.BaseActivity;
import com.wyl.base.BaseInterface;
import com.wyl.bean.UserBean;
import com.wyl.utils.NetUtils;

/**
 * ע�����
 * 
 * @author wangyinliang ����ʱ ����ȡ��֤�� ע��ʱ��ʾdialog��ʾע��ĺ�ʱ����
 *         ע��ɹ��󻹻ص�½���棬ͬʱ�ر�dialog��ע����棬����������
 * 
 */
public class Register extends BaseActivity implements BaseInterface {
	private EditText name, password, qrpassword, phone, code;
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
		name = etById(R.id.act_register_name_edit);
		password = etById(R.id.act_register_password_edit);
		qrpassword = etById(R.id.act_register_qrpassword_edit);
		register = butById(R.id.act_register_login_button);
		code_tv = tvById(R.id.act_regidter_code_text);
		phone = etById(R.id.act_register_phone_edit);
		code = etById(R.id.act_register_code_edit);
		back = imageById(R.id.act_fegister_back_longin);
	
	}

	@Override
	public void initDatas() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViewOper() {
		// TODO Auto-generated method stub
		// ע�����¼�
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ���ע��ʱ�ж��ֻ�����
				isnet = NetUtils.isNet(Register.this);
				if (isnet) {
					Log.i("wyl", "ok");
					// �����ע��󣬰�����е�ֵ�ó������ж�
					String strName = name.getText().toString().trim();
					final String strPassWord = password.getText().toString().trim();
					String strQrPassWord = qrpassword.getText().toString().trim();
					String strCode_tv = code_tv.getText().toString().trim();
					String strPhone = phone.getText().toString().trim();
					String strCode = code.getText().toString().trim();
					// �ж�
					if (TextUtils.isEmpty(strName)) {
						Register.this.toastS("�������û���~");
						return;
					}
					if (TextUtils.isEmpty(strPassWord)) {
						Register.this.toastS("����������~");
						return;
					}
					if (TextUtils.isEmpty(strQrPassWord)) {
						Register.this.toastS("��ȷ������~");
						return;
					}
					if (TextUtils.isEmpty(strPhone)) {
						Register.this.toastS("�������ֻ���~");
						return;
					}
					if (name.length() < 6) {
						Register.this.toastS("��������û�������~");
						return;
					}
					if (strPhone.length() != 11) {
						Register.this.toastS("�ֻ�����������~");
						return;
					}
					if (strCode == null) {
						Register.this.toastS("�ֻ���û����֤~");
						return;
					}

					// ʵ����һ��BuserBean����
					final UserBean user = new UserBean();
					user.setUsername(strName);
					user.setPassword(strPassWord);
					user.setMobilePhoneNumber(strPhone);
					user.setEmail(strName);

					final ProgressDialog diglog = new ProgressDialog(Register.this);
					diglog.show();

					// ������֤��֤��
					BmobSMS.verifySmsCode(Register.this, strPhone, strCode,
							new VerifySMSCodeListener() {

								@Override
								public void done(BmobException ex) {
									// TODO Auto-generated method stub
									if (ex == null) {// ������֤����֤�ɹ�
										Log.i("wyl", "��֤ͨ��");
										// ���÷��������ֻ���֤Ϊtrue
										user.setMobilePhoneNumberVerified(true);
										// �˺�ע��
										user.signUp(Register.this,
												new SaveListener() {
													// ע��ɹ�
													@Override
													public void onSuccess() {
														// TODO Auto-generated
														// method stub
														// ����diglog

														UserBean ub = BmobUser.getCurrentUser(
																		Register.this,
																		UserBean.class);
														ub.setUb_password(strPassWord);
														GatherApplication.appli
																.setUser(ub);
														diglog.dismiss();
														//���ص�½
														Intent intent = new Intent(Register.this, Login.class);
														startActivity(intent);
														// ���뵭��activity��Ч
														overridePendingTransition(android.R.anim.fade_in,
																android.R.anim.fade_out);
														Register.this.finish();
													}

													// ע��ʧ��
													@Override
													public void onFailure(int arg0,
															String arg1) {
														// TODO Auto-generated
														// method stub
														// ����diglog
														diglog.dismiss();
														Register.this.finish();

													}
												});
									} else {// ��֤ʧ��
										Log.i("wyl",
												"��֤ʧ�ܣ�code =" + ex.getErrorCode()
														+ ",msg = "
														+ ex.getLocalizedMessage());
										Register.this.toastS("��֤ʧ��");
										// ����diglog
										
									}
								}
							});
				}else{
					Register.this.toastS("��ǰ���粻���ã�����");
				}
			}
		});
		// ��ȡ��֤��
		code_tv.setOnClickListener(new OnClickListener() {

			// �ж��Ƿ������ֻ��ţ��ֻ����Ƿ���ȷ
			String strPhone1 = phone.getText().toString().trim();

			public void onClick(View v) {

				String strPhone = phone.getText().toString().trim();
				// TODO Auto-generated method stub
				BmobSMS.requestSMSCode(Register.this, strPhone, "�������",
						new RequestSMSCodeListener() {
							@Override
							public void done(Integer arg0, BmobException arg1) {
								// TODO Auto-generated method stub
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
													// TODO Auto-generated catch
													// block
													// e.printStackTrace();
													Register.this
															.toastS("���Ž����쳣~");
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
									Log.i("wyl", "��ȡ��֤��ʧ�ܣ���������֤~"+arg1);
								}
							}
						});

			}
		});
		// ����
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ����
				Intent intent = new Intent(Register.this, InitActivity.class);
				startActivity(intent);

				// ���뵭��activity��Ч
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ������ʾ�͵�һ����ʾʱ�ж��ֻ�����
		isnet = NetUtils.isNet(Register.this);
		if (!isnet) {
			Register.this.toastS("��ǰ���粻���ã�����");
		}
	}
}

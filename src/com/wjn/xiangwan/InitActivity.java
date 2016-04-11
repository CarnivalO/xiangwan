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
 * ��Ӧ�ó���һ��3��Ļ�ӭ�������� ���������ӵ�ʱ���Ҫ����������֤����ȡ�ֻ���Ϣ���汾�š�������Ӧ�����ݵļ���
 * 
 * @author wjn ������������������ͼ�꣬����ͼƬ�͵�½��ע���ı���
 */
public class InitActivity extends BaseActivity implements BaseInterface{
	// ����������
	private AlphaAnimation mAnimation;
	// ��ӭͼƬ
	private ImageView init_image;
	//��½ע��
	private TextView login,register;
	protected UserBean ub;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// mvc���ģʽ (�ֲ�)
		initViews();
		//չʾ������ǰ��ȡ���жϵ�ǰӦ�ð汾��������Ƿ���ͬ��Ҫ�����
		initDatas();
		initViewOper();
		//�����Ƿ�wifi
		BmobUpdateAgent.setUpdateOnlyWifi(false); 
		BmobUpdateAgent.update(this);
	}

	// ʵ����ui���
	public void initViews() {
		// TODO Auto-generated method stub
		// ��ȡ���� ͨ�������Ĺ�����
		mAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this,R.anim.init_alpha);
		// �õ�ͼƬ����
		init_image = imageById(R.id.act_main_image_1);
		login = tvById(R.id.act_login);
		register = tvById(R.id.act_register);
	}

	// ģ������
	public void initDatas() {

	}

	public void initViewOper() {
		
		BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {

	        @Override
	        public void onClick(int status) {
	            switch (status) {
	            case UpdateStatus.Update:
	                Toast.makeText(InitActivity.this, "������������°�ť" , Toast.LENGTH_SHORT).show();
	                break;
	            case UpdateStatus.NotNow:
	                Toast.makeText(InitActivity.this, "������Ժ���˵��ť" , Toast.LENGTH_SHORT).show();
					//��ת����½����
					//Intent intent = new Intent(InitActivity.this,LoginActivity.class);
					ub = BmobUser.getCurrentUser(InitActivity.this,UserBean.class);
					if(ub!=null && ub.getUsername().length()>3){
					//	intent = new Intent(InitActivity.this,HomeActivity.class);
						GatherApplication.appli.setUb(ub);
					}
					//startActivity(intent);
					//InitActivity.this.finish();
	                break;
	            case UpdateStatus.Close://ֻ����ǿ�Ƹ���״̬�²Ż��ڸ��¶Ի�������Ϸ�����close��ť,����û���������������¡���ť����ʱ�򿪷��߿���Щ����������ֱ���˳�Ӧ�õ�
	                Toast.makeText(InitActivity.this, "����˶Ի���رհ�ť" , Toast.LENGTH_SHORT).show();
	                break;
	            }
	        }
	    });
		
		
		 BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

		        @Override
		        public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
		        	if(updateStatus == UpdateStatus.Yes){
		        	}else{
						//��ת����½����
						//Intent intent = new Intent(InitActivity.this,LoginActivity.class);
						ub = BmobUser.getCurrentUser(InitActivity.this,UserBean.class);
						if(ub!= null && ub.getUsername().length()>3){
						//	intent = new Intent(InitActivity.this,HomeActivity.class);
							GatherApplication.appli.setUb(ub);
						}
						//startActivity(intent);
						//InitActivity.this.finish();
		        	}
		        	//�汾�и���
//		            if (updateStatus == UpdateStatus.Yes) {
//		            }else if(updateStatus == UpdateStatus.No){
//		                Toast.makeText(InitActivity.this, "�汾�޸���", Toast.LENGTH_SHORT).show();
//		            }else if(updateStatus==UpdateStatus.EmptyField){//����ʾֻ�����ѿ����߹�ע��Щ��������Գɹ���������û���ʾ
//		                Toast.makeText(InitActivity.this, "������AppVersion��ı����1��target_size���ļ���С���Ƿ���д��2��path����android_url���߱�������һ�", Toast.LENGTH_SHORT).show();
//		            }else if(updateStatus==UpdateStatus.IGNORED){
//		                Toast.makeText(InitActivity.this, "�ð汾�ѱ����Ը���", Toast.LENGTH_SHORT).show();
//		            }else if(updateStatus==UpdateStatus.ErrorSizeFormat){
//		                Toast.makeText(InitActivity.this, "����target_size��д�ĸ�ʽ����ʹ��file.length()������ȡapk��С��", Toast.LENGTH_SHORT).show();
//		            }else if(updateStatus==UpdateStatus.TimeOut){
//		                Toast.makeText(InitActivity.this, "��ѯ������ѯ��ʱ", Toast.LENGTH_SHORT).show();
//		            }
		        }
		    });
		//��������
		AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
		//���ö������ŵ�ʱ��
		alpha.setDuration(3000);
		login.startAnimation(alpha);
		register.startAnimation(alpha);
		
		init_image.startAnimation(mAnimation);
		mAnimation.setAnimationListener(new AnimationListener() {
			// ������ʼ
			@Override
			public void onAnimationStart(Animation animation) {
				//��ȡ��ҳ����
				BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
				//����score�ֶν�����ʾ����
				query.order("-createdAt");
				//������������
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

			// �����ظ�
			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			// ��������
			@Override
			public void onAnimationEnd(Animation animation) {
				// ��½��ת
				//ע��ĵ���¼�
				login.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//��ת��ע��ҳ��
						Intent intent = new Intent(InitActivity.this,LoginActivity.class);
						startActivity(intent);
						
						//���뵭��activity��Ч
						overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
					}
				});
				register.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//��ת��ע��ҳ��
						Intent intent = new Intent(InitActivity.this,RegisterActivity.class);
						startActivity(intent);
						
						//���뵭��activity��Ч
						overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
					}
				});
				
			}
		});
	}
}

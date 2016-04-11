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
 * ��Ӧ�ó���һ��3��Ļ�ӭ�������� ���������ӵ�ʱ���Ҫ����������֤����ȡ�ֻ���Ϣ���汾�š�������Ӧ�����ݵļ���
 * 
 * @author wyl ������������������ͼ�꣬����ͼƬ�͵�½��ע���ı���
 */
public class InitActivity extends BaseActivity implements BaseInterface{
	// ��������Ľ��䶯����
	private AlphaAnimation mAnimation;
	// ��ӭͼƬ
	private ImageView init_image;
	//�ɻ������
	private ImageView fj_image,wb_image;
	//����ע��
	private TextView longin,register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// wvc���ģʽ (�ֲ�)
		initViews();
		initDatas();
		initViewOper();

	}

	// ʵ����ui���
	public void initViews() {
		// TODO Auto-generated method stub
		// ��ȡ���� ͨ�������Ĺ�����
		mAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this,
				R.anim.init_alpha);
		// �õ�ͼƬ����
		init_image = imageById(R.id.act_main_image_1);
		fj_image = imageById(R.id.act_name_icon_Image);
		wb_image = imageById(R.id.act_name_wb_Image);
		longin = tvById(R.id.act_name_register_textview);
		register = tvById(R.id.act_name_longin_textview);

	}

	// ���ݲ���
	public void initDatas() {
		// TODO Auto-generated method stub

	}

	// ʵ��ҵ��
	public void initViewOper() {
		// TODO Auto-generated method stub
		//����һ�����䶯��
		AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
		//���ö������ŵ�ʱ��
		alpha.setDuration(3000);
		fj_image.startAnimation(alpha);
		wb_image.startAnimation(alpha);
		longin.startAnimation(alpha);
		register.startAnimation(alpha);
		
		init_image.startAnimation(mAnimation);
		mAnimation.setAnimationListener(new AnimationListener() {
			// ������ʼ
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			// �����ظ�
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			// ��������
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
					// TODO ��½��ת
				//ע��ĵ���¼�
				longin.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//��ת��ע��ҳ��
						Intent intent = new Intent(InitActivity.this,Register.class);
						startActivity(intent);
						
						//���뵭��activity��Ч
						overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
						InitActivity.this.finish();
					}
				});
				
				register.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//��ת����½����
						Intent intent = new Intent(InitActivity.this,Login.class);
						startActivity(intent);
						
						//���뵭��activity��Ч
//						overridePendingTransition(R.anim.zoomout,R.anim.zoomin); 
						overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
						InitActivity.this.finish();
					}
				});
				
			}
		});
	}
}

package com.wjn.xiangwan;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseInterface;
import com.wjn.bean.GatherBean;
import com.wjn.bean.UserBean;
import com.wjn.imageloader.ImageLoaderutils;

public class ItemShowActivity extends BaseActivity implements BaseInterface{
	private GatherBean gather;
	private ImageLoader loader;
	private DisplayImageOptions options;
	private DisplayImageOptions options2;
	private UserBean ub;
	private Builder dialog;
	
	private TextView mBack,mCanyu,mGuanli,gatherTitle,gatherName,gatherIntro,mPingLunNumber,mLoveNumber,mPerNumber,mRMBNumber;
	private ImageView gatherImg,usericon;
	private LinearLayout mPingLun,mGatherRmb;
	//֧���ɹ��󣬸ı����Ӧ�¼�
	private OnClickListener newsOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//�趨�����ת������
			Intent intent = new Intent(ItemShowActivity.this,ChatingActivity.class);
			startActivity(intent);
		}
	};
	@Override
	protected void onStart() {
		super.onStart();
		mPingLunNumber.setText(gather.getPingluns()+"");
		//�жϵ�ǰ�û��Ƿ��Ƿ�����
		if(gather.getGatherUserId().equals(ub.getObjectId())){
			mCanyu.setVisibility(View.INVISIBLE);
			mGuanli.setVisibility(View.VISIBLE);
			mGatherRmb.setClickable(false);
			mCanyu.setClickable(false);
		}else{
		//�жϵ�ǰ��¼�û��Ƿ��Ѿ�����,�Ѹ���Ĳ��õ��
		List<String> payuserids = gather.getPaymentUserId();
		for(int i=0;i < payuserids.size();i++){
			if(payuserids.get(i).equals(ub.getObjectId())){
				mCanyu.setText("�鿴");
				mCanyu.setOnClickListener(newsOnclick);
				mGatherRmb.setClickable(false);
				mRMBNumber.setText("�Ѹ�");
				if(gather.getFlag()!=null&&gather.getFlag()){
					myShowDialog();
				}
				break; 
			}
		}
		}
	}
	private void myShowDialog() {
		// �趨�������Dialog
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��ǰ��Ѿ�����");
		builder.setMessage("���Ƿ����ֳ�");
		builder.setPositiveButton("��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				List<String> startuserids = gather.getStartUserId();
				List<String> startusernames = gather.getStartUserName();
				startuserids.add(ub.getObjectId());
				startusernames.add(ub.getUsername());
				
				gather.setStartUserId(startuserids);
				gather.setStartUserName(startusernames);
				//Ϊ�Bean���id��name
				GatherBean gg = new GatherBean();
				gg.setStartUserId(startuserids);
				gg.setStartUserName(startusernames);
				gg.update(ItemShowActivity.this,gather.getObjectId(),new UpdateListener() {
					
					@Override
					public void onSuccess() {
						
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						
					}
				});
			}
		});
		builder.setNegativeButton("��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.show();
	}
	private OnClickListener RmbOnClick = new OnClickListener() {
		

		@Override
		public void onClick(View v) {
			//����֧��
			dialog = new AlertDialog.Builder(ItemShowActivity.this);
			dialog.setTitle("��ѡ��֧����ʽ");
			dialog.setMessage("��ǰ��۸�Ϊ:"+gather.getGatherRMB()+"��");
			//΢��֧��
			dialog.setPositiveButton("΢��֧��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
						new BmobPay(ItemShowActivity.this).payByWX(Integer.decode(gather.getGatherRMB()),"", new PayListener() {
						
						private String orderId;
						@Override
						public void unknow() {
							ItemShowActivity.this.toastS("��������ԭ����ȷ���Ƿ�֧���ɹ�");
						}
						
						@Override
						public void succeed() {
							ItemShowActivity.this.toastS("֧���ɹ�");
							paySuccessed(orderId);
						}
						//˳��1
						@Override
						public void orderId(String arg0) {
							ItemShowActivity.this.toastS("�����ţ�"+arg0);
							this.orderId = arg0;
						}
						//˳��2
						@Override
						public void fail(int arg0, String arg1) {
							ItemShowActivity.this.toastS("֧��ʧ��"+arg0+"ʧ��ԭ��"+arg1);
						}
					});
				}
			}); 
			//֧����֧��
			dialog.setNegativeButton("֧����", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new BmobPay(ItemShowActivity.this).pay(Integer.decode(gather.getGatherRMB()),"", new PayListener() {
						
						private String orderId;
						@Override
						public void unknow() {
							ItemShowActivity.this.toastS("��������ԭ����ȷ���Ƿ�֧���ɹ�");
						}
						
						@Override
						public void succeed() {
							paySuccessed(orderId);
							
						}
						//˳��1
						@Override
						public void orderId(String arg0) {
							ItemShowActivity.this.toastS("�����ţ�"+arg0);
							this.orderId = arg0;
						}
						//˳��2
						@Override
						public void fail(int arg0, String arg1) {
							ItemShowActivity.this.toastS("֧��ʧ��"+arg0+"ʧ��ԭ��"+arg1);
						}
					});
				}
			});
			dialog.show();
		}
	};
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_item_gathershow);
		initViews();
		initDatas();
		initViewOper();
		
	}
	@Override
	public void initViews() {
		mBack = tvById(R.id.act_item_show_back);
		mCanyu= tvById(R.id.act_item_show_canyu);
		gatherTitle = tvById(R.id.act_item_show_gathertitle);
		gatherName = tvById(R.id.act_item_show_gathername);
		gatherIntro = tvById(R.id.act_item_show_gatherintro);
		mPingLunNumber = tvById(R.id.act_item_show_pinglun_number);
		mLoveNumber = tvById(R.id.act_item_show_loves);
		mPerNumber = tvById(R.id.act_item_show_per);
		mRMBNumber = tvById(R.id.act_item_show_gatherRmb_number);
		mGuanli = tvById(R.id.act_item_show_guanli);
		
		gatherImg = imageById(R.id.act_item_show_gatherimg);
		usericon = imageById(R.id.act_item_show_usericon);
		
		mPingLun = linearById(R.id.act_item_show_pinglun);
		mGatherRmb = linearById(R.id.act_item_show_gatherRmb);
	}

	@Override
	public void initDatas() {
		gather = GatherApplication.appli.getNotifgather();
		if(gather == null)
		gather = GatherApplication.appli.getItem_showgather();
		ub = GatherApplication.appli.getUb();
		//�жϵ�ǰ��Ƿ�ǰ��¼�û�

//		List������ʾ
		String url = null;
		if (gather.getGatherJPG() != null) {
			url = gather.getGatherJPG().getFileUrl(this);
		}
		loader = ImageLoaderutils.getInstance(this);
		options = ImageLoaderutils.getOpt2();
		loader.displayImage(url,gatherImg,options);
		
//		ͷ����ʾ
		String url2 = null;
		if (gather.getGatherIcon() != null) {
			url2 = gather.getGatherIcon().getFileUrl(this);
		}
		loader = ImageLoaderutils.getInstance(this);
		options2= ImageLoaderutils.getOpt();
		loader.displayImage(url2,usericon,options2);
	}
		private void paySuccessed(String orderId){ // ����ɹ���������� 
			mCanyu.setText("�鿴");
			mCanyu.setOnClickListener(newsOnclick);
			mGatherRmb.setClickable(false);
			mRMBNumber.setText("�Ѹ�");
			ItemShowActivity.this.toastS("֧���ɹ�");
			List<String> canjias = ub.getCanJiaGatherId();// ��ǰ�û��Ĳ���Ļ��ID����
			canjias.add(gather.getObjectId()); // ���¼��ϣ���ӵ�ǰ�ĻID
			List<String> userorderIds = ub.getOrderIds(); // ֧���Ļ�Ķ�����
			userorderIds.add(orderId);//��ӻ�Ķ�����
			ub.setCanJiaGatherId(canjias);//�趨�μӵ��û�id
			ub.setOrderIds(userorderIds);//�趨����id
			
			GatherApplication.appli.setUb(ub); //���ػ�����µ�ǰ�û�����Ϣ
			/**
			 * ���·������ϵ�ǰ�û�����Ϣ������˸���Ļ��ID �������
			 */
			UserBean uu = new UserBean();
			uu.setCanJiaGatherId(canjias);
			uu.setOrderIds(userorderIds);
			uu.update(ItemShowActivity.this,ub.getObjectId(),new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					System.out.println("�����û�����Ϣ�ɹ�");
					List<String> userids = gather.getPaymentUserId();
					userids.add(ub.getObjectId());
					List<String> userNames = gather.getPaymentUserName();
					userNames.add(ub.getUsername());
					gather.setPaymentUserId(userids);//Ϊ����Ӹ����û�id
					gather.setPaymentUserName(userNames);//Ϊ����Ӹ����û�name
					mPerNumber.setText(gather.getPaymentUserId().size()+"");//����ɹ�����ʾ��ǰ��������
					
					/**
					 * ����·���������
					 */
					GatherBean gg = new GatherBean();
					gg.setPaymentUserId(userids);
					gg.setPaymentUserName(userNames);
					gg.update(ItemShowActivity.this, gather.getObjectId(), new UpdateListener() { 
						// ���·����������Ϣ ����˲���û���û���������ID
						
						@Override
						public void onSuccess() {
							//����ɹ�
							System.out.println("���»����Ϣ�ɹ�");
						
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// �û����ݵĻ���
							System.out.println("���»����Ϣʧ��"+arg1+arg0);
						}
					});
				}
				
				@Override
				public void onFailure(int arg0, String arg1) { // ��ǰ�û����·�������Ϣʧ��
					System.out.println("�����û�����Ϣʧ��"+arg1+arg0);
				}
			});
		}
	@Override
	public void initViewOper() {
		mGuanli.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ItemShowActivity.this,GuanliActivity.class);
				startActivity(intent);
			}
		});
		//��ʾ�ղ�
		mLoveNumber.setText(gather.getPraiseUsers().size()+"");
		//��ʾ�۸�
		mRMBNumber.setText(gather.getGatherRMB()+"");
		//�����
		gatherTitle.setText(gather.getGatherTitle());
		//�����
		gatherName.setText(gather.getGatherName());
		//�����
		gatherIntro.setText(gather.getGatherIntro());
		mPerNumber.setText(gather.getPaymentUserId().size()+"");
		
		mCanyu.setOnClickListener(RmbOnClick);
		mGatherRmb.setOnClickListener(RmbOnClick);
		mPingLun.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ItemShowActivity.this,ChatingActivity.class);
				startActivity(intent);
			}
		});
		mBack.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				ItemShowActivity.this.finish();
			}
		});
	}
	public void butOnClick(View v){
		//Я��gatherid��ת�����۽���
		Intent intent = new Intent(this,ChatingActivity.class);
		startActivity(intent);
	}
	
}

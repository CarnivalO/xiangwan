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
	//支付成功后，改变的响应事件
	private OnClickListener newsOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//设定点击跳转到评论
			Intent intent = new Intent(ItemShowActivity.this,ChatingActivity.class);
			startActivity(intent);
		}
	};
	@Override
	protected void onStart() {
		super.onStart();
		mPingLunNumber.setText(gather.getPingluns()+"");
		//判断当前用户是否是发起者
		if(gather.getGatherUserId().equals(ub.getObjectId())){
			mCanyu.setVisibility(View.INVISIBLE);
			mGuanli.setVisibility(View.VISIBLE);
			mGatherRmb.setClickable(false);
			mCanyu.setClickable(false);
		}else{
		//判断当前登录用户是否已经付款,已付款的不让点击
		List<String> payuserids = gather.getPaymentUserId();
		for(int i=0;i < payuserids.size();i++){
			if(payuserids.get(i).equals(ub.getObjectId())){
				mCanyu.setText("查看");
				mCanyu.setOnClickListener(newsOnclick);
				mGatherRmb.setClickable(false);
				mRMBNumber.setText("已付");
				if(gather.getFlag()!=null&&gather.getFlag()){
					myShowDialog();
				}
				break; 
			}
		}
		}
	}
	private void myShowDialog() {
		// 设定活动开启的Dialog
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("当前活动已经开启");
		builder.setMessage("您是否在现场");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				List<String> startuserids = gather.getStartUserId();
				List<String> startusernames = gather.getStartUserName();
				startuserids.add(ub.getObjectId());
				startusernames.add(ub.getUsername());
				
				gather.setStartUserId(startuserids);
				gather.setStartUserName(startusernames);
				//为活动Bean添加id和name
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
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.show();
	}
	private OnClickListener RmbOnClick = new OnClickListener() {
		

		@Override
		public void onClick(View v) {
			//设置支付
			dialog = new AlertDialog.Builder(ItemShowActivity.this);
			dialog.setTitle("请选择支付方式");
			dialog.setMessage("当前活动价格为:"+gather.getGatherRMB()+"￥");
			//微信支付
			dialog.setPositiveButton("微信支付", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
						new BmobPay(ItemShowActivity.this).payByWX(Integer.decode(gather.getGatherRMB()),"", new PayListener() {
						
						private String orderId;
						@Override
						public void unknow() {
							ItemShowActivity.this.toastS("由于网络原因不能确定是否支付成功");
						}
						
						@Override
						public void succeed() {
							ItemShowActivity.this.toastS("支付成功");
							paySuccessed(orderId);
						}
						//顺序1
						@Override
						public void orderId(String arg0) {
							ItemShowActivity.this.toastS("订单号："+arg0);
							this.orderId = arg0;
						}
						//顺序2
						@Override
						public void fail(int arg0, String arg1) {
							ItemShowActivity.this.toastS("支付失败"+arg0+"失败原因"+arg1);
						}
					});
				}
			}); 
			//支付宝支付
			dialog.setNegativeButton("支付宝", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new BmobPay(ItemShowActivity.this).pay(Integer.decode(gather.getGatherRMB()),"", new PayListener() {
						
						private String orderId;
						@Override
						public void unknow() {
							ItemShowActivity.this.toastS("由于网络原因不能确定是否支付成功");
						}
						
						@Override
						public void succeed() {
							paySuccessed(orderId);
							
						}
						//顺序1
						@Override
						public void orderId(String arg0) {
							ItemShowActivity.this.toastS("订单号："+arg0);
							this.orderId = arg0;
						}
						//顺序2
						@Override
						public void fail(int arg0, String arg1) {
							ItemShowActivity.this.toastS("支付失败"+arg0+"失败原因"+arg1);
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
		//判断当前活动是否当前登录用户

//		List背景显示
		String url = null;
		if (gather.getGatherJPG() != null) {
			url = gather.getGatherJPG().getFileUrl(this);
		}
		loader = ImageLoaderutils.getInstance(this);
		options = ImageLoaderutils.getOpt2();
		loader.displayImage(url,gatherImg,options);
		
//		头像显示
		String url2 = null;
		if (gather.getGatherIcon() != null) {
			url2 = gather.getGatherIcon().getFileUrl(this);
		}
		loader = ImageLoaderutils.getInstance(this);
		options2= ImageLoaderutils.getOpt();
		loader.displayImage(url2,usericon,options2);
	}
		private void paySuccessed(String orderId){ // 付款成功后更新数据 
			mCanyu.setText("查看");
			mCanyu.setOnClickListener(newsOnclick);
			mGatherRmb.setClickable(false);
			mRMBNumber.setText("已付");
			ItemShowActivity.this.toastS("支付成功");
			List<String> canjias = ub.getCanJiaGatherId();// 当前用户的参与的活动的ID集合
			canjias.add(gather.getObjectId()); // 更新集合，添加当前的活动ID
			List<String> userorderIds = ub.getOrderIds(); // 支付的活动的订单号
			userorderIds.add(orderId);//添加活动的订单号
			ub.setCanJiaGatherId(canjias);//设定参加的用户id
			ub.setOrderIds(userorderIds);//设定订单id
			
			GatherApplication.appli.setUb(ub); //本地缓冲更新当前用户的信息
			/**
			 * 更新服务器上当前用户的信息，添加了付款的活动的ID 付款订单号
			 */
			UserBean uu = new UserBean();
			uu.setCanJiaGatherId(canjias);
			uu.setOrderIds(userorderIds);
			uu.update(ItemShowActivity.this,ub.getObjectId(),new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					System.out.println("更新用户的信息成功");
					List<String> userids = gather.getPaymentUserId();
					userids.add(ub.getObjectId());
					List<String> userNames = gather.getPaymentUserName();
					userNames.add(ub.getUsername());
					gather.setPaymentUserId(userids);//为活动增加付款用户id
					gather.setPaymentUserName(userNames);//为活动增加付款用户name
					mPerNumber.setText(gather.getPaymentUserId().size()+"");//付款成功后显示当前付款人数
					
					/**
					 * 活动更新服务器数据
					 */
					GatherBean gg = new GatherBean();
					gg.setPaymentUserId(userids);
					gg.setPaymentUserName(userNames);
					gg.update(ItemShowActivity.this, gather.getObjectId(), new UpdateListener() { 
						// 更新服务器活动的信息 添加了参与该活动的用户的姓名和ID
						
						@Override
						public void onSuccess() {
							//付款成功
							System.out.println("更新活动的信息成功");
						
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// 用户数据的回退
							System.out.println("更新活动的信息失败"+arg1+arg0);
						}
					});
				}
				
				@Override
				public void onFailure(int arg0, String arg1) { // 当前用户更新服务器信息失败
					System.out.println("更新用户的信息失败"+arg1+arg0);
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
		//显示收藏
		mLoveNumber.setText(gather.getPraiseUsers().size()+"");
		//显示价格
		mRMBNumber.setText(gather.getGatherRMB()+"");
		//活动标题
		gatherTitle.setText(gather.getGatherTitle());
		//活动名称
		gatherName.setText(gather.getGatherName());
		//活动详情
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
		//携带gatherid跳转到评论界面
		Intent intent = new Intent(this,ChatingActivity.class);
		startActivity(intent);
	}
	
}

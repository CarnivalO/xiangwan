package com.wjn.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjn.application.GatherApplication;
import com.wjn.base.BaseFragment;
import com.wjn.base.BaseInterface;
import com.wjn.bean.UserBean;
import com.wjn.imageloader.ImageLoaderutils;
import com.wjn.utils.BitmapUtils;
import com.wjn.view.MyImageView;
import com.wjn.xiangwan.GatherShowActivity;
import com.wjn.xiangwan.HomeActivity;
import com.wjn.xiangwan.LoginActivity;
import com.wjn.xiangwan.MyPayActivity;
import com.wjn.xiangwan.R;

public class PersonalFragment extends BaseFragment implements BaseInterface {
	// 持有当前管理的HomeActivity的Activity的对象，以做交互用
	// 要通过服务器得到数据
	private HomeActivity home;
	// 获取控件，以及用户名
	private MyImageView userHead;
	private TextView username;
	private LinearLayout mySendsLayout,myLovesLayout,myYouhuiLayout,myPayIdLayout;
	private TextView mySendNumbers,myLovesNumber,myYouHuiNumber,myPayIdNumber;
	// UserBase
	private UserBean ub;
	private ImageLoader loader;
	private DisplayImageOptions options;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		home = (HomeActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//设置布局文件
		return inflater.inflate(R.layout.fragment_personal, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		//获取布局id
		userHead = (MyImageView) findViewById(R.id.fragment_personal_tx);
		username = tvById(R.id.fragment_personal_name);
		mySendNumbers = tvById(R.id.fragment_personal_sendsnumber);
		myLovesNumber  = tvById(R.id.fragment_personal_mylovesnumber);
		mySendsLayout = linearById(R.id.fragment_personal_mysends);
		myLovesLayout = linearById(R.id.fragment_personal_myloves);
		myYouhuiLayout = linearById(R.id.fragment_my_youhuilinear);
		myPayIdLayout = linearById(R.id.fragment_my_payidLinear);
		myYouHuiNumber = tvById(R.id.fragment_my_youhui);
		myPayIdNumber = tvById(R.id.fragment_my_payid);
	}

	@Override
	public void initDatas() {
		// 从Application器拿到用户信息
		ub = GatherApplication.appli.getUb();
		if (ub == null) {
			// 服务器没有则从本地缓存获取 ，需要上下文home
			ub = BmobUser.getCurrentUser(home, UserBean.class);
			// 保持Application中use的持续性
			GatherApplication.appli.setUb(ub);
		}
		if (ub == null) {
			// 跳转到登陆页面重新登陆
			home.startActivity(new Intent(home, LoginActivity.class));
			home.finish();
		}
	}

	@Override
	public void initViewOper() {
		//订单响应事件
		myPayIdLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ub.getCanJiaGatherId().size() == 0){
					home.toastS("您还没有订单");
				}else{
					Intent intent = new Intent(home,MyPayActivity.class);
					home.startActivity(intent);
				}
			}
		});
		//向集合中添加数据
		List<String> myGathers =ub.getMyGathers();
		List<String> myLoves = ub.getLoveGatherId();
		List<String> myPays = ub.getCanJiaGatherId();
		//判断 如果活动是空 则新建集合
				if (myGathers == null) {
					myGathers = new ArrayList<String>();
					ub.setMyGathers(myGathers);
				}
				//判断 如果收藏是空 则新建集合
				if (myLoves == null) {
					myLoves = new ArrayList<String>();
					ub.setLoveGatherId(myLoves);
				}
				//判断 如果付款是空 则新建集合
				if (myPays == null) {
					myPays = new ArrayList<String>();
					ub.setCanJiaGatherId(myPays);
				}
				//如果活动数量大于零
				if (myGathers.size() > 0) {
					mySendsLayout.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent(home , GatherShowActivity.class);
							//准备一个UserBean 在GatherActivity里只要获取到UserBean的id，去服务器查询
							intent.putExtra("key", 0);
							home.startActivity(intent);
						
						}
					});
				}else{
					mySendsLayout.setOnClickListener(new OnClickListener() {
						//如果为0就弹出提示
						public void onClick(View v) {
							home.toastS("您还没有发起过活动");
						}
					});
				}
				//如果收藏数量大于零
				if (myLoves.size() > 0) {
					myLovesLayout.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							Intent intent = new Intent(home , GatherShowActivity.class);
							//跳转之前，只要维护userbean，查询时，从ub里取出所有收藏的活动id
							intent.putExtra("key", 1);
							home.startActivity(intent);
						}
					});
				}else{
					myLovesLayout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							home.toastS("您还没有收藏过活动");
						}
					});
				}
				//如果付款订单大于零
				if (myPays.size() > 0) {
					myPayIdLayout.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							Intent intent = new Intent(home , GatherShowActivity.class);
							//跳转
							//准备一个UserBean 在GatherActivity里只要获取到UserBean的id，去服务器查询
							intent.putExtra("key", 7);
							home.startActivity(intent);
						
						}
					});
				}else{
					mySendsLayout.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							home.toastS("您还没有发起过活动");
						}
					});
				}
		
		// 头像显示
		String url=null ;
		if(ub==null){
			ub = BmobUser.getCurrentUser(home, UserBean.class);
		}
		//设置头像
		if (ub.getUsericon() != null&&ub.getUsericon().getFileUrl(home)!=null) {
			url= ub.getUsericon().getFileUrl(home);
			loader = ImageLoaderutils.getInstance(home);
			options = ImageLoaderutils.getOpt();
			loader.displayImage(url, userHead, options);
		}
		//设置账户
		username.setText(ub.getMobilePhoneNumber() + "");
		//设置头像监听事件
		userHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//设置dialog弹出选择相册页面
				AlertDialog.Builder builder = new Builder(home);
				builder.setTitle("选择");
				final String[] items = { "拍照", "从相册选择", "取消" };
				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (arg1 == 0) {// 拍照
							Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent1, 1);
						} else if (arg1 == 1) {// 相册
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.putExtra("return-data", true);
							intent.setType("image/*");
							// 剪裁
							intent.putExtra("crop", "circleCrop");
							// 裁剪比例
							intent.putExtra("aspectX", 1);
							intent.putExtra("aspectY", 1);
							// 裁剪大小
							intent.putExtra("outputX", 240);
							intent.putExtra("outputY", 240);
							startActivityForResult(intent, 0);
						}
					}
				});
				builder.show();
			}
		});
	}

	/**
	 * 跳转相册或照相机返回
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap;
		if (requestCode == 1 || requestCode == 0) {
			if (data == null) {
				return;
			}
			if (data.getExtras() == null) {
				return;
			}
			if (data.getExtras().get("data") == null) {
				return;
			}
			// 把bitmap图片对象进行压缩
			bitmap = BitmapUtils.bitmap100k((Bitmap) data.getExtras().get(
					"data"),100);
			// 得到文件路径
			File file = new File("mnt/sdcard/gather/userhead/");
			// 判断文件路径是否存在，没有则全部创建
			if (!file.exists()) {
				file.mkdirs();
			}
			// 得到图片在sd卡中的完整路径
			File file2 = new File(file.getAbsolutePath(), "username.jpeg");
			// 删除sd卡中相同文件名的文件
			if (file2.exists())
				file2.delete();

			try {
				FileOutputStream fos = new FileOutputStream(file2);
				bitmap.compress(CompressFormat.JPEG, 100, fos);
			} catch (Exception e) {
				e.printStackTrace();
			}
			userHead.setImageBitmap(bitmap);
			final UserBean newsub = new UserBean();
			newsub.setUsericon(new BmobFile(file2));
			newsub.getUsericon().upload(home, new UploadFileListener() {

				@Override
				public void onSuccess() {
					home.toastS("上传成功");
					ub.setUsericon(newsub.getUsericon());
					ub.update(home, new UpdateListener() {
						@Override
						public void onSuccess() {
							home.toastS("更改用户信息成功");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							home.toastS("更改用户信息失败" + arg1);
							Log.i("wjn", "---onFailure---" + arg1);
						}
					});
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					home.toastS("上传失败" + arg1);
					Log.i("wjn", "-------" + arg1);
				}
			});
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		//对于数据的回显
		if(GatherApplication.appli.getUb()!=null)
		ub =GatherApplication.appli.getUb();
		List<String> myGathers =ub.getMyGathers();
		List<String> myLoves = ub.getLoveGatherId();
		List<String> myPayIds = ub.getCanJiaGatherId();
		//判断活动是否为空
		if (myGathers == null) {
			myGathers=new ArrayList<String>();
			ub.setMyGathers(myGathers);
		}
		//判断收藏是否为空
		if(myLoves == null){
			myLoves = new ArrayList<String>();
			ub.setLoveGatherId(myGathers);
		}
		//判断付款是否为空
		if(myPayIds == null){
			myLoves = new ArrayList<String>();
			ub.setCanJiaGatherId(myPayIds);
		}
		//设置我的回显
		mySendNumbers.setText(myGathers.size()+"");
		//设置收藏回显
		myLovesNumber.setText(myLoves.size()+"");
		//设置付款回显
		myPayIdNumber.setText(myPayIds.size()+"");
	}
}

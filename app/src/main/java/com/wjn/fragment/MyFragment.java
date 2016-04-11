package com.wyl.fragment;

import java.io.File;
import java.io.FileOutputStream;

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
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyl.application.GatherApplication;
import com.wyl.base.BaseFragment;
import com.wyl.base.BaseInterface;
import com.wyl.bean.UserBean;
import com.wyl.imageloader.ImageLoaderutils;
import com.wyl.utils.BitmapUtils;
import com.wyl.view.MyImageView;
import com.wyl.wb.HomeActivity;
import com.wyl.wb.Login;
import com.wyl.wb.R;

public class MyFragment extends BaseFragment implements BaseInterface {
	// 持有当前管理的HomeActivity的Activity的对象，以做交互用
	// 要通过服务器得到数据
	private HomeActivity home;
	// 获取控件，以及用户名
	private MyImageView userHead;
	private TextView username;

	// UserBase
	private UserBean ub;
	private ImageLoader loader;
	private DisplayImageOptions options;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		home = (HomeActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_my, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		userHead = (MyImageView) findViewById(R.id.fragment_my_tx);
		username = tvById(R.id.fragment_my_name);
	}

	@Override
	public void initDatas() {
		// TODO Auto-generated method stub
		// 从Application器拿到用户信息
		ub = GatherApplication.appli.getUser();
		if (ub == null) {
			// 服务器没有则从本地缓存获取 ，需要上下文home
			ub = BmobUser.getCurrentUser(home, UserBean.class);
			// 保持Application中use的持续性
			GatherApplication.appli.setUser(ub);
		}
		if (ub == null) {
			// 跳转到登陆页面重新登陆
			home.startActivity(new Intent(home, Login.class));
			home.finish();
		}
	}

	@Override
	public void initViewOper() {
		// TODO Auto-generated method stub

		// 头像显示
		String url=null ;
		if(ub==null){
			ub = BmobUser.getCurrentUser(home, UserBean.class);
		}
		if (ub.getUsericon() != null&&ub.getUsericon().getFileUrl(home)!=null) {
			url= ub.getUsericon().getFileUrl(home);

		}
		loader = ImageLoaderutils.getInstance(home);
		options = ImageLoaderutils.getOpt();
		loader.displayImage(url, userHead, options);

		username.setText(ub.getMobilePhoneNumber() + "");
		userHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(home);
				builder.setTitle("选择");
				final String[] items = { "拍照", "从相册选择", "取消" };
				// builder.setMessage("请选择");
				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						if (arg1 == 0) {// 拍照
							Intent intent1 = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
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
	 * 跳转相册或照相机还回
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
					"data"));
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userHead.setImageBitmap(bitmap);
			final UserBean newsub = new UserBean();
			newsub.setUsericon(new BmobFile(file2));
			newsub.getUsericon().upload(home, new UploadFileListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					home.toastS("上传成功");
					ub.setUsericon(newsub.getUsericon());
					ub.update(home, new UpdateListener() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							home.toastS("更改用户信息成功");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							home.toastS("更改用户信息失败" + arg1);
							Log.i("wyl", "---onFailure---" + arg1);
						}
					});
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					home.toastS("上传失败" + arg1);
					Log.i("wyl", "-------" + arg1);
				}
			});
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}
}

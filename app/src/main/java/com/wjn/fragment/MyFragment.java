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
	// ���е�ǰ�����HomeActivity��Activity�Ķ�������������
	// Ҫͨ���������õ�����
	private HomeActivity home;
	// ��ȡ�ؼ����Լ��û���
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
		// ��Application���õ��û���Ϣ
		ub = GatherApplication.appli.getUser();
		if (ub == null) {
			// ������û����ӱ��ػ����ȡ ����Ҫ������home
			ub = BmobUser.getCurrentUser(home, UserBean.class);
			// ����Application��use�ĳ�����
			GatherApplication.appli.setUser(ub);
		}
		if (ub == null) {
			// ��ת����½ҳ�����µ�½
			home.startActivity(new Intent(home, Login.class));
			home.finish();
		}
	}

	@Override
	public void initViewOper() {
		// TODO Auto-generated method stub

		// ͷ����ʾ
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
				builder.setTitle("ѡ��");
				final String[] items = { "����", "�����ѡ��", "ȡ��" };
				// builder.setMessage("��ѡ��");
				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						if (arg1 == 0) {// ����
							Intent intent1 = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent1, 1);
						} else if (arg1 == 1) {// ���
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.putExtra("return-data", true);
							intent.setType("image/*");
							// ����
							intent.putExtra("crop", "circleCrop");
							// �ü�����
							intent.putExtra("aspectX", 1);
							intent.putExtra("aspectY", 1);
							// �ü���С
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
	 * ��ת�������������
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
			// ��bitmapͼƬ�������ѹ��
			bitmap = BitmapUtils.bitmap100k((Bitmap) data.getExtras().get(
					"data"));
			// �õ��ļ�·��
			File file = new File("mnt/sdcard/gather/userhead/");
			// �ж��ļ�·���Ƿ���ڣ�û����ȫ������
			if (!file.exists()) {
				file.mkdirs();
			}
			// �õ�ͼƬ��sd���е�����·��
			File file2 = new File(file.getAbsolutePath(), "username.jpeg");
			// ɾ��sd������ͬ�ļ������ļ�
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
					home.toastS("�ϴ��ɹ�");
					ub.setUsericon(newsub.getUsericon());
					ub.update(home, new UpdateListener() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							home.toastS("�����û���Ϣ�ɹ�");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							home.toastS("�����û���Ϣʧ��" + arg1);
							Log.i("wyl", "---onFailure---" + arg1);
						}
					});
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					home.toastS("�ϴ�ʧ��" + arg1);
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

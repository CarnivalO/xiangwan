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
	// ���е�ǰ�����HomeActivity��Activity�Ķ�������������
	// Ҫͨ���������õ�����
	private HomeActivity home;
	// ��ȡ�ؼ����Լ��û���
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
		//���ò����ļ�
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
		//��ȡ����id
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
		// ��Application���õ��û���Ϣ
		ub = GatherApplication.appli.getUb();
		if (ub == null) {
			// ������û����ӱ��ػ����ȡ ����Ҫ������home
			ub = BmobUser.getCurrentUser(home, UserBean.class);
			// ����Application��use�ĳ�����
			GatherApplication.appli.setUb(ub);
		}
		if (ub == null) {
			// ��ת����½ҳ�����µ�½
			home.startActivity(new Intent(home, LoginActivity.class));
			home.finish();
		}
	}

	@Override
	public void initViewOper() {
		//������Ӧ�¼�
		myPayIdLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ub.getCanJiaGatherId().size() == 0){
					home.toastS("����û�ж���");
				}else{
					Intent intent = new Intent(home,MyPayActivity.class);
					home.startActivity(intent);
				}
			}
		});
		//�򼯺����������
		List<String> myGathers =ub.getMyGathers();
		List<String> myLoves = ub.getLoveGatherId();
		List<String> myPays = ub.getCanJiaGatherId();
		//�ж� �����ǿ� ���½�����
				if (myGathers == null) {
					myGathers = new ArrayList<String>();
					ub.setMyGathers(myGathers);
				}
				//�ж� ����ղ��ǿ� ���½�����
				if (myLoves == null) {
					myLoves = new ArrayList<String>();
					ub.setLoveGatherId(myLoves);
				}
				//�ж� ��������ǿ� ���½�����
				if (myPays == null) {
					myPays = new ArrayList<String>();
					ub.setCanJiaGatherId(myPays);
				}
				//��������������
				if (myGathers.size() > 0) {
					mySendsLayout.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent(home , GatherShowActivity.class);
							//׼��һ��UserBean ��GatherActivity��ֻҪ��ȡ��UserBean��id��ȥ��������ѯ
							intent.putExtra("key", 0);
							home.startActivity(intent);
						
						}
					});
				}else{
					mySendsLayout.setOnClickListener(new OnClickListener() {
						//���Ϊ0�͵�����ʾ
						public void onClick(View v) {
							home.toastS("����û�з�����");
						}
					});
				}
				//����ղ�����������
				if (myLoves.size() > 0) {
					myLovesLayout.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							Intent intent = new Intent(home , GatherShowActivity.class);
							//��ת֮ǰ��ֻҪά��userbean����ѯʱ����ub��ȡ�������ղصĻid
							intent.putExtra("key", 1);
							home.startActivity(intent);
						}
					});
				}else{
					myLovesLayout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							home.toastS("����û���ղع��");
						}
					});
				}
				//��������������
				if (myPays.size() > 0) {
					myPayIdLayout.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							Intent intent = new Intent(home , GatherShowActivity.class);
							//��ת
							//׼��һ��UserBean ��GatherActivity��ֻҪ��ȡ��UserBean��id��ȥ��������ѯ
							intent.putExtra("key", 7);
							home.startActivity(intent);
						
						}
					});
				}else{
					mySendsLayout.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							home.toastS("����û�з�����");
						}
					});
				}
		
		// ͷ����ʾ
		String url=null ;
		if(ub==null){
			ub = BmobUser.getCurrentUser(home, UserBean.class);
		}
		//����ͷ��
		if (ub.getUsericon() != null&&ub.getUsericon().getFileUrl(home)!=null) {
			url= ub.getUsericon().getFileUrl(home);
			loader = ImageLoaderutils.getInstance(home);
			options = ImageLoaderutils.getOpt();
			loader.displayImage(url, userHead, options);
		}
		//�����˻�
		username.setText(ub.getMobilePhoneNumber() + "");
		//����ͷ������¼�
		userHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//����dialog����ѡ�����ҳ��
				AlertDialog.Builder builder = new Builder(home);
				builder.setTitle("ѡ��");
				final String[] items = { "����", "�����ѡ��", "ȡ��" };
				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (arg1 == 0) {// ����
							Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
					"data"),100);
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
				e.printStackTrace();
			}
			userHead.setImageBitmap(bitmap);
			final UserBean newsub = new UserBean();
			newsub.setUsericon(new BmobFile(file2));
			newsub.getUsericon().upload(home, new UploadFileListener() {

				@Override
				public void onSuccess() {
					home.toastS("�ϴ��ɹ�");
					ub.setUsericon(newsub.getUsericon());
					ub.update(home, new UpdateListener() {
						@Override
						public void onSuccess() {
							home.toastS("�����û���Ϣ�ɹ�");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							home.toastS("�����û���Ϣʧ��" + arg1);
							Log.i("wjn", "---onFailure---" + arg1);
						}
					});
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					home.toastS("�ϴ�ʧ��" + arg1);
					Log.i("wjn", "-------" + arg1);
				}
			});
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		//�������ݵĻ���
		if(GatherApplication.appli.getUb()!=null)
		ub =GatherApplication.appli.getUb();
		List<String> myGathers =ub.getMyGathers();
		List<String> myLoves = ub.getLoveGatherId();
		List<String> myPayIds = ub.getCanJiaGatherId();
		//�жϻ�Ƿ�Ϊ��
		if (myGathers == null) {
			myGathers=new ArrayList<String>();
			ub.setMyGathers(myGathers);
		}
		//�ж��ղ��Ƿ�Ϊ��
		if(myLoves == null){
			myLoves = new ArrayList<String>();
			ub.setLoveGatherId(myGathers);
		}
		//�жϸ����Ƿ�Ϊ��
		if(myPayIds == null){
			myLoves = new ArrayList<String>();
			ub.setCanJiaGatherId(myPayIds);
		}
		//�����ҵĻ���
		mySendNumbers.setText(myGathers.size()+"");
		//�����ղػ���
		myLovesNumber.setText(myLoves.size()+"");
		//���ø������
		myPayIdNumber.setText(myPayIds.size()+"");
	}
}

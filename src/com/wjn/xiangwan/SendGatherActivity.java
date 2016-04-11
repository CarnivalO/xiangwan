package com.wjn.xiangwan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.baidu.mapapi.search.core.PoiInfo;
import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseInterface;
import com.wjn.bean.GatherBean;
import com.wjn.bean.UserBean;
import com.wjn.utils.BitmapUtils;

public class SendGatherActivity extends BaseActivity implements BaseInterface {
	private EditText gatherName,gatherTitle,gatherIntro,gatherRMB;
	private TextView gatherTime,gatherSite;
	private ImageView gatherIMG,mBack,gatherSave;
	private Spinner gatherClass;
	private PoiInfo info;
	private ArrayAdapter<String> adapter;
	private String[] classs = { "周边户外", "少儿兴趣", "diy", "健身运动", "节日/集市", "演出","展览/展会", "沙龙讲座", "品茶/美食", "多人聚会" };
	private String thisStr = "周边户外";
	private File file;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_send_gather);
		GatherApplication.appli.setInfo(null);
		initViews();
		initDatas();
		initViewOper();
	}
	
	@Override
	public void initViews() {
		//获取id
		gatherName = etById(R.id.act_send_gathername);
		gatherTitle = etById(R.id.act_send_gathertitle);
		gatherIntro = etById(R.id.act_send_gatherintro);
		gatherRMB = etById(R.id.act_send_gatherrmb);
		gatherTime = tvById(R.id.act_send_gathertime);
		gatherSite = tvById(R.id.act_send_gathersite);
		gatherIMG = imageById(R.id.act_send_gatherimg);
		mBack = imageById(R.id.act_send_back);
		gatherSave = imageById(R.id.act_send_gather_save);
		gatherClass = (Spinner)findViewById(R.id.act_send_gather_class);
		System.gc();
	}

	@Override
	public void initDatas() {
		//设置adapter
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,classs);
	}
	public void BaiduMap_Onclick(View v){
		//设定百度地图跳转
		Intent intent = new Intent(SendGatherActivity.this,BaiduMapActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void initViewOper() {
		
		gatherClass.setAdapter(adapter);
		gatherClass.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				thisStr = classs[position];
				Log.i("Log", classs[position]+"被选择");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	mBack.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//弹出警告提示
			AlertDialog.Builder builder = new Builder(SendGatherActivity.this);
			builder.setTitle("注意!");
			builder.setMessage("确定取消发布吗？");
			builder.setNegativeButton("是的", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SendGatherActivity.this.finish();
				}
			});
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				}
			});
		builder.show();
			}
		});
	//发布消息
	gatherSave.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final ProgressDialog dialog = new ProgressDialog(SendGatherActivity.this);
			dialog.setMessage("正在发布~~");
			dialog.show();
			
			final GatherBean gBean = new GatherBean();
			//活动名称
			gBean.setGatherName(gatherName.getText().toString().trim());
			if(gBean.getGatherName().length()<2){
				SendGatherActivity.this.toastS("活动名称过短");
				dialog.dismiss();
				return;
			}
			//活动标题
			gBean.setGatherTitle(gatherTitle.getText().toString().trim());
			if(gBean.getGatherTitle().length()<6||gBean.getGatherTitle().length()>15){
				SendGatherActivity.this.toastS("简介在5-15字之间");
				dialog.dismiss();
				return;
			}
			//活动详细介绍
			gBean.setGatherIntro(gatherIntro.getText().toString().trim());
			if(gBean.getGatherIntro().length()<5){
				SendGatherActivity.this.toastS("详细介绍应10字以上");
				dialog.dismiss();
				return;
			}
			//设定价格
			gBean.setGatherRMB(gatherRMB.getText().toString().trim());
			String time = gatherTime.getText().toString().trim();
			gBean.setGatherTime(time);
			if(info == null){
				SendGatherActivity.this.toastS("请选择活动地点");
				dialog.dismiss();
				return;
			}
			//活动所在城市
			gBean.setGatherCity(info.city);
			//活动所在地点
			gBean.setGatherSite(info.name);
			gBean.setLocation(info);
			gBean.setGatherClass(thisStr);
			gBean.setGpsAdd(new BmobGeoPoint(info.location.longitude,info.location.latitude));
			dialog.show();
			final BmobFile bFile = new BmobFile(file);
			bFile.upload(SendGatherActivity.this, new UploadFileListener() {
				
				//发布成功
				@Override
				public void onSuccess() {
					//设定图片监听
					gBean.setGatherJPG(bFile);
					gBean.setGatherUserId(GatherApplication.appli.getUb().getObjectId());
					gBean.setGatherIcon(GatherApplication.appli.getUb().getUsericon());
					//发布的监听
					gBean.save(SendGatherActivity.this,new SaveListener() {
					
						
						private List<String> myGathers;
						@Override
						public void onSuccess(){
							if (!dialog.isShowing()) {
								gBean.delete(SendGatherActivity.this);
								SendGatherActivity.this.toastS("您放弃了此次发布");
								return;
							}
							UserBean ub =new UserBean();
							 myGathers = GatherApplication.appli.getUb().getMyGathers();
							//判断活动是否为空
							 if (myGathers == null) {
								myGathers = new ArrayList<String>();
							}
							myGathers.add(gBean.getObjectId());
							ub.setMyGathers(myGathers);
							ub.update(SendGatherActivity.this,GatherApplication.appli.getUb().getObjectId(), new UpdateListener() {
								
								public void onSuccess() {
									//保证了本地数据跟服务器数据的一致性
									GatherApplication.appli.getUb().setMyGathers(myGathers);
									BmobQuery<UserBean>  query =new BmobQuery<UserBean>();
									query.addWhereEqualTo("objectId", GatherApplication.appli.getUb().getObjectId());
									query.findObjects(SendGatherActivity.this, new FindListener<UserBean>() {
								
								@Override
								public void onSuccess(List<UserBean> arg0) {
									if (arg0!=null&&arg0.size()>0) {
										GatherApplication.appli.setUb(arg0.get(0));
										
										GatherApplication.appli.setInfo(null);
										SendGatherActivity.this.finish();
										if (dialog.isShowing()) {
											//销毁dialog
											dialog.dismiss();
										}
									}
								}
								@Override
								public void onError(int arg0, String arg1) {
									if(dialog.isShowing()){
										//销毁dialog
										dialog.dismiss();
									}
									}
								});
								}
								@Override
								public void onFailure(int arg0, String arg1) {
									SendGatherActivity.this.toastS("对不起,由于特殊原因,发布失败");
									//回滚操作
									myGathers.remove(gBean.getObjectId());
									GatherApplication.appli.getUb().setMyGathers(myGathers);
									//删除服务器中发表过的活动
									gBean.delete(SendGatherActivity.this);
									if (dialog.isShowing()) {
										//销毁dialog
										dialog.dismiss();									
								}
								}
							});
						}
								@Override
								public void onFailure(int arg0,String arg1) {
									// TODO Auto-generated method stub
									SendGatherActivity.this.toastS("发布失败");
									if(dialog.isShowing()){
										dialog.dismiss();
										}
									}
							});
				
						}
						
				public void onFailure(int arg0, String arg1) {
					if(dialog.isShowing()){
					dialog.dismiss();
					}
				}
			});
		}
	});
			
			
			
			
			
	//选择时间
	gatherTime.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			new DatePickerDialog(SendGatherActivity.this, new OnDateSetListener(){
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					gatherTime.setText(new StringBuilder().append(year).append("-").append((monthOfYear+1)).append("-").append(dayOfMonth));
				}
			},2015,9,20).show();
		}
	});
	}
	public void gather_imgOnclick(View v){

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.putExtra("return-data", true);
		intent.setType("image/*");
		// 剪裁
		intent.putExtra("crop", "circleCrop");
		// 裁剪比例
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 3);
		// 裁剪大小
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 150);
		startActivityForResult(intent, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int arg1, Intent data) {
		super.onActivityResult(requestCode, arg1, data);
		
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
		
			gatherIMG.setImageBitmap(bitmap);
			File file2 = new File("mnt/sdcard/gather/gatherIMG/");
			if(!file2.exists()){
				file2.mkdirs();
			}
			file = new File(file2.getAbsolutePath(),"gatherimg.jpeg");
			if(file.exists()){
				file.delete();
			}
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bitmap.compress(CompressFormat.JPEG,100,stream);
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		info = GatherApplication.appli.getInfo();
		if(info != null){
			gatherSite.setText(info.name);
		}
		//值传递
	
	}
}
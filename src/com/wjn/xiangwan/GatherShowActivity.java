package com.wjn.xiangwan;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;

import com.wjn.adapter.XListViewAdapter;
import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseInterface;
import com.wjn.bean.GatherBean;
import com.wjn.bean.UserBean;

public class GatherShowActivity extends BaseActivity implements BaseInterface {

	private TextView mTitle,dataisnull;
	private ImageView mDialog;
	private ListView list;
	private UserBean ub;
	private int key = 0;
	private List<GatherBean> gathers;
	private XListViewAdapter adapter;
	private boolean dataFlag = true;
	private String moreName;
	private String mClassPager;
	
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_showgather);
		initViews();
		initDatas();
		initViewOper();
	}
	
	@Override
	public void initViews() {
		mTitle = tvById(R.id.act_showgather_title);
		dataisnull = tvById(R.id.act_showgather_null);
		mDialog = imageById(R.id.act_showgather_dialog);
		list = (ListView)findViewById(R.id.act_showgather_list);
	}

	@Override
	public void initDatas() {
		new AsyncTask<Void, Void, Void>(){
			int i = 0;
			protected void onPostExecute(Void result) {
				//判断活动是否为空
				if(gathers == null||gathers.size()<1){
					mDialog.setVisibility(View.INVISIBLE);
					dataisnull.setText("还没有添加活动");
					dataisnull.setTextSize(22.f);
				}else {
					GatherApplication.appli.setAdapterAct(GatherShowActivity.this);
					mDialog.setVisibility(View.INVISIBLE);
					dataisnull.setText("");
					adapter = new XListViewAdapter(gathers);
					list.setAdapter(adapter);
				}
				
			}
			protected void onProgressUpdate(Void[] values) {
				//对进度条进行展示
				switch (i%6) {
				case 0:
					mDialog.setImageResource(R.drawable.id_loading1);
					break;
				case 1:
					mDialog.setImageResource(R.drawable.id_loading2);
					break;
				case 2:
					mDialog.setImageResource(R.drawable.id_loading3);
					break;
				case 3:
					mDialog.setImageResource(R.drawable.id_loading4);
					break;
				case 4:
					mDialog.setImageResource(R.drawable.id_loading5);
					break;
				case 5:
					mDialog.setImageResource(R.drawable.id_loading6);
					break;
				}
				i++;
			};
					@Override
			protected Void doInBackground(Void... params) {
						while (dataFlag){
							publishProgress();
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								Log.i("Log","子线程异常,70行");
							}
						}
						return null;
					}}.execute();
		
		
		ub = GatherApplication.appli.getUb();
		BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
		Intent data = getIntent();
		switch (data.getIntExtra("key" , -1)) {
		case -1:
			//活动标记
			key=-1;
			//设定标题
			mTitle.setText("加载失败...");
			break;
		case 0:
			key=0;
			mTitle.setText("我的发布");
			//在服务器查数据
			query.addWhereEqualTo("gatherUserId", ub.getObjectId());
			break;
		case 1:
			//设定我的收藏
			key=1;
			mTitle.setText("我的收藏");
			List<String> ids = ub.getLoveGatherId();
			//在服务器查数据
			query.addWhereContainedIn("objectId", ids);
			break;
		case 7:
			//设定我参加的
			key=7;
			mTitle.setText("我参加的");
			List<String> canjias = ub.getCanJiaGatherId();
			//在服务器查数据
			query.addWhereContainedIn("objectId", canjias);
			break;
		case 2:
			//设定更多
			key=2;
			moreName = GatherApplication.appli.getMoreName();
		//TODO 如果为空，关闭当前应用
			mTitle.setText(moreName);
			//在服务器查数据
			query.addWhereEqualTo("gatherClass", moreName);
			break;
		case 3:
			//设定搜索
			key=3;
			mClassPager = GatherApplication.appli.getClasspager();
			mTitle.setText("与"+mClassPager+"有关的活动");
			//在服务器查数据
			query.addWhereContains("gatherName", mClassPager);
			mTitle.setSingleLine(true);
			break;
		case 4:
			//设定免费
			key=4;
			mClassPager = GatherApplication.appli.getClasspager();
			mTitle.setText("免费");
			//在服务器查数据
			query.addWhereEqualTo("gatherRMB","0");
			mTitle.setSingleLine(true);
			break;
		case 5:
			//设定热门
			key=5;
			mTitle.setText("热门");
			query.order("-praiseUserscount");
			query.setLimit(5);//获取最近的5条
			mTitle.setSingleLine(true);
			break;
		case 6:
			//设定附近
			key=6;
			mTitle.setText("附近");
			query.addWhereNear("gpsAdd",new BmobGeoPoint(GatherApplication.appli.getLocations().get(0).longitude,GatherApplication.appli.getLocations().get(0).latitude));
			query.setLimit(5);//获取最近的5条
			mTitle.setSingleLine(true);
			break;
		}
		query.findObjects(this, new FindListener<GatherBean>() {
			
			

			@Override
			public void onSuccess(List<GatherBean> arg0) {
				gathers = arg0;
				dataFlag = false;
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				dataFlag = false;
			}
		});
	}

	@Override
	public void initViewOper() {

	}

}

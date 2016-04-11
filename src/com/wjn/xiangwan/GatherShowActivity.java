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
				//�жϻ�Ƿ�Ϊ��
				if(gathers == null||gathers.size()<1){
					mDialog.setVisibility(View.INVISIBLE);
					dataisnull.setText("��û����ӻ");
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
				//�Խ���������չʾ
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
								Log.i("Log","���߳��쳣,70��");
							}
						}
						return null;
					}}.execute();
		
		
		ub = GatherApplication.appli.getUb();
		BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
		Intent data = getIntent();
		switch (data.getIntExtra("key" , -1)) {
		case -1:
			//����
			key=-1;
			//�趨����
			mTitle.setText("����ʧ��...");
			break;
		case 0:
			key=0;
			mTitle.setText("�ҵķ���");
			//�ڷ�����������
			query.addWhereEqualTo("gatherUserId", ub.getObjectId());
			break;
		case 1:
			//�趨�ҵ��ղ�
			key=1;
			mTitle.setText("�ҵ��ղ�");
			List<String> ids = ub.getLoveGatherId();
			//�ڷ�����������
			query.addWhereContainedIn("objectId", ids);
			break;
		case 7:
			//�趨�Ҳμӵ�
			key=7;
			mTitle.setText("�Ҳμӵ�");
			List<String> canjias = ub.getCanJiaGatherId();
			//�ڷ�����������
			query.addWhereContainedIn("objectId", canjias);
			break;
		case 2:
			//�趨����
			key=2;
			moreName = GatherApplication.appli.getMoreName();
		//TODO ���Ϊ�գ��رյ�ǰӦ��
			mTitle.setText(moreName);
			//�ڷ�����������
			query.addWhereEqualTo("gatherClass", moreName);
			break;
		case 3:
			//�趨����
			key=3;
			mClassPager = GatherApplication.appli.getClasspager();
			mTitle.setText("��"+mClassPager+"�йصĻ");
			//�ڷ�����������
			query.addWhereContains("gatherName", mClassPager);
			mTitle.setSingleLine(true);
			break;
		case 4:
			//�趨���
			key=4;
			mClassPager = GatherApplication.appli.getClasspager();
			mTitle.setText("���");
			//�ڷ�����������
			query.addWhereEqualTo("gatherRMB","0");
			mTitle.setSingleLine(true);
			break;
		case 5:
			//�趨����
			key=5;
			mTitle.setText("����");
			query.order("-praiseUserscount");
			query.setLimit(5);//��ȡ�����5��
			mTitle.setSingleLine(true);
			break;
		case 6:
			//�趨����
			key=6;
			mTitle.setText("����");
			query.addWhereNear("gpsAdd",new BmobGeoPoint(GatherApplication.appli.getLocations().get(0).longitude,GatherApplication.appli.getLocations().get(0).latitude));
			query.setLimit(5);//��ȡ�����5��
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

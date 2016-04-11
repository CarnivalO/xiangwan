package com.wjn.xiangwan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.wjn.adapter.ChatingAdapter;
import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseInterface;
import com.wjn.bean.ChatingBean;
import com.wjn.bean.GatherBean;
import com.wjn.bean.UserBean;

/**
 * ����۽���
 * 
 * @author xmwjn
 * 
 */
public class ChatingActivity extends BaseActivity implements BaseInterface {
	private ImageView mSave;
	private EditText mEdit;
	private ListView mList;
	private String textStr;
	private GatherBean gBean;
	private UserBean uBean;

	private List<ChatingBean> chatBeans;
	private ChatingAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_chating);
		GatherApplication.appli.setChatingAct(this);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		mSave = imageById(R.id.act_chating_save);
		mEdit = etById(R.id.act_chating_edit);
		mList = (ListView) findViewById(R.id.act_chating_list);
	}

	@Override
	public void initDatas() {
		gBean = GatherApplication.appli.getItem_showgather();
		uBean = GatherApplication.appli.getUb();
		BmobQuery<ChatingBean> query = new BmobQuery<ChatingBean>();
		query.addWhereEqualTo("gatherBeanID", gBean.getObjectId());
		query.findObjects(this, new FindListener<ChatingBean>() {

			@Override
			public void onSuccess(List<ChatingBean> arg0) {
				chatBeans = arg0;
				if (arg0 == null) {
					chatBeans = new ArrayList<ChatingBean>();
				}
				adapter = new ChatingAdapter(chatBeans);
				mList.setAdapter(adapter);
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	@Override
	public void initViewOper() {
		mSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ChatingActivity.this.toastS("����ύ");
				if (chatBeans == null) {
					chatBeans = new ArrayList<ChatingBean>();
						adapter = new ChatingAdapter(chatBeans);
						mList.setAdapter(adapter);
				}
				textStr = mEdit.getText().toString().trim();
				if (textStr.length() < 1) {
					ChatingActivity.this.toastS("��������������");
					return;
				}
				//����ʱ���ʽ
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				String time = dateFormat.format(new Date());
				//��bean����������
				ChatingBean cBean = new ChatingBean(gBean.getObjectId(), uBean
						.getUsericon(), uBean.getMobilePhoneNumber(), time, textStr);
				//�������Ϣ���
				mEdit.setText("");
				//��������
				cBean.save(ChatingActivity.this);
				chatBeans.add(cBean);// ���� ����
				adapter.updateItem(chatBeans);//��������item
				adapter.notifyDataSetChanged();//֪ͨ������Ϣ
				gBean.setPingluns(adapter.getCount());//��ȡ��������
				GatherBean newsBean = new GatherBean();
				newsBean.setPingluns(adapter.getCount());
				newsBean.update(ChatingActivity.this, gBean.getObjectId(),
						new UpdateListener() {

							@Override
							public void onSuccess() {

							}

							@Override
							public void onFailure(int arg0, String arg1) {

							}
						});
			}
		});
	}

}

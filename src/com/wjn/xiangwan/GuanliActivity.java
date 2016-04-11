package com.wjn.xiangwan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.UpdateListener;

import com.google.gson.Gson;
import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.base.BaseInterface;
import com.wjn.bean.GatherBean;
import com.wjn.bean.PushMsgBean;
import com.wjn.bean.UserBean;
/*
 * �����Ľ��棬
 * ��������������������ˣ���ǰ��¼�û�
 */
public class GuanliActivity extends BaseActivity implements BaseInterface{
	
	private TextView mPersons,mRmb;
	private ListView mList;
	private RadioGroup mRadioGroup;
	private EditText mMsgEdit;
	private ImageView mMsgSend;
	//�����û�name
	List<String> payUserNames;
	//�ֳ��û�name
	List<String> startUserNames;
	//��ǰѡ�е�
	List<String> sendUserNames;
	
	private GatherBean gBean;
	private UserBean uBean;
	private ArrayAdapter<String> adapter;
	
		@Override
		protected void onCreate(Bundle arg0) {
			super.onCreate(arg0);
			setContentView(R.layout.act_guanli);
			initViews();
			initDatas();
			initViewOper();
		}
		@Override
		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			if(gBean.getFlag() == null||!gBean.getFlag()){
				AlertDialog.Builder dialog = new Builder(this);
				dialog.setTitle("����");
				dialog.setMessage("���Ļ��δ���������ڿ���?");
				dialog.setNegativeButton("����", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						gBean.setFlag(true);
						GatherBean gg = new GatherBean();
						gg.setFlag(true);
						gg.update(GuanliActivity.this,gBean.getObjectId(),new UpdateListener() {
							
							@Override
							public void onSuccess() {
							//֪ͨ�û�
								BmobPushManager bmobPush = new BmobPushManager(GuanliActivity.this);
								BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
								query.addWhereContainsAll("uid", gBean.getPaymentUserName());
								bmobPush.setQuery(query);
								PushMsgBean msg = new PushMsgBean();
								msg.setGatherId(gBean.getObjectId());
								msg.setUsername(uBean.getUsername());
								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
								String time = dateFormat.format(new Date());
								msg.setTime(time);
								msg.setText("������Ļ�Ѿ���ʼ");
								Gson g = new Gson();
								try {
									bmobPush.pushMessage(new JSONObject(g.toJson(msg)));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								
							}
						});
					}
				});
				dialog.setPositiveButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				dialog.show();
			}
		}
		@Override
		public void initViews() {
			mPersons = tvById(R.id.act_guanli_per);
			mRmb = tvById(R.id.act_guanli_rmb);
			mList = (ListView) findViewById(R.id.act_guanli_list);
			mRadioGroup = (RadioGroup) findViewById(R.id.act_guanli_radiogroup);
			mMsgEdit = etById(R.id.act_guanli_msg_edit);
			mMsgSend = imageById(R.id.act_guanli_msg_send);
		}

		@Override
		public void initDatas() {
			//��ʼ������
			gBean = GatherApplication.appli.getItem_showgather();
			uBean = GatherApplication.appli.getUb();
			payUserNames = gBean.getPaymentUserName();
			sendUserNames = payUserNames;
			startUserNames = gBean.getStartUserName();
			mPersons.setText(payUserNames.size()+"");
			String rmbStr = gBean.getGatherRMB();
			Integer rmb = Integer.decode(rmbStr);
			mRmb.setText(rmb*payUserNames.size()+"");
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,payUserNames);
		}

		@Override
		public void initViewOper() {
			mMsgSend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// ���ù����������
					String msgtext = mMsgEdit.getText().toString().trim();
					if(msgtext.length()<1){
						GuanliActivity.this.toastS("����������");
						return;
					}
					//����bmobpush
					BmobPushManager bmobPush = new BmobPushManager(GuanliActivity.this);
					BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
					query.addWhereContainsAll("uid", sendUserNames);//�޸ķ�����
					bmobPush.setQuery(query);
					PushMsgBean msg = new PushMsgBean();
					//��ȡ����id
					msg.setGatherId(gBean.getObjectId());
					//��ȡ�û���
					msg.setUsername(uBean.getUsername());
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String time = dateFormat.format(new Date());
					//����ʱ��
					msg.setTime(time);
					//������Ϣ
					msg.setText(msgtext);
					mMsgEdit.setText("");
					//ʹ��json������Ϣ
					Gson g = new Gson();
					try {
						bmobPush.pushMessage(new JSONObject(g.toJson(msg)));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			
			mList.setAdapter(adapter);
			mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//����RadioButton
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch (checkedId) {
					case R.id.act_guanli_radiogroup_item1:
						mMsgEdit.setHint("��������������Ϣ");
						adapter = new ArrayAdapter<String>(GuanliActivity.this, android.R.layout.simple_list_item_1,payUserNames);
						mList.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						sendUserNames = payUserNames;
						break;
					case R.id.act_guanli_radiogroup_item2:
						adapter = new ArrayAdapter<String>(GuanliActivity.this, android.R.layout.simple_list_item_1,startUserNames);
						mList.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						mMsgEdit.setHint("���ֳ���������Ϣ");
						sendUserNames = startUserNames;
						break;
					case R.id.act_guanli_radiogroup_item3:
						mMsgEdit.setHint("��δ����������Ϣ");
						List<String> meidaos = new ArrayList<String>();
						meidaos.addAll(payUserNames);
						meidaos.remove(startUserNames);
						adapter = new ArrayAdapter<String>(GuanliActivity.this, android.R.layout.simple_list_item_1,meidaos);
						mList.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						sendUserNames = meidaos;
						break;

					default:
						break;
					}
				}
			});
		}
}

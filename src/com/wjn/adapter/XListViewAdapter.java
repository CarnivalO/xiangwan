package com.wjn.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjn.application.GatherApplication;
import com.wjn.base.BaseActivity;
import com.wjn.bean.GatherBean;
import com.wjn.bean.UserBean;
import com.wjn.imageloader.ImageLoaderutils;
import com.wjn.view.MyImageView;
import com.wjn.xiangwan.LoginActivity;
import com.wjn.xiangwan.R;
/**
 * HomeFragment��XlistView��Adapter
 * @author xmwjn
 *
 */
public class XListViewAdapter extends BaseAdapter {
	private List<GatherBean> gathers;
	private BaseActivity act;
	
	private ImageLoader loader;
	private DisplayImageOptions options;
	private DisplayImageOptions options2;
	private UserBean ub;
	/**
	 * ��Ŀ�����в�����ͨ�����췽������Activity
	 * ��������adapterֻ���ڹ̶�Activity��ʹ��
	 */

	public XListViewAdapter(List<GatherBean> gathers) {
		super();
		this.gathers = gathers;
		if(this.gathers == null){
			this.gathers=new ArrayList<GatherBean>();
		}
		this.setAct(GatherApplication.appli.getAdapterAct());
		ub = GatherApplication.appli.getUb();
		if(ub == null){
			ub = BmobUser.getCurrentUser(act,UserBean.class);
		}
		if(ub == null){
			gathers = new ArrayList<GatherBean>();
		}
	}

	@Override
	public int getCount() {
		if(gathers==null){
			gathers = new ArrayList<GatherBean>();
			
		}
		return gathers.size();
	}
	/**
	 * ���ĵ�ǰ����Դ
	 * @param gathers
	 */
	public void updateGathers(List<GatherBean> gathers){
		this.gathers = gathers;
	}
	@Override
	public Object getItem(int position) {
		return gathers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//ListView���Ż�
		View v = null;
		ViewHolder vh = null;
		final GatherBean gb = gathers.get(position);
		if (convertView == null) {
			vh = new ViewHolder();
			v = act.getLayoutInflater().inflate(R.layout.xlist_item, null);
			vh.gatherImg = (ImageView) v.findViewById(R.id.item_gatherimg);
			vh.gatherLove = (ImageView) v.findViewById(R.id.item_gatherlove);
			vh.userIcon = (MyImageView) v.findViewById(R.id.item_usericon);
			vh.gatherName = (TextView) v.findViewById(R.id.item_gathername);
			vh.gatherSite = (TextView) v.findViewById(R.id.item_gathersite);
			vh.gatherKm = (TextView) v.findViewById(R.id.item_km);
			vh.gatherRMB = (TextView) v.findViewById(R.id.item_rmb);
			vh.gatherTitle = (TextView) v.findViewById(R.id.item_gathertitle);
			v.setTag(vh);
		} else {
			v = convertView;
			vh = (ViewHolder) v.getTag();
		}

		vh.gatherName.setText(gb.getGatherName());
		vh.gatherSite.setText(gb.getGatherSite());
		vh.gatherRMB.setText(gb.getGatherRMB()+"");
		vh.gatherTitle.setText(gb.getGatherTitle());

		/**
		 * �����û�ָ������������㣬�������������ʵ�ʵ�����롣���Ĵ������£� //����p1��p2����֮���ֱ�߾��룬��λ����
		 * DistanceUtil. getDistance(p1, p2);
		 * �����û�ָ�������½Ǻ����Ͻ����꣬�������������깹�ɾ��εĵ�����������Ĵ������£� //����northeast,
		 * southwest���㹹�ɾ��εĵ�����������������������ꡣ��λ��ƽ����
		 * AreaUtil.calculateArea(northeast, southwest);
		 */
		LatLng startKM = new LatLng(GatherApplication.appli.getLocations().get(0).latitude, GatherApplication.appli.getLocations().get(0).longitude);
		LatLng endKM = gb.getLocation().location;
		Log.i("Log", "start:" + startKM.latitude + "," + startKM.longitude);
		Log.i("Log", "end"+endKM.latitude + "," +endKM.longitude);
		
		double ssss = DistanceUtil.getDistance(startKM, endKM);
		String gatherKM =getKM((int) ssss);
		
		vh.gatherKm.setText(gatherKM);
//		List������ʾ
		String url = null;
		if (gb.getGatherJPG() != null) {
			url = gb.getGatherJPG().getFileUrl(act);
		}
		loader = ImageLoaderutils.getInstance(act);
		options = ImageLoaderutils.getOpt();
		loader.displayImage(url,vh.gatherImg,options);
		
//		ͷ����ʾ
		String url2 = null;
		if (gb.getGatherIcon() != null) {
			url2 = gb.getGatherIcon().getFileUrl(act);
		}
		loader = ImageLoaderutils.getInstance(act);
		options2= ImageLoaderutils.getOpt();
		loader.displayImage(url2,vh.userIcon,options2);
		
		//��ֹ�û����ֿ�
		if(ub == null){
			ub = UserBean.getCurrentUser(act,UserBean.class);
		}
		if(ub == null){
			vh.gatherLove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					act.startActivity(new Intent(act,LoginActivity.class));
					act.finish();
				}
			});
		}else{
		//���� �ı���ɫ
		//Ϊ�˷�ֹnull ���Ȼ�ȡ��
		List<String> userloves = ub.getLoveGatherId();
		if(userloves == null){
			userloves = new ArrayList<String>();
		}
		//��ԭ��ʼ״̬
		vh.gatherLove.setImageResource(R.drawable.loveoff);
		for(int i = 0;i<userloves.size();i++){
			if(	userloves.get(i).equals(gb.getObjectId())){
				//������޹������
				vh.gatherLove.setImageResource(R.drawable.loveon);
				break;
			}
		}
		vh.gatherLove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//���õ���
				//����userbean�Ƿ����ղ�
				boolean flag= false;
				List<String> userloves2 = ub.getLoveGatherId();
				for(int i = 0;i<userloves2.size();i++){
					if(	userloves2.get(i).equals(gb.getObjectId())){
						//������޹������
						flag = true;
						break;
					
					}
				}
				updateGatherLove((ImageView)v,position,flag);
				//���ղ�
				//δ�ղ�
				((ImageView)v).setImageResource(R.drawable.loveon);
			}
		});
		}
		return v;
	}

	private String getKM(int distance) {
		if(distance<1000){
			return ((int)distance )+ " ��";
		}
		
		double km = distance/1000;
		
		return ((int) km) + "ǧ��";
	}

	static class ViewHolder {
		ImageView gatherImg, gatherLove;
		MyImageView userIcon;
		TextView gatherName, gatherKm, gatherRMB, gatherTitle, gatherSite;
	}

	public Activity getAct() {
		return act;
	}
	/**
	 * �������
	 * @param position 
	 * @param v 
	 * @param flag 
	 */
	private void updateGatherLove(final ImageView v, int position, final boolean flag2){
		//��ȡ�û��е��ղ�
		List<String> userloves = ub.getLoveGatherId();
		//��ȡ�����ղ��û�
		final GatherBean gb = gathers.get(position);
		List<String> gatherloves = gb.getPraiseUsers();
		if(userloves == null){
			userloves = new ArrayList<String>();
			ub.setLoveGatherId(userloves);
		}
		if(gatherloves == null){
			gatherloves = new ArrayList<String>();
			gb.setPraiseUsers(gatherloves);
		}
		if (flag2) {//ȡ����
			v.setImageResource(R.drawable.loveoff);
			//ɾ�����ػ�����е�  user��obiectId
			userloves.remove(gb.getObjectId());
			//ɾ�������û������е�  user��obiectId
			gatherloves.remove(ub.getObjectId());
			//���ı���bean��������
			ub.setLoveGatherId(userloves);
			gb.setPraiseUsers(gatherloves);
			//�ϴ�������
			final UserBean uu = new UserBean();
			uu.setLoveGatherId(userloves);
			final GatherBean gg = new GatherBean();
			gg.setPraiseUsers(gatherloves);
			gg.update(act, gb.getObjectId(), new UpdateListener() {
				
				public void onSuccess() {
					uu.update(act, ub.getObjectId(), new UpdateListener() {
						
						public void onSuccess() {
							act.toastS("ȡ������");
						}
						
						public void onFailure(int arg0, String arg1) {
							//ȡ����
							v.setImageResource(R.drawable.loveon);
							ub.getLoveGatherId().add(gb.getObjectId());
							gb.getPraiseUsers().add(ub.getObjectId());
							gg.getPraiseUsers().remove(ub.getObjectId());
							gg.update(act, gb.getObjectId(), new UpdateListener() {
								
								public void onSuccess() {
									
								}
								
								public void onFailure(int arg0, String arg1) {
									
								}
							});
						}
					});
				}
				
				public void onFailure(int arg0, String arg1) {
					//ȡ����
						v.setImageResource(R.drawable.loveon);
						ub.getLoveGatherId().add(gb.getObjectId());
						gb.getPraiseUsers().add(ub.getObjectId());
						
				}
			});
			
		}else{//����
			v.setImageResource(R.drawable.loveon);
			//���裺1���
			userloves.add(gb.getObjectId());
			gatherloves.add(ub.getObjectId());
			
			//2.��֤�������ݸ����������ݵ�һ����
			ub.setLoveGatherId(userloves);
			gb.setPraiseUsers(gatherloves);
			//3.�ϴ�������
			final GatherBean gg = new GatherBean();
			gg.setPraiseUsers(gatherloves);
			
			final UserBean uu = new UserBean();
			uu.setLoveGatherId(userloves);
			gg.update(act, gb.getObjectId(), new UpdateListener() {
				
				public void onSuccess() {
					
					uu.update(act, ub.getObjectId() , new UpdateListener() {
						
						public void onSuccess() {
							act.toastS("�ղسɹ�");
						}
						
						public void onFailure(int arg0, String arg1) {
//							gatherloves.remove(ub.getObjectId());
//							gg.setPraiseUsers(gatherloves);
							//����ͼ��ԭ��Ϊ����֮ǰ
								v.setImageResource(R.drawable.loveoff);
							ub.getLoveGatherId().remove(gb.getObjectId());
							gb.getPraiseUsers().remove(ub.getObjectId());
							gg.getPraiseUsers().remove(ub.getObjectId());
							gg.update(act, gb.getObjectId(), new UpdateListener() {
								
								public void onSuccess() {
									
								}
								
								public void onFailure(int arg0, String arg1) {
									
								}
							});
						}
					});
				}
				
				public void onFailure(int arg0, String arg1) {
					//����ͼ��ԭ��Ϊ����֮ǰ
						v.setImageResource(R.drawable.loveoff);
					ub.getLoveGatherId().remove(gb.getObjectId());
					gb.getPraiseUsers().remove(ub.getObjectId());
				}
			});
		}
}
	public void setAct(BaseActivity act) {
		this.act = act;
	}

}

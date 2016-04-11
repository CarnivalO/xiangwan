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
 * HomeFragment中XlistView的Adapter
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
	 * 项目开发中不建议通过构造方法传入Activity
	 * 这代表这个adapter只能在固定Activity中使用
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
	 * 更改当前数据源
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
		//ListView的优化
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
		 * 根据用户指定的两个坐标点，计算这两个点的实际地理距离。核心代码如下： //计算p1、p2两点之间的直线距离，单位：米
		 * DistanceUtil. getDistance(p1, p2);
		 * 根据用户指定的左下角和右上角坐标，计算这两个坐标构成矩形的地理面积。核心代码如下： //计算northeast,
		 * southwest两点构成矩形的地理面积，即东北、西南坐标。单位：平方米
		 * AreaUtil.calculateArea(northeast, southwest);
		 */
		LatLng startKM = new LatLng(GatherApplication.appli.getLocations().get(0).latitude, GatherApplication.appli.getLocations().get(0).longitude);
		LatLng endKM = gb.getLocation().location;
		Log.i("Log", "start:" + startKM.latitude + "," + startKM.longitude);
		Log.i("Log", "end"+endKM.latitude + "," +endKM.longitude);
		
		double ssss = DistanceUtil.getDistance(startKM, endKM);
		String gatherKM =getKM((int) ssss);
		
		vh.gatherKm.setText(gatherKM);
//		List背景显示
		String url = null;
		if (gb.getGatherJPG() != null) {
			url = gb.getGatherJPG().getFileUrl(act);
		}
		loader = ImageLoaderutils.getInstance(act);
		options = ImageLoaderutils.getOpt();
		loader.displayImage(url,vh.gatherImg,options);
		
//		头像显示
		String url2 = null;
		if (gb.getGatherIcon() != null) {
			url2 = gb.getGatherIcon().getFileUrl(act);
		}
		loader = ImageLoaderutils.getInstance(act);
		options2= ImageLoaderutils.getOpt();
		loader.displayImage(url2,vh.userIcon,options2);
		
		//防止用户出现空
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
		//遍历 改变颜色
		//为了防止null 首先获取到
		List<String> userloves = ub.getLoveGatherId();
		if(userloves == null){
			userloves = new ArrayList<String>();
		}
		//还原初始状态
		vh.gatherLove.setImageResource(R.drawable.loveoff);
		for(int i = 0;i<userloves.size();i++){
			if(	userloves.get(i).equals(gb.getObjectId())){
				//如果点赞过，变红
				vh.gatherLove.setImageResource(R.drawable.loveon);
				break;
			}
		}
		vh.gatherLove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//设置点赞
				//遍历userbean是否已收藏
				boolean flag= false;
				List<String> userloves2 = ub.getLoveGatherId();
				for(int i = 0;i<userloves2.size();i++){
					if(	userloves2.get(i).equals(gb.getObjectId())){
						//如果点赞过，变红
						flag = true;
						break;
					
					}
				}
				updateGatherLove((ImageView)v,position,flag);
				//已收藏
				//未收藏
				((ImageView)v).setImageResource(R.drawable.loveon);
			}
		});
		}
		return v;
	}

	private String getKM(int distance) {
		if(distance<1000){
			return ((int)distance )+ " 米";
		}
		
		double km = distance/1000;
		
		return ((int) km) + "千米";
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
	 * 点击心形
	 * @param position 
	 * @param v 
	 * @param flag 
	 */
	private void updateGatherLove(final ImageView v, int position, final boolean flag2){
		//获取用户中的收藏
		List<String> userloves = ub.getLoveGatherId();
		//获取活动里的收藏用户
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
		if (flag2) {//取消赞
			v.setImageResource(R.drawable.loveoff);
			//删除本地活动对象中的  user的obiectId
			userloves.remove(gb.getObjectId());
			//删除本地用户对象中的  user的obiectId
			gatherloves.remove(ub.getObjectId());
			//更改本地bean对象属性
			ub.setLoveGatherId(userloves);
			gb.setPraiseUsers(gatherloves);
			//上传服务器
			final UserBean uu = new UserBean();
			uu.setLoveGatherId(userloves);
			final GatherBean gg = new GatherBean();
			gg.setPraiseUsers(gatherloves);
			gg.update(act, gb.getObjectId(), new UpdateListener() {
				
				public void onSuccess() {
					uu.update(act, ub.getObjectId(), new UpdateListener() {
						
						public void onSuccess() {
							act.toastS("取消点赞");
						}
						
						public void onFailure(int arg0, String arg1) {
							//取消赞
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
					//取消赞
						v.setImageResource(R.drawable.loveon);
						ub.getLoveGatherId().add(gb.getObjectId());
						gb.getPraiseUsers().add(ub.getObjectId());
						
				}
			});
			
		}else{//点赞
			v.setImageResource(R.drawable.loveon);
			//步骤：1添加
			userloves.add(gb.getObjectId());
			gatherloves.add(ub.getObjectId());
			
			//2.保证本地数据跟服务器数据的一致性
			ub.setLoveGatherId(userloves);
			gb.setPraiseUsers(gatherloves);
			//3.上传服务器
			final GatherBean gg = new GatherBean();
			gg.setPraiseUsers(gatherloves);
			
			final UserBean uu = new UserBean();
			uu.setLoveGatherId(userloves);
			gg.update(act, gb.getObjectId(), new UpdateListener() {
				
				public void onSuccess() {
					
					uu.update(act, ub.getObjectId() , new UpdateListener() {
						
						public void onSuccess() {
							act.toastS("收藏成功");
						}
						
						public void onFailure(int arg0, String arg1) {
//							gatherloves.remove(ub.getObjectId());
//							gg.setPraiseUsers(gatherloves);
							//把视图还原至为点赞之前
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
					//把视图还原至为点赞之前
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

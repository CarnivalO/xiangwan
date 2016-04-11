package com.wjn.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjn.application.GatherApplication;
import com.wjn.bean.ChatingBean;
import com.wjn.imageloader.ImageLoaderutils;
import com.wjn.xiangwan.ChatingActivity;
import com.wjn.xiangwan.R;

public class ChatingAdapter extends BaseAdapter{
	private List<ChatingBean> chatingBeans;
	private ChatingActivity act;
	private ImageLoader loader;
	private DisplayImageOptions options;
	
	
	public ChatingAdapter(List<ChatingBean> chatingBeans){
		super();
		this.chatingBeans = chatingBeans;
		act = (ChatingActivity) GatherApplication.appli.getChatingAct();
	}

	@Override
	public int getCount() {
		return chatingBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return chatingBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		ViewHolder vh;
		if(convertView == null){
			v = act.getLayoutInflater().inflate(R.layout.chating_item, null);
			vh = new ViewHolder();
			vh.usericon = (ImageView)v.findViewById(R.id.chating_usericon);
			vh.username = (TextView) v.findViewById(R.id.chating_username);
			vh.time = (TextView) v.findViewById(R.id.chating_time);
			vh.text = (TextView) v.findViewById(R.id.chating_text);
			v.setTag(vh);
		}else{
			v = convertView;
			vh = (ViewHolder)v.getTag();
		}
		ChatingBean bean = chatingBeans.get(position);
		vh.username.setText(bean.getUsername());
		vh.time.setText(bean.getTime());
		vh.text.setText(bean.getText());
		String url = null;
		if (bean.getUsericon() != null) {
			url= bean.getUsericon().getFileUrl(act);
		}
		loader = ImageLoaderutils.getInstance(act);
		options = ImageLoaderutils.getOpt();
		loader.displayImage(url, vh.usericon, options);
		return v;
	}
	static class ViewHolder{
		ImageView usericon;
		TextView username,time,text;
	}
	public void updateItem(List<ChatingBean> chatingBeans){
		this.chatingBeans = chatingBeans;
	}
}

package com.wjn.bmobpush;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

import com.google.gson.Gson;
import com.wjn.application.GatherApplication;
import com.wjn.bean.GatherBean;
import com.wjn.bean.PushMsgBean;
import com.wjn.xiangwan.ItemShowActivity;
import com.wjn.xiangwan.R;

public class MyPushMessageReceiver extends BroadcastReceiver{
	private PushMsgBean pushBean;
	private GatherBean gBean;
	
    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub
    	if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            String json = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
           Gson gson = new Gson();
           pushBean = gson.fromJson(json, PushMsgBean.class);
           BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
           query.getObject(context, pushBean.getGatherId(), new GetListener<GatherBean>() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(GatherBean arg0) {
				// TODO Auto-generated method stub
				gBean = arg0;
				showNotification(context);
				GatherApplication.appli.setNotifgather(arg0);
			}
		});
    	}
    }
private void showNotification(Context context){
		Notification noti = new Notification(R.drawable.ic_launcher,"享玩",System.currentTimeMillis());
		noti.defaults = Notification.DEFAULT_ALL;
		noti.flags = Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(context,ItemShowActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
		noti.setLatestEventInfo(context, pushBean.getUsername(), pushBean.getText(),pIntent);
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0,noti);
	}
}
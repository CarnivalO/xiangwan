package com.wyl.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	public static boolean isNet(Context content) {
		// 获取操作对象
		ConnectivityManager conne = (ConnectivityManager) content
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 当前网络出问题了，info可能为null
		// 如果info为null,一定没网
		// 获取联网状态对象
		if (conne != null) {
			NetworkInfo info = conne.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				return true;
			}

		}
		return false;
	}
}
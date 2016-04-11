package com.wyl.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	public static boolean isNet(Context content) {
		// ��ȡ��������
		ConnectivityManager conne = (ConnectivityManager) content
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ��ǰ����������ˣ�info����Ϊnull
		// ���infoΪnull,һ��û��
		// ��ȡ����״̬����
		if (conne != null) {
			NetworkInfo info = conne.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				return true;
			}

		}
		return false;
	}
}
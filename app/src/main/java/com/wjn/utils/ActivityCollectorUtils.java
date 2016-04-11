package com.wyl.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityCollectorUtils {
	//�������Activity��list����
	public static List<Activity> activitys = new ArrayList<Activity>();
	//���
	public static void addActivity(Activity activity){
		activitys.add(activity);
	}
	//ɾ��
	public static void removeActivity(Activity activity) { 

		activitys.remove(activity); 
    } 
	//�˳�����
    public static void finishAll() { 

        for (Activity activity : activitys) { 
            if (!activity.isFinishing()) { 

                activity.finish(); 
            } 

        } 
    } 
}

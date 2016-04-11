package com.wyl.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityCollectorUtils {
	//创建存放Activity的list集合
	public static List<Activity> activitys = new ArrayList<Activity>();
	//添加
	public static void addActivity(Activity activity){
		activitys.add(activity);
	}
	//删除
	public static void removeActivity(Activity activity) { 

		activitys.remove(activity); 
    } 
	//退出程序
    public static void finishAll() { 

        for (Activity activity : activitys) { 
            if (!activity.isFinishing()) { 

                activity.finish(); 
            } 

        } 
    } 
}

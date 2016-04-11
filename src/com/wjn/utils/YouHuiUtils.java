package com.wjn.utils;

import java.util.Random;

public class YouHuiUtils {
	public static String getYouHui(){
		Random random = new Random(20);
		int randomNumber = random.nextInt(20);
		if(randomNumber == 1){
			return "5ÔªÓÅ»İÈ¯";
		}
		
		return null;
		
	}
}

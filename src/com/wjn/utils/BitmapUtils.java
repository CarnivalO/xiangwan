package com.wjn.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class BitmapUtils {
	public static Bitmap bitmap100k(Bitmap bit,int kb){
		ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
		//把当前的Bitmap对象，以一定的品质压缩进流
		//品质不是百分比
		bit.compress(CompressFormat.JPEG, 100, bytearray);
		int count = 100;
		while (bytearray.toByteArray().length>(kb*1024)) {
			//清空一下内存流
			bytearray.reset();
			count = count*90/100;
			bit.compress(CompressFormat.JPEG, count, bytearray);
		}
		byte[] data = bytearray.toByteArray();
		bit = BitmapFactory.decodeByteArray(data, 0, data.length);
		return bit;
	}
}

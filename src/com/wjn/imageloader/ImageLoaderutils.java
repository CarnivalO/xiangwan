package com.wjn.imageloader;


import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wjn.xiangwan.R;

public class ImageLoaderutils {
private  static	ImageLoader loader;
private  static  ImageLoaderConfiguration.Builder confbuilder;
private   static ImageLoaderConfiguration conf;
private   static DisplayImageOptions.Builder optbuilder;
private  static DisplayImageOptions opt;
//����ע��ŵ�imageloader����
	public static  ImageLoader getInstance(Context context)
	{
	
		loader=ImageLoader.getInstance();
		confbuilder=new ImageLoaderConfiguration.Builder(context);
		File file=new File("/mnt/sdcard/gather/imageloader");
		//�ƶ�sdcard�����·��
		confbuilder.discCache(new UnlimitedDiscCache(file));
		//�����ͼƬ����
		confbuilder.discCacheFileCount(100);
		//������������
		confbuilder.discCacheSize(1024*1024*10*10);
		conf=confbuilder.build();
		loader.init(conf);
		return loader;
	}
	//������ʾͼƬʹ��opt
	public   static DisplayImageOptions getOpt()
	{
		return new DisplayImageOptions.Builder()  
		 .showImageOnLoading(R.drawable.head) //����ͼƬ�������ڼ���ʾ��ͼƬ  
		 .showImageForEmptyUri(R.drawable.head)//����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ  
		.cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ���  
		.cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����  
		.considerExifParams(true)  //�Ƿ���JPEGͼ��EXIF��������ת����ת��
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//����ͼƬ����εı��뷽ʽ��ʾ  
		.bitmapConfig(Bitmap.Config.RGB_565)//����ͼƬ�Ľ�������//  
		.resetViewBeforeLoading(true)//����ͼƬ������ǰ�Ƿ����ã���λ  
		.build();//�������  
	}
	
	//������ʾͼƬʹ��opt  ListView�� �û���ͷ��
		public   static DisplayImageOptions getOpt2()
		{
			optbuilder=new DisplayImageOptions.Builder();
			//uri ����Ĭ��ͼƬ
			optbuilder.showImageForEmptyUri(R.drawable.head);
			//ͼƬ��ȡʧ�� ���ص�Ĭ��ͼƬ
			optbuilder.showImageOnFail(R.drawable.head);
			optbuilder.cacheInMemory(true);//���ڴ滺��
			optbuilder.cacheOnDisc(true);//��Ӳ�̻���
			opt=optbuilder.build();
			return opt;
		}
		
		//������ʾͼƬʹ��opt   ListView��  ���ͼ��
		public   static DisplayImageOptions getOpt3()
		{
			optbuilder=new DisplayImageOptions.Builder();
			//uri ����Ĭ��ͼƬ
			optbuilder.showImageForEmptyUri(R.drawable.head);
			//ͼƬ��ȡʧ�� ���ص�Ĭ��ͼƬ
			optbuilder.showImageOnFail(R.drawable.head);
			optbuilder.cacheInMemory(true);//���ڴ滺��
			optbuilder.cacheOnDisc(true);//��Ӳ�̻���
			opt=optbuilder.build();
			return opt;
		}

}

package com.wjn.base;

import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 向上采取的BaseActivity,用来简化view对象通过findViewById()获取对象
 * 
 * @author wangjianian
 * 
 */
public class BaseActivity extends FragmentActivity {
	
	/**
	 * 采取的toast方法,传入字符串，会在当前activity上把传人的字符串toast出来，时间为短
	 * @param str 想要toast的字符串
	 */
	public void toastS(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 采取的toast方法,传入字符串，会在当前activity上把传人的字符串toast出来，时间为长
	 * @param str 想要toast的字符串
	 */
	public void toastL(String str){
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 获取按钮——通过传入的id，获得id对象对应的Button组件对象，并返回该对象
	 * 
	 * @param id
	 *            传入系统或用户之定义的Id，格式：R.xx.xxx
	 * @return 返回传入Id所对应的Button组件对象
	 */
	public Button butById(int id) {
		return (Button) findViewById(id);
	}

	/**
	 * 获取文本框——通过传入的id，获得id对象对应的TextView组件对象，并返回该对象
	 * 
	 * @param id
	 *            传入系统或用户之定义的Id，格式：R.xx.xxx
	 * @return 返回传入Id所对应的TextView组件对象
	 */
	public TextView tvById(int id) {
		return (TextView) findViewById(id);
	}

	/**
	 * 获取按钮——通过传入的id，获得id对象对应的EditText组件对象，并返回该对象
	 * 
	 * @param id
	 *            传入系统或用户之定义的Id，格式：R.xx.xxx
	 * @return 返回传入Id所对应的EditText组件对象
	 */
	public EditText etById(int id) {
		return (EditText) findViewById(id);
	}

	/**
	 * 获取按钮——通过传入的id，获得id对象对应的ImageView组件对象，并返回该对象
	 * 
	 * @param id
	 *            传入系统或用户之定义的Id，格式：R.xx.xxx
	 * @return 返回传入Id所对应的ImageView组件对象
	 */
	public ImageView imageById(int id) {
		return (ImageView) findViewById(id);
	}

	/**
	 * 获取按钮——通过传入的id，获得id对象对应的LinearLayout组件对象，并返回该对象
	 * 
	 * @param id
	 *            传入系统或用户之定义的Id，格式：R.xx.xxx
	 * @return 返回传入Id所对应的LinearLayout组件对象
	 */
	public LinearLayout linearById(int id) {
		return (LinearLayout) findViewById(id);
	}
}

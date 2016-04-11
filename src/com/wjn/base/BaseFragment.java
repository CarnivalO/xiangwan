package com.wjn.base;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseFragment extends Fragment {
	/**
	 * 通过父类中的getView()方法返回的view布局，在findViewById(),通过传入的id
	 * 拿到布局中的组件
	 * @param id 要实例化的组件id
	 * @return 返回view类型，可以调用此方法后向下转型
	 */
	public View findViewById(int id){
		return getView().findViewById(id);
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

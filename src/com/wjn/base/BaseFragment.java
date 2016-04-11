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
	 * ͨ�������е�getView()�������ص�view���֣���findViewById(),ͨ�������id
	 * �õ������е����
	 * @param id Ҫʵ���������id
	 * @return ����view���ͣ����Ե��ô˷���������ת��
	 */
	public View findViewById(int id){
		return getView().findViewById(id);
	}
	
	/**
	 * ��ȡ��ť����ͨ�������id�����id�����Ӧ��Button������󣬲����ظö���
	 * 
	 * @param id
	 *            ����ϵͳ���û�֮�����Id����ʽ��R.xx.xxx
	 * @return ���ش���Id����Ӧ��Button�������
	 */
	public Button butById(int id) {
		return (Button) findViewById(id);
	}

	/**
	 * ��ȡ�ı��򡪡�ͨ�������id�����id�����Ӧ��TextView������󣬲����ظö���
	 * 
	 * @param id
	 *            ����ϵͳ���û�֮�����Id����ʽ��R.xx.xxx
	 * @return ���ش���Id����Ӧ��TextView�������
	 */
	public TextView tvById(int id) {
		return (TextView) findViewById(id);
	}

	/**
	 * ��ȡ��ť����ͨ�������id�����id�����Ӧ��EditText������󣬲����ظö���
	 * 
	 * @param id
	 *            ����ϵͳ���û�֮�����Id����ʽ��R.xx.xxx
	 * @return ���ش���Id����Ӧ��EditText�������
	 */
	public EditText etById(int id) {
		return (EditText) findViewById(id);
	}

	/**
	 * ��ȡ��ť����ͨ�������id�����id�����Ӧ��ImageView������󣬲����ظö���
	 * 
	 * @param id
	 *            ����ϵͳ���û�֮�����Id����ʽ��R.xx.xxx
	 * @return ���ش���Id����Ӧ��ImageView�������
	 */
	public ImageView imageById(int id) {
		return (ImageView) findViewById(id);
	}

	/**
	 * ��ȡ��ť����ͨ�������id�����id�����Ӧ��LinearLayout������󣬲����ظö���
	 * 
	 * @param id
	 *            ����ϵͳ���û�֮�����Id����ʽ��R.xx.xxx
	 * @return ���ش���Id����Ӧ��LinearLayout�������
	 */
	public LinearLayout linearById(int id) {
		return (LinearLayout) findViewById(id);
	}
}

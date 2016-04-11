package com.wyl.adapter;

import java.util.List;

import com.wyl.base.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	private List<BaseFragment> fragments;
	public MyFragmentPagerAdapter(FragmentManager fm,List<BaseFragment>fragments) {
		// TODO Auto-generated constructor stub
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}

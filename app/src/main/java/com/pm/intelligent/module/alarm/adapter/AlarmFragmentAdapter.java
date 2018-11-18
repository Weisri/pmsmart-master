package com.pm.intelligent.module.alarm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * Created by WeiSir on 2018/8/28.
 */
public class AlarmFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    public AlarmFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public AlarmFragmentAdapter(FragmentManager fm, List<Fragment> mFragmentList, List<String> mTitles) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}

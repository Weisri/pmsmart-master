package com.pm.intelligent.module.faultTracking.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by pumin on 2018/8/11.
 */

public class FaultTrackingFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private List<String> mTitles;

    public FaultTrackingFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public FaultTrackingFragmentAdapter(FragmentManager fm, List<Fragment> mFragmentList, List<String> mTitles) {
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

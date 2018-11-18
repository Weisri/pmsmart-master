package com.pm.intelligent.module.alarm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.Contants;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.module.alarm.AlarmInfoFragment;
import com.pm.intelligent.module.alarm.adapter.AlarmFragmentAdapter;
import com.pm.intelligent.module.alarm.dao.AlarmInfoDao;
import com.pm.intelligent.utils.SPUtils;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.intelligent.widget.LoadingDialog;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.wall_e.multiStatusLayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by WeiSir on 2018/7/4.
 */

public class AlarmInfoActivity extends BaseActivity implements
        BaseToolBar.MyToolBarLeftBtnListener {
    @BindView(R.id.home_toolbar)
    BaseToolBar mToolbar;
    @BindView(R.id.tab_top)
    TabLayout mTabLayout;
    @BindView(R.id.vp_content)
    ViewPager mViewPager;
    @BindView(R.id.status_layout)
    MultiStatusLayout statusLayout;
    private List<String> mTabTitleList = new ArrayList<>();
    private List<Fragment> mFragment = new ArrayList<>();


    @Override
    protected int setLayoutId() {
        return R.layout.alarm_activity_layout;
    }

    @Override
    protected void findViews() {
        mContext = this;
    }

    @Override
    protected void initViews() {
        mToolbar.setMyToolBarLeftBtnClick(this);
        mToolbar.setTitle("实时报警");

        mFragment = new ArrayList<>();
        mTabTitleList.add("待处理");
        mTabTitleList.add("处理中");
        mTabTitleList.add("已处理");
        for (int i = 0; i < mTabTitleList.size(); i++) {
            Fragment fuyong = AlarmInfoFragment.getFuYong(mTabTitleList.get(i));
            mFragment.add(fuyong);
        }
        AlarmFragmentAdapter alarmFragmentAdapter = new AlarmFragmentAdapter(getSupportFragmentManager(), mFragment, mTabTitleList);
        mViewPager.setAdapter(alarmFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initDatas() {
        doRequestData();
    }

    /**
     * Do request data.
     */
    public void doRequestData() {
        statusLayout.showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getInstance(this).getUserToken());
        AlarmInfoDao.doRequsetAlarm(Contants.ALARM, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                statusLayout.showContent();
            }

            @Override
            public void failed(int error, Object obj) {
                statusLayout.showError();
                super.failed(error, obj);
            }
        });
    }


    @Override
    public void imageLeftBtnclick() {
        finish();
    }

    public static void actionStart(Context context, int type) {
        Intent intent = new Intent(context, AlarmInfoActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    private int mNetStatus;

    @Override
    public void onNetChange(int netWorkState) {
        this.mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            ToastUtils.showLong(this, "当前网络不可用");
        }
    }
}

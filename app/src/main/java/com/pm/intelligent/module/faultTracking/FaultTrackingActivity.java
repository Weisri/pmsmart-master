package com.pm.intelligent.module.faultTracking;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.pm.intelligent.Contants;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.module.faultTracking.adapter.FaultTrackingFragmentAdapter;
import com.pm.intelligent.module.faultTracking.dao.FaultTrackingDao;
import com.pm.intelligent.utils.SPUtils;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.wall_e.multiStatusLayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by pumin on 2018/8/11.
 */

public class FaultTrackingActivity extends BaseActivity implements View.OnClickListener,
        BaseToolBar.MyToolBarLeftBtnListener,
        TabLayout.OnTabSelectedListener {
    @BindView(R.id.home_toolbar)
    BaseToolBar homeToolbar;
    @BindView(R.id.tab_top)
    TabLayout tabTop;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.status_layout)
    MultiStatusLayout statusLayout;

    private List<Fragment> mFragmentList;
    private List<String> mList = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.act_fault_tracking;
    }

    @Override
    protected void findViews() {
        initEvent();
    }

    private void initEvent() {
        homeToolbar.setMyToolBarLeftBtnClick(this);
        tabTop.setOnTabSelectedListener(this);
    }


    @Override
    protected void initViews() {

        doRequestData();

        mFragmentList = new ArrayList<>();
        mList.add("未修复");
        mList.add("修复中");
        mList.add("已修复");
        for (int i = 0; i < mList.size(); i++) {
            Fragment fuyong = NoRepairFragment.getFuYong(mList.get(i));
            mFragmentList.add(fuyong);
        }
        FaultTrackingFragmentAdapter adapter = new FaultTrackingFragmentAdapter(getSupportFragmentManager(), mFragmentList, mList);
        vpContent.setAdapter(adapter);
        tabTop.setupWithViewPager(vpContent);
    }

    @Override
    protected void initDatas() {
    }


    public void doRequestData() {
        statusLayout.showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getInstance(this).getUserToken());
        FaultTrackingDao.doRequestFaultTrack(Contants.FALSE, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                statusLayout.showContent();
            }

            @Override
            public void failed(int error, Object obj) {
                statusLayout.showContent();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            default:
                break;
        }
    }

    @Override
    public void imageLeftBtnclick() {
        finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}

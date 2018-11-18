package com.pm.intelligent.module.hardware;

import android.widget.ExpandableListView;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.Contants;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.module.hardware.dao.HardWareDao;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.SPUtils;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.wall_e.multiStatusLayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by WeiSir on 2018/7/6.
 */

public class HardWareCheckActivity extends BaseActivity implements
        BaseToolBar.MyToolBarLeftBtnListener,
        ExpandableListView.OnGroupExpandListener {

    @BindView(R.id.toolbar)
    BaseToolBar mToolbar;
    @BindView(R.id.expand_list)
    ExpandableListView mExpandList;
    @BindView(R.id.status_layout_box)
    MultiStatusLayout statusLayoutBox;


    private List<HardWareGroupBean> mGroupList = new ArrayList<>();
    private List<List> mChildList = new ArrayList<>();
    private Map<String, List<HardWareItemBean>> datasets = new HashMap<>();
    private MyExpandableListAdapter mExAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.hardware_activity_layout;
    }

    @Override
    protected void findViews() {
        mContext = this;
    }

    @Override
    protected void initViews() {
        mToolbar.setMyToolBarLeftBtnClick(this);
//        getDatas();
    }


    @Override
    protected void initDatas() {
        mExpandList.setOnGroupExpandListener(this);
        doRequsetData();
    }

    /***
     * 请求数据
     */
    private void doRequsetData() {
        statusLayoutBox.showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("iccid", CommonUtils.iccid);  //TODO iccid
        params.put("token", SPUtils.getInstance(this).getUserToken());
        HardWareDao.doRequsetHard(Contants.HARDWARES, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
//                datasets = (Map<String, List<HardWareItemBean>>) obj;
                mGroupList = (List<HardWareGroupBean>) obj;
                Logger.d(mGroupList);
                if (mGroupList.size() == 0) {
                    statusLayoutBox.showEmpty();
                } else {

//                映射group  item
                    Map<String, List<HardWareItemBean>> datasets = new HashMap<>();
                    String[] pList = new String[mGroupList.size()];

                    for (int j = 0; j < pList.length; j++) {
                        pList[j] = mGroupList.get(j).getHardwareName();
                        for (int k = 0; k <mGroupList.get(j).getHardwareParamVos().size(); k++) {    // TODO: 2018/10/9 下标越界
                            datasets.put(pList[j], mGroupList.get(j).getHardwareParamVos());//映射合集
                        }
                    }
                    mExAdapter = new MyExpandableListAdapter(HardWareCheckActivity.this, datasets, pList);
                    mExpandList.setAdapter(mExAdapter);
                    mExAdapter.notifyDataSetChanged();
                    statusLayoutBox.showContent();
                }
            }

            @Override
            public void failed(int error, Object obj) {
                super.failed(error, obj);
                ToastUtils.showShort(HardWareCheckActivity.this, "数据加载失败");
                statusLayoutBox.showError();
            }
        });
    }


    @Override
    public void imageLeftBtnclick() {
        finish();
    }

    /**
     * 收起其它group
     * @param groupPosition
     */
    @Override
    public void onGroupExpand(int groupPosition) {
        for (int i = 0; i < mGroupList.size(); i++) {
            if (i != groupPosition) {
                mExpandList.collapseGroup(i);
            }
        }
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

package com.pm.intelligent.module.system;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.pm.intelligent.Contants;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.base.CommonRecycleViewAdapter;
import com.pm.intelligent.bean.SystemDataBean;
import com.pm.intelligent.module.system.dao.SystemReportDao;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.SPUtils;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.wall_e.multiStatusLayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by WeiSir on 2018/7/12.
 */

public class SystemActivity extends BaseActivity
        implements BaseToolBar.MyToolBarLeftBtnListener {
    @BindView(R.id.home_toolbar)
    BaseToolBar mToolbar;
    @BindView(R.id.lv_box_leackage)
    RecyclerView rvBoxLeackage;
    @BindView(R.id.status_layout_box)
    MultiStatusLayout statusLayout;
    private List<SystemDataBean> mDataList = new ArrayList<>();


    @Override
    protected int setLayoutId() {
        return R.layout.leackage_activty_layout;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initViews() {
        mToolbar.setMyToolBarLeftBtnClick(this);
        rvBoxLeackage.setLayoutManager(new LinearLayoutManager(SystemActivity.this, LinearLayoutManager.VERTICAL, false));

    }


    @Override
    protected void initDatas() {
        statusLayout.showLoading();
        HashMap<String, String> params = new HashMap<>();
        String token = SPUtils.getInstance(this).getUserToken();
        params.put("token", token);
        params.put("iccid", CommonUtils.iccid);//TODO  Commonutil.iccid
        SystemReportDao.doSystemRequest(Contants.SYSTEM, params, new OkCallBack() {
            @Override
            public void success(Object obj) {

                mDataList = (List<SystemDataBean>) obj;
                if (mDataList.isEmpty()) {
                    statusLayout.showEmpty();
                } else {
                    boxLeackageAdapter.replaceData(mDataList);
                    rvBoxLeackage.setAdapter(boxLeackageAdapter);
                    boxLeackageAdapter.notifyDataSetChanged();
                    statusLayout.showContent();
                }
            }

            @Override
            public void failed(int error, Object obj) {
                super.failed(error, obj);
                statusLayout.showError();
            }

        });
    }


    CommonRecycleViewAdapter boxLeackageAdapter = new CommonRecycleViewAdapter<SystemDataBean>(R.layout.system_result_list_item, mDataList) {
        @Override
        protected void convert(BaseViewHolder helper, SystemDataBean item) {

            TextView tv2 = helper.getView(R.id.tv_system_status);
            if (item.getSystemStatus() == 1) {
                tv2.setTextColor(mContext.getResources().getColor(R.color.text_red));
                helper.setText(R.id.tv_system_status, "异常");
            } else {
                tv2.setTextColor(mContext.getResources().getColor(R.color.text_black));
                helper.setText(R.id.tv_system_status, "正常");
            }
            helper.setText(R.id.tv_system_name, item.getSystemName());
        }
    };

    private int mNetStatus;

    @Override
    public void onNetChange(int netWorkState) {
        super.onNetChange(netWorkState);
        mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            statusLayout.showNetError();
        } else {
            statusLayout.showContent();
        }

    }

    @Override
    public void imageLeftBtnclick() {
        finish();
    }


}

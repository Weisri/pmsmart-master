package com.pm.intelligent.module.faultTracking;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.base.CommonRecycleViewAdapter;
import com.pm.intelligent.bean.FaultDetailBean;
import com.pm.intelligent.bean.FaultTrackEntity;
import com.pm.intelligent.module.faultTracking.bean.FaultInfo;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.NetUtils;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.intelligent.widget.LineView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pumin on 2018/8/13.
 */

public class NoRepairDetailActivity extends BaseActivity implements BaseToolBar.MyToolBarLeftBtnListener {
    @BindView(R.id.home_toolbar)
    BaseToolBar homeToolbar;
    @BindView(R.id.tv_alarm_head)
    TextView tvFault;
    @BindView(R.id.rv_alarm_detail)
    RecyclerView rvFaultDetail;
    @BindView(R.id.iv_call_tab)
    ImageView ivCallTab;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_call)
    ImageView ivCall;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    private FaultTrackEntity mFaultInfo;
    private int mNetStatus;

    @Override
    protected int setLayoutId() {
        return R.layout.act_fault_tracking_detail;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initViews() {
        homeToolbar.setMyToolBarLeftBtnClick(this);
        rvFaultDetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvFaultDetail.setAdapter(faultDetailAdapter);

    }

    @Override
    protected void initDatas() {
        mFaultInfo = (FaultTrackEntity) getIntent().getSerializableExtra("faultInfoBean");
        tvFault.setText(mFaultInfo.getShelterName() +mFaultInfo.getTroubleName());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        if (mFaultInfo.getTroubleStatus()==0) {
            if (mFaultInfo.getTroubleTime()!=null) {
                String time = format.format(mFaultInfo.getTroubleTime());
                mDataList.add(new FaultDetailBean("您的故障信息已收到，待修复",time));
            }
            faultDetailAdapter.notifyDataSetChanged();
        }
        if (mFaultInfo.getTroubleStatus() == 1) {
            if (mFaultInfo.getUpdateTime()!=null) {
                String time = format.format(mFaultInfo.getUpdateTime());
                mDataList.add(new FaultDetailBean("您的故障信息已收到，正在修复中",time));
                mDataList.add(new FaultDetailBean("您的故障信息已收到，待修复",time));
            }
            faultDetailAdapter.notifyDataSetChanged();
        }
        if (mFaultInfo.getTroubleStatus()==2) {
            if (mFaultInfo.getTroubleTime()!=null) {
                String time = format.format(mFaultInfo.getTroubleTime());
                mDataList.add(new FaultDetailBean("您的故障信息已收到，已修复",time));
                mDataList.add(new FaultDetailBean("您的故障信息已收到，正在修复中",time));
                mDataList.add(new FaultDetailBean("您的故障信息已收到，待修复",time));
            }
            faultDetailAdapter.notifyDataSetChanged();
        }
    }

    private List<FaultDetailBean> mDataList = new ArrayList<>();
    CommonRecycleViewAdapter faultDetailAdapter = new CommonRecycleViewAdapter<FaultDetailBean>(R.layout.alarminfo_list_item, mDataList) {
        @Override
        protected void convert(BaseViewHolder helper, FaultDetailBean item) {
            int position = helper.getLayoutPosition();
            LineView view = helper.getView(R.id.iv_indicator);
            helper.setText(R.id.tv_info_detail, mDataList.get(position).getTvInfo());
            helper.setText(R.id.tv_data, mDataList.get(position).getTvTime());
            if (position == 0) {
                view.setLineMargin(0, CommonUtils.dip2px(mContext, 14), 0, 0);
            }

            if (position == 2) {
                view.setLineMargin(0, 0, 0, CommonUtils.dip2px(mContext, 14));
            }
        }
    };

    @Override
    public void imageLeftBtnclick() {
        finish();
    }

    @Override
    public void onNetChange(int netWorkState) {
        super.onNetChange(netWorkState);
        if (mNetStatus == -1) {
            ToastUtils.showLong(this, "当前网络不可用");
        }
    }


    @OnClick({R.id.iv_call_tab, R.id.iv_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_call_tab:
                if (!llContent.isShown()) {
                    llContent.setVisibility(View.VISIBLE);
                }else
                    llContent.setVisibility(View.GONE);
                break;
            case R.id.iv_call:
                callPhone("0755-28024057");
                break;
        }
    }
    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

}

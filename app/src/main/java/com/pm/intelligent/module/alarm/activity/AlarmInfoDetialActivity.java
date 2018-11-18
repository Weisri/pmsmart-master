package com.pm.intelligent.module.alarm.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.base.CommonRecycleViewAdapter;
import com.pm.intelligent.bean.AlarmDetialBean;
import com.pm.intelligent.bean.AlarmInfoEntity;
import com.pm.intelligent.bean.FaultDetailBean;
import com.pm.intelligent.module.alarm.adapter.AlarmInfoDetialsAapter;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.Log;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.intelligent.widget.LineView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by WeiSir on 2018/7/16.
 */

public class AlarmInfoDetialActivity extends BaseActivity implements
        BaseToolBar.MyToolBarLeftBtnListener {
    @BindView(R.id.home_toolbar)
    BaseToolBar mToolbar;
    @BindView(R.id.rv_alarm_detail)
    RecyclerView rvAlarmDetail;
    @BindView(R.id.tv_alarm_head)
    TextView mAlarmHead;
    private List<AlarmDetialBean> mInfoBeanList = new ArrayList<>();
    private AlarmInfoEntity mInfoBean;
    private AlarmInfoDetialsAapter mDetialAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.alarm_list_item_info_detial;
    }

    @Override
    protected void findViews() {
        mContext =this;

    }

    @Override
    protected void initViews() {
        mToolbar.setMyToolBarLeftBtnClick(this);
        rvAlarmDetail.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvAlarmDetail.setAdapter(alarmDetailAdapter);

    }

    @Override
    protected void initDatas() {
        mInfoBean = (AlarmInfoEntity) getIntent().getSerializableExtra("alarmInfoBean");
        Log.i(mInfoBean);
        mAlarmHead.setText(mInfoBean.getShelterName()+":"+mInfoBean.getAlarmName());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        if (mInfoBean.getAlarmStatus()==0) {
            String time = format.format(mInfoBean.getAlarmTime());
            mDataList.add(new FaultDetailBean("您的故障信息已收到，待修复",time));
            alarmDetailAdapter.notifyDataSetChanged();
        }
        if (mInfoBean.getAlarmStatus() == 1) {
            String time = format.format(mInfoBean.getUpdateTime());
            mDataList.add(new FaultDetailBean("您的故障信息已收到，正在修复中",time));
            mDataList.add(new FaultDetailBean("您的故障信息已收到，待修复",time));
            alarmDetailAdapter.notifyDataSetChanged();
        }
        if (mInfoBean.getAlarmStatus()==2) {
            String time = format.format(mInfoBean.getAlarmTime());
            mDataList.add(new FaultDetailBean("您的故障信息已收到，已修复",time));
            mDataList.add(new FaultDetailBean("您的故障信息已收到，正在修复中",time));
            mDataList.add(new FaultDetailBean("您的故障信息已收到，待修复",time));
            alarmDetailAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void imageLeftBtnclick() {
        finish();
    }


    private List<FaultDetailBean> mDataList = new ArrayList<>();
    CommonRecycleViewAdapter alarmDetailAdapter = new CommonRecycleViewAdapter<FaultDetailBean>(R.layout.alarminfo_list_item,mDataList){
        @Override
        protected void convert(BaseViewHolder helper, FaultDetailBean item) {
            int position = helper.getLayoutPosition();
            LineView view = helper.getView(R.id.iv_indicator);

            if (position == 0){
                view.setLineMargin(0, CommonUtils.dip2px(mContext,14),0,0);
            }

            if (position == 2){
                view.setLineMargin(0, 0,0,CommonUtils.dip2px(mContext,14));
            }




        }
    };

    private int mNetStatus;
    @Override
    public void onNetChange(int netWorkState) {
        this.mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            ToastUtils.showLong(this,"当前网络不可用");
        }
    }

}

package com.pm.intelligent.module.patrol.activity;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.bean.SpotPatrolBean;
import com.pm.intelligent.module.alarm.activity.AlarmInfoActivity;
import com.pm.intelligent.module.faultTracking.FaultTrackingActivity;
import com.pm.intelligent.module.patrol.adapter.SpotPatrolRecyclerAdapter;
import com.pm.intelligent.utils.ActivityUtil;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.ThreadTask;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.intelligent.widget.MySinkingView;
import com.pm.mc.chat.netty.pojo.Msg;
import com.pm.nettyService.IOutLinkStatusCallBack;
import com.pm.nettyService.IOutMsgReCallBack;
import com.pm.nettyService.NettyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 78122 on 2018/8/20.
 */

public class SinglePatroActivity extends BaseActivity implements
        BaseToolBar.MyToolBarLeftBtnListener, IOutMsgReCallBack<Msg.SpotCheckMessage>, IOutLinkStatusCallBack {
    @BindView(R.id.toolbar)
    BaseToolBar toolbar;
    @BindView(R.id.tv_patrol_station)
    TextView tvPatrolStation;
    @BindView(R.id.sinking)
    MySinkingView mSinkingView;
    @BindView(R.id.my_waveview)
    LinearLayout myWaveview;
    @BindView(R.id.btn_start)
    TextView btnStart;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.rec_popu_list)
    RecyclerView recPopuList;
    private SpotPatrolRecyclerAdapter mRecAdapter;
    private List<SpotPatrolBean> mResults = new ArrayList<>();
    private List<SpotPatrolBean> mDatasCopy = new ArrayList<>();
    private boolean mFalg = true;
    private ImageView mDun;
    private float percent = 0;
    private NettyUtil mNettyUtil;
    private boolean mNettyStatus;

    @Override
    protected int setLayoutId() {
        return R.layout.patrol_activity_single_patrol;
    }

    @Override
    protected void findViews() {
        mDun = this.findViewById(R.id.dun);
    }

    @Override
    protected void initViews() {
        toolbar.setMyToolBarLeftBtnClick(this);
        tvPatrolStation.setText(CommonUtils.shelterName);
    }

    @Override
    protected void initDatas() {
        mNettyUtil = NettyUtil.with(Msg.SpotCheckMessage.class).setCallBack(this);
    }

    @OnClick({R.id.btn_start, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (mNettyStatus) {
                    ToastUtils.showShort(this, "netty未连接");
                } else {
                    recPopuList.setVisibility(View.VISIBLE);
                    btnStart.setVisibility(View.GONE);
//                    btnCancel.setVisibility(View.VISIBLE);
                    mDun.setVisibility(View.GONE);
                    mSinkingView.setVisibility(View.VISIBLE);
                    percent = 0.00f;
                    mSinkingView.setPercent(percent);
                    sendHandlerMsg();
                    doPatrol();
                }
                break;
            case R.id.btn_cancel:
                mDun.setVisibility(View.VISIBLE);
                mSinkingView.setVisibility(View.GONE);
                btnCancel.setText("查看");
                if (btnCancel.getText().toString().equals("开始监测")) {
                    ToastUtils.show(this, "不能重复监测", Toast.LENGTH_SHORT);
                }
                if (btnCancel.getText().toString().equals("查看")) {
                    ActivityUtil.INSTANCE.startActivity(FaultTrackingActivity.class);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发送netty指令
     */
    private void sendHandlerMsg() {
        mNettyUtil.sendMsg(Msg.Message.newBuilder()
                .setMessageType(Msg.MessageType.SPOT_CHECK).
                        setSpotCheckMessage(Msg.SpotCheckMessage.newBuilder()
                                .setCheckOrder(0)
                                .setIccid("10001")
                                .build())
                .build());
    }

    @Override
    public void messageReceived(Msg.SpotCheckMessage msg) {

        try {
            Logger.t("点检结果").d(msg.toString());
            List<Msg.SpotCheck> checkList = msg.getSpotChecksList();
            for (int i = 0; i < checkList.size(); i++) {
                SpotPatrolBean spotPatrolBean = new SpotPatrolBean();
                spotPatrolBean.setCheckStatus(checkList.get(i).getCheckStatus());
                spotPatrolBean.setHardwareType(checkList.get(i).getHardwareType());
                spotPatrolBean.setIccid(msg.getIccid());
                spotPatrolBean.setShelterName(msg.getShelterName());
                mResults.add(spotPatrolBean);
            }
            Logger.d(mResults.toString());
            test();
            mRecAdapter.setDataList(mDatasCopy);
            mRecAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(SinglePatroActivity.this, "错误");
        }
    }


    Timer timer = new Timer(true);

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

            count++;
            percent = count / (float) mMaxCount;
            Logger.e("   ==   " + percent);
            mSinkingView.setPercent(percent);
            if (!mResults.isEmpty()) {
                if (count != 0 && count % getPercentValue(mResults.size()) == 0) {

                    if (index < mResults.size()) {

                        mDatasCopy.add(mResults.get(index));
                        index++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecAdapter.notifyDataSetChanged();
                                recPopuList.smoothScrollToPosition(mDatasCopy.size());
                            }
                        });
                    }
                }
            }

            if (count == mMaxCount) {
                Logger.i("ahahahahahahahahahah");
                mSinkingView.complete();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnCancel.setVisibility(View.VISIBLE);
                        btnCancel.setText("查看");
                    }
                });

                timer.cancel();
            }

        }
    };

    int count;
    int index;
    private int mMaxCount;

    private int getMaxCount() {
        int maxCount = mResults.size() / 100;
        maxCount++;
        return maxCount * 100;
    }


    private int getPercentValue(int size) {

        int value = mMaxCount / size;

        return value;
    }


    private void test() {

        mMaxCount = getMaxCount();

        timer.schedule(timerTask, 0, 30);


    }

    private void doPatrol() {
        recPopuList.setHasFixedSize(false);
        recPopuList.setItemAnimator(new DefaultItemAnimator());
        mRecAdapter = new SpotPatrolRecyclerAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recPopuList.setLayoutManager(manager);
        recPopuList.setAdapter(mRecAdapter);
    }


    @Override
    public void imageLeftBtnclick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNettyUtil.unBind();
    }

    @Override
    public void nettyLinkStatus(boolean status) {
        mNettyStatus = status;
    }
}

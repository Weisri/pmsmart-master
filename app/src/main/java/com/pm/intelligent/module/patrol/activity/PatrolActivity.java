package com.pm.intelligent.module.patrol.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.bean.HomeShelterEntity;
import com.pm.intelligent.bean.PatrolResultBean;
import com.pm.intelligent.greendao.HomeShelterEntityDao;
import com.pm.intelligent.module.faultTracking.FaultTrackingActivity;
import com.pm.intelligent.module.patrol.adapter.PollingPatrolRecAdapter;
import com.pm.intelligent.module.patrol.adapter.SpotPatrolRecyclerAdapter;
import com.pm.intelligent.utils.ActivityUtil;
import com.pm.intelligent.utils.ThreadTask;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.utils.rxUtil.RxBus;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.intelligent.widget.LoadFinshCallBack;
import com.pm.intelligent.widget.MySinkingView;
import com.pm.mc.chat.netty.pojo.Msg;
import com.pm.mc.chat.netty.pojo.PollingCheck;
import com.pm.nettyService.IOutMsgReCallBack;
import com.pm.nettyService.IOutLinkStatusCallBack;
import com.pm.nettyService.NettyService;
import com.pm.nettyService.NettyUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by WeiSir on 2018/7/9.
 */

public class PatrolActivity extends BaseActivity implements
        BaseToolBar.MyToolBarLeftBtnListener, Handler.Callback, IOutLinkStatusCallBack {
    @BindView(R.id.toolbar)
    BaseToolBar mToolbar;
    @BindView(R.id.my_waveview)
    LinearLayout myWaveview;
    @BindView(R.id.btn_start)
    TextView btnStart;
    @BindView(R.id.rec_popu_list)
    RecyclerView recPopuList;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    private PollingPatrolRecAdapter mRecAdapter;
    private List<PatrolResultBean> mDatas = new ArrayList<>();
    private List<PatrolResultBean> mDatasCopy = new ArrayList<>();
    private boolean mFalg = true;
    private MySinkingView mSinkingView;
    private ImageView mDun;
    private NettyUtil mNettyUtil;
    private int mNetStatus;
    private boolean mNettyStatus;
    private boolean mFinish = false;
    //水位百分比
    private float percent = 0;

    @Override
    protected int setLayoutId() {
        return R.layout.patrol_activity_patroling_layout;
    }

    @Override
    protected void findViews() {
        mContext = this;
        mSinkingView = this.findViewById(R.id.sinking);
        mDun = this.findViewById(R.id.dun);
    }

    @Override
    protected void initViews() {
        mToolbar.setMyToolBarLeftBtnClick(this);
    }

    @Override
    protected void initDatas() {
        mNettyUtil = NettyUtil.with(Msg.PollingCheckMessage.class).setCallBack(new IOutMsgReCallBack<Msg.PollingCheckMessage>() {
            @Override
            public void messageReceived(Msg.PollingCheckMessage msg) {
                Logger.d("巡检结果-->" + msg.toString());
                List<Msg.PollingCheck> pollingCheckList = msg.getPollingChecksList();
                for (int i = 0; i < pollingCheckList.size(); i++) {
                    PatrolResultBean resultBean = new PatrolResultBean();
                    resultBean.setmStationName(pollingCheckList.get(i).getShelterName());
                    resultBean.setCheckStatus(pollingCheckList.get(i).getCheckStatus());
                    resultBean.setIccid(pollingCheckList.get(i).getIccid());
                    mDatas.add(resultBean);
                }
                test();

                Logger.d("mDatas" + mDatas.toString());
//                mRecAdapter.notifyDataSetChanged();

            }
        });
    }

    private void doPatrol() {

        recPopuList.setHasFixedSize(false);
        recPopuList.setItemAnimator(new DefaultItemAnimator());
        mRecAdapter = new PollingPatrolRecAdapter(this, mDatasCopy);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recPopuList.setLayoutManager(manager);
        recPopuList.setAdapter(mRecAdapter);
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
                    doPatrol();
                    sendNettyCode();
                }
                break;
            case R.id.btn_cancel:
                mDun.setVisibility(View.VISIBLE);
                mSinkingView.setVisibility(View.GONE);
//                btnCancel.setText("开始检测");
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
     * 发送站牌ICCID
     * 巡检指令： 1
     */
    private void sendNettyCode() {
        mSinkingView.setPercent(0.01f);
        List<Msg.PollingCheck> iccidList = new ArrayList<>();
        HomeShelterEntityDao shelterEntityDao = MyApplication.getDaoSession().getHomeShelterEntityDao();
        for (HomeShelterEntity entity : shelterEntityDao.loadAll()) {
            iccidList.add(Msg.PollingCheck.newBuilder()
                    .setIccid(entity.getIccid())
                    .build()
            );
        }
        Logger.d(iccidList.toString());
        mNettyUtil.sendMsg(Msg.Message.newBuilder()
                .setMessageType(Msg.MessageType.POLLING_CHECK)
                .setPollingCheckMessage(Msg.PollingCheckMessage.newBuilder()
                        .setCheckOrder(1)
                        .addAllPollingChecks(iccidList)
                        .build())
                .build());
    }


    @Override
    public void imageLeftBtnclick() {
        finish();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    Timer timer = new Timer(true);

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

            count++;

            percent = count / (float) mMaxCount;

            mSinkingView.setPercent(percent);

            if (mDatas.size() != 0) {
                if (count != 0 && count % getPercentValue(mDatas.size()) == 0) {

                    if (index < mDatas.size()) {

                        mDatasCopy.add(mDatas.get(index));
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
                mFinish = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mFinish) {
                            btnCancel.setVisibility(View.VISIBLE);
                            btnCancel.setText("查看");
                        }
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
        int maxCount = mDatas.size() / 100;
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

    @Override
    public void onNetChange(int netWorkState) {
        this.mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            ToastUtils.showLong(this, "当前网络不可用");
        }
    }

    @Override
    public void nettyLinkStatus(boolean status) {
        Logger.d(status);
        mNettyStatus = status;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNettyUtil.unBind();
    }

}

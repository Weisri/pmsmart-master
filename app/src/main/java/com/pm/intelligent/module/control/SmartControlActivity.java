package com.pm.intelligent.module.control;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.Contants;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.bean.AdjustsEntity;
import com.pm.intelligent.bean.SeatHeatBean;
import com.pm.intelligent.bean.SwitchsEntity;
import com.pm.intelligent.module.control.dao.SeatHeatDao;
import com.pm.intelligent.utils.AESUtil;
import com.pm.intelligent.utils.AppUtils;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.DateUtils;
import com.pm.intelligent.utils.SPUtils;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.utils.net.API;
import com.pm.intelligent.utils.net.RetrofitUtil;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.intelligent.widget.LoadingDialog;
import com.pm.mc.chat.netty.pojo.Msg;
import com.pm.nettyService.IOutMsgReCallBack;
import com.pm.nettyService.NettyUtil;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.wall_e.multiStatusLayout.MultiStatusLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by WeiSir on 2018/7/6.
 */

public class SmartControlActivity extends BaseActivity implements
        BaseToolBar.MyToolBarLeftBtnListener, IOutMsgReCallBack {

    private static final String TAG = "SmartControlActivity";
    @BindView(R.id.toolbar)
    BaseToolBar mToolbar;
    @BindView(R.id.expand_list)
    ExpandableListView mExpandList;
    @BindView(R.id.btn_reset_control)
    TextView btnResetControl;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.status_layout)
    MultiStatusLayout statusLayout;

    private List<String> mGroupDatas = new ArrayList<>();
    private List<List> mChildList = new ArrayList<>();
    private List<List> mChildListCopy = new ArrayList<>();
    private ControlExpandLisAdapter mExAdapter;
    private NettyUtil mNettyUtil;
    private String mIccid;
    private MyTimerTask timerTask;
    private List<SeatHeatBean> mHeatBenanList = new ArrayList<>();


    @Override
    protected int setLayoutId() {
        return R.layout.control_activity_smartcontrol_layout;
    }

    @Override
    protected void findViews() {

    }


    @Override
    protected void initViews() {
        Logger.d("iccid" + CommonUtils.iccid);
        initSet();
    }


    private void initSet() {
        doRequestData(false);

        mToolbar.setMyToolBarLeftBtnClick(this);
        mExAdapter = new ControlExpandLisAdapter(this, mGroupDatas, mChildList, mExpandList);
        mExpandList.setAdapter(mExAdapter);
        /**如果加热座椅打开，请求数据*/
//        mExAdapter.setSeatSwitchStatusCallBack(new ControlExpandLisAdapter.SeatSwitchStatusCallBack() {
//            @Override
//            public void isOpen(boolean isOpen) {
////                doRequestSeatHeat();
//                //定时30秒自动请求后台
////                timer.schedule(timerTask, 0, 3000);
////                mExAdapter.notifyDataSetChanged();
//            }
//        });

    }

    /**
     * 请求开关初始化数据
     *
     * @param reset
     */
    public void doRequestData(final boolean reset) {
        statusLayout.showLoading();
        mIccid = CommonUtils.iccid;//todo iccid
        String userToken = SPUtils.getInstance(this).getUserToken();
        API instence = RetrofitUtil.getInstence();
        final Observable<ResponseBody> switchs = instence.getSwitchs(userToken, mIccid);
        final Observable<ResponseBody> adjusts = instence.getAdjusts(userToken, mIccid);
        Disposable subscribe = switchs.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, List<SwitchsEntity>>() {
                    @Override
                    public List<SwitchsEntity> apply(ResponseBody responseBody) throws Exception {
                        String json = responseBody.string();
                        JSONObject object = new JSONObject(json);
                        int status = object.getInt("status");
                        List<SwitchsEntity> entityList = new ArrayList<>();
                        if (status == 200) {
                            String data = object.getString("data");
                            String aesDecrypt = AESUtil.aesDecrypt(data);
                            Logger.d("解密后： " + aesDecrypt);
                            JSONArray array = new JSONArray(aesDecrypt);
                            for (int i = 0; i < array.length(); i++) {
                                SwitchsEntity switchsEntity = AppUtils.getGson().fromJson(array.getString(i), SwitchsEntity.class);
                                entityList.add(switchsEntity);
                            }


                        } else {
                            return entityList;
                        }
                        return entityList;
                    }
                })
                .doOnNext(new Consumer<List<SwitchsEntity>>() {
                    @Override
                    public void accept(List<SwitchsEntity> switchsEntities) throws Exception {
                        if (reset) {
                            mChildListCopy.add(switchsEntities);
                        } else {
                            mChildList.add(switchsEntities);
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<List<SwitchsEntity>, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(List<SwitchsEntity> switchsEntities) throws Exception {
                        return adjusts;
                    }
                })
                .map(new Function<ResponseBody, List<AdjustsEntity>>() {
                    @Override
                    public List<AdjustsEntity> apply(ResponseBody responseBody) throws Exception {
                        String json = responseBody.string();
                        Logger.json(json);

                        JSONObject object = new JSONObject(json);

                        int status = object.getInt("status");
                        List<AdjustsEntity> entityList = new ArrayList<>();
                        if (status == 200) {
//                            JSONArray array = object.getJSONArray("data");
                            String data = object.getString("data");
                            String aesDecrypt = AESUtil.aesDecrypt(data);
                            Logger.d("解密后： " + aesDecrypt);
                            JSONArray array = new JSONArray(aesDecrypt);
                            for (int i = 0; i < array.length(); i++) {
                                AdjustsEntity adjustsEntity = AppUtils.getGson().fromJson(array.getString(i), AdjustsEntity.class);
                                if (!"LED双色屏亮度".equals(adjustsEntity.getHardwareInfo())) {
                                    entityList.add(adjustsEntity);
                                }
//                                entityList.add(adjustsEntity);
                            }
                        } else {
                            return entityList;
                        }
                        return entityList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AdjustsEntity>>() {
                    @Override
                    public void accept(List<AdjustsEntity> adjustsEntities) throws Exception {

                        if (reset) { //重置
                            mChildListCopy.add(adjustsEntities);
                            for (int i = 0; i < mChildList.size(); i++) {
                                List list = mChildList.get(i);
                                List list1 = mChildListCopy.get(i);
                                for (int j = 0; j < list.size(); j++) {
                                    Object o = list.get(j);
                                    Object o1 = list1.get(j);
                                    if (o instanceof SwitchsEntity) {
                                        SwitchsEntity entity = (SwitchsEntity) o;
                                        SwitchsEntity entity1 = (SwitchsEntity) o1;
                                        entity.setSwitchName(entity1.getSwitchName());
                                        entity.setParamState(entity1.getParamState());
                                    }
                                    if (o instanceof AdjustsEntity) {
                                        AdjustsEntity entity = (AdjustsEntity) o;
                                        AdjustsEntity entity1 = (AdjustsEntity) o1;
                                        entity.setHardwareInfo(entity1.getHardwareInfo());
                                        entity.setParamValue(entity1.getParamValue());
                                    }
                                }
                            }
                        } else {
                            mChildList.add(adjustsEntities);
                        }
                        if (adjustsEntities.isEmpty() || adjustsEntities == null) {
                            statusLayout.showEmpty();
                        } else {
                            mExAdapter.setChildData(mChildList);
                            mExAdapter.notifyDataSetChanged();
                            /**如果加热座椅开关打开 请求数据*/
                            if (CommonUtils.isSeatOpen) {
                                doRequestSeatHeat();
                                setTimerTask();
                            }
                            statusLayout.showContent();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.d(throwable.getMessage());
                        statusLayout.showError();
                    }
                });
    }

    /**
     * 加热座椅定时器
     */
    Timer timer = new Timer();
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                doRequestSeatHeat();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setTimerTask( ) {
        //定时
        if (timerTask != null) {
            timerTask.cancel();  //将原任务从队列中移除
            timerTask = new MyTimerTask();
            timer.schedule(timerTask, 0, 6000);
        } else {
            timerTask = new MyTimerTask();
            timer.schedule(timerTask, 0, 6000);
        }

    }

    /**
     * 请求加热座椅的温度数据
     */
    @SuppressLint("CheckResult")
    private void doRequestSeatHeat() {
//        mIccid = CommonUtils.iccid;
//        API instance = RetrofitUtil.getInstence();
//        final Observable<ResponseBody> heatedSeat = instance.heatedSeat();
//        heatedSeat.subscribeOn(Schedulers.io())
//                .map(new Function<ResponseBody, List<SeatHeatBean>>() {
//                    @Override
//                    public List<SeatHeatBean> apply(ResponseBody responseBody) throws Exception {
//                        String obj= responseBody.string();
//                        Logger.json(obj);
//                        JsonObject jsonObject = new JsonObject();
//                        int status = jsonObject.get("status").getAsInt();
//                        List<SeatHeatBean> heatBeanList = new ArrayList<>();
//                        if (status == 200) {
//                            JsonArray array = jsonObject.get("data").getAsJsonArray();
//                            for (int i = 0; i < array.size(); i++) {
//                                SeatHeatBean seatHeatBean = AppUtils.getGson().fromJson(array.get(i).getAsString(),SeatHeatBean.class);
//                                heatBeanList.add(seatHeatBean);
//                            }
//                        }
//                        return heatBeanList;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<SeatHeatBean>>() {
//                    @Override
//                    public void accept(List<SeatHeatBean> seatHeatBeans) throws Exception {
//                        mHeatBenanList = seatHeatBeans;
//                        setSeatHeatChartData();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Logger.d("请求异常。。。"+throwable.getMessage());
//                    }
//                });
        HashMap<String, String> params = new HashMap<>();
        params.put("iccid", "123456");  //TODO iccid
        SeatHeatDao.doRequestSeatHeat(Contants.BASE_URL2, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                mHeatBenanList = (List<SeatHeatBean>) obj;
                Logger.d("温度。。。。。" + obj);
                setSeatHeatChartData();
                super.success(obj);
            }

            @Override
            public void failed(int error, Object obj) {
                super.failed(error, obj);
                Logger.d("请求失败" + obj);
            }
        });

    }

    /**
     * 设置加热座椅折线图数据
     */
    private void setSeatHeatChartData() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        //设置x轴方向上的标签数据
        final List<String> xValsDate = new ArrayList<String>();
        for (int i = 0; i < mHeatBenanList.size(); i++) {
            String time = mHeatBenanList.get(i).getTime();
//            String[] all =time.split(" ");
//            xValsDate.add(all[1]);
            xValsDate.add(mHeatBenanList.get(i).getTime());
        }


        //x轴数据
        List<Float> xAXisDate = new ArrayList<>();
        for (int i = 0; i <mHeatBenanList.size(); i++) {
            xAXisDate.add((float) i);
        }
        //y轴数据
        List<Float> yAXisData = new ArrayList<>();
        for (int i = 0; i < mHeatBenanList.size(); i++) {
            yAXisData.add(Float.valueOf(mHeatBenanList.get(i).getValue()));
        }
        mExAdapter.setxValsDate(xValsDate);
        mExAdapter.setxAXisDate(xAXisDate);
        mExAdapter.setyAXisData(yAXisData);
        mExAdapter.notifyDataSetChanged();

    }


    private void getDatas() {
        mGroupDatas.add("智能开关");
        mGroupDatas.add("智能调节");
    }

    @Override
    protected void initDatas() {
        getDatas();
        mNettyUtil = NettyUtil.with().setCallBack(this);
    }


    @OnClick({R.id.btn_reset_control, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_control:
                doRequestData(true);
                break;
            case R.id.tv_commit:
                String json = AppUtils.getGson().toJson(mExAdapter.getChildList());
                Logger.json(json);
                LoadingDialog.INSTANCE.showDialog(SmartControlActivity.this, "正在设置");
                if (mNettyUtil != null) {
                    if (mNettyUtil.getLinkStatus()) {
                        mNettyUtil.sendMsg(generateSwichMsg());
                        mNettyUtil.sendMsg(generateAdjustsMsg());
                    } else {
                        LoadingDialog.INSTANCE.dimissDialog();
                        ToastUtils.showShort(SmartControlActivity.this, "netty未连接，请重试");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 开关
     *
     * @return
     */
    private Msg.Message generateSwichMsg() {
        List list = mChildList.get(0);
        List<Msg.Switch> switchList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o instanceof SwitchsEntity) {
                SwitchsEntity switchs = (SwitchsEntity) o;
                switchList.add(
                        Msg.Switch.newBuilder()
                                .setProtocolId(switchs.getProtocolId())
                                .setSwitchStatus(switchs.getParamState())
                                .build()
                );
            }
        }

        return Msg.Message.newBuilder()
                .setMessageType(Msg.MessageType.SWITCH_STATUS)
                .setSwitchMessage(Msg.SwitchMessage.newBuilder()
                        .setIccid(CommonUtils.iccid)//todo  iccid
                        .addAllSwitchList(switchList)
                ).build();
    }

    /**
     * 调节
     *
     * @return
     */
    private Msg.Message generateAdjustsMsg() {
        List list = mChildList.get(1);
        List<Msg.Adjust> adjustsList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o instanceof AdjustsEntity) {
                AdjustsEntity adjusts = (AdjustsEntity) o;

                adjustsList.add(
                        Msg.Adjust.newBuilder()
                                .setAdjustName(adjusts.getHardwareInfo())
                                .setProtocolId(adjusts.getProtocolId())
                                .setAdjustValue(adjusts.getParamValue()!=null?adjusts.getParamValue():"100")
                                .build()
                );
            }
        }

        return Msg.Message.newBuilder()
                .setMessageType(Msg.MessageType.ADJUST_VALUE)
                .setAdjustMessage(Msg.AdjustMessage.newBuilder()
                        .setIccid(CommonUtils.iccid)  //todo  iccid
                        .addAllAdjustList(adjustsList)
                ).build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNettyUtil.unBind();
        timer.cancel();
    }


    @Override
    public void messageReceived(Object msg) {
        Logger.d(msg.toString());
        Msg.Message o = (Msg.Message) msg;
        switch (o.getMessageType()) {
            case SWITCH_STATUS:
                Msg.SwitchMessage switchMessage = ((Msg.Message) msg).getSwitchMessage();
                int status = switchMessage.getStatus();
                Logger.d(status);
                if (status == 1) {
                    LoadingDialog.INSTANCE.dimissDialog();
                    ToastUtils.showShort(this, "设置成功");
                    doRequestSeatHeat();
                    setTimerTask();
                } else if (status == 0) {
                    ToastUtils.showShort(this, "设置失败，请重试");
                }
                break;
            default:
                break;
        }

}

    private int mNetStatus;

    @Override
    public void onNetChange(int netWorkState) {
        this.mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            ToastUtils.showLong(this, "当前网络不可用");
        } else {
        }
    }

    @Override
    public void imageLeftBtnclick() {
        finish();
    }


}

package com.pm.intelligent.module.scenes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.bean.HomeShelterEntity;
import com.pm.intelligent.bean.ProjectEntity;
import com.pm.intelligent.bean.ScenesEntity;
import com.pm.intelligent.bean.SettingValueEntity;
import com.pm.intelligent.greendao.HomeShelterEntityDao;
import com.pm.intelligent.module.scenes.adapter.SceneListAdapter;
import com.pm.intelligent.module.scenes.adapter.SpinnerAdapter;
import com.pm.intelligent.utils.AESUtil;
import com.pm.intelligent.utils.AppUtils;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.SPUtils;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.utils.net.API;
import com.pm.intelligent.utils.net.RetrofitUtil;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.intelligent.widget.LoadingDialog;
import com.pm.mc.chat.netty.pojo.Msg;
import com.pm.nettyService.IOutMsgReCallBack;
import com.pm.nettyService.NettyUtil;
import com.wall_e.multiStatusLayout.MultiStatusLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by WeiSir on 2018/9/7.
 */
public class ScencesActivity extends BaseActivity implements
        BaseToolBar.MyToolBarLeftBtnListener, AdapterView.OnItemSelectedListener, IOutMsgReCallBack<Msg.SceneMessage> {
    @BindView(R.id.toolbar)
    BaseToolBar toolbar;
    @BindView(R.id.lv_scences)
    ListView lvScences;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.btn_reset_control)
    TextView btnResetControl;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.status_layout_scene)
    MultiStatusLayout multipleStatusView;


    private List<ScenesEntity> mDataList = new ArrayList<>();
    private List<ScenesEntity> mDataListCopy = new ArrayList<>();
    private SceneListAdapter mSceneAdapter;
    private List<ProjectEntity> mProjectEntityList = new ArrayList<>();
    private int mProjectId;
    private NettyUtil mNettyUtil;

    @Override
    protected int setLayoutId() {
        return R.layout.scences_activity_layout;
    }

    @Override
    protected void findViews() {
        toolbar.setMyToolBarLeftBtnClick(this);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void initViews() {
        mSceneAdapter = new SceneListAdapter(this, mDataList);
        lvScences.setAdapter(mSceneAdapter);
    }

    @Override
    protected void initDatas() {
        setSpinner();
        doRequestData(false);
        mNettyUtil = NettyUtil.with(Msg.SceneMessage.class).setCallBack(this);
    }

    private void setSpinner() {
        ProjectEntity mProjectEntity = new ProjectEntity();
        HomeShelterEntityDao dao = MyApplication.getDaoSession().getHomeShelterEntityDao();
        List<HomeShelterEntity> homeShelterEntities = dao.loadAll();
        for (int i = 0; i < homeShelterEntities.size(); i++) {
            mProjectEntity.setProjectName(homeShelterEntities.get(i).getProjectName());
            mProjectEntity.setProjectId(homeShelterEntities.get(i).getProjectId());
            mProjectEntityList.add(mProjectEntity);
        }
        SpinnerAdapter mSpinnerAdapter = new SpinnerAdapter(this, removeDuplicteId(mProjectEntityList));
        spinner.setAdapter(mSpinnerAdapter);
    }

    /**
     * Remove duplicte id list.
     * 去重
     *
     * @param list the list
     * @return the list
     */
    public static List<ProjectEntity> removeDuplicteId(List<ProjectEntity> list) {
        Set<ProjectEntity> entitySet = new TreeSet<ProjectEntity>(new Comparator<ProjectEntity>() {
            @Override
            public int compare(ProjectEntity o1, ProjectEntity o2) {
                return o1.getProjectName().compareTo(o2.getProjectName());
            }
        });
        entitySet.addAll(list);
        return new ArrayList<ProjectEntity>(entitySet);
    }

    /**
     * Do request data.
     *
     * @param reset the reset
     */
    @SuppressLint("CheckResult")
    public void doRequestData(final boolean reset) {
        multipleStatusView.showLoading();
        String userToken = SPUtils.getInstance(this).getUserToken();
        String iccid = CommonUtils.iccid;  //TODO iccid
        API instence = RetrofitUtil.getInstence();
        final Observable<ResponseBody> scenes = instence.getScenes(userToken, iccid);
        scenes.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, List<ScenesEntity>>() {
                    @Override
                    public List<ScenesEntity> apply(ResponseBody responseBody) throws Exception {
                        String json = responseBody.string();
                        Logger.json(json);
                        JSONObject object = new JSONObject(json);
                        int status = object.getInt("status");
                        Gson gson = new GsonBuilder().serializeNulls().create();

                        List<ScenesEntity> entityList = new ArrayList<>();
                        if (status == 200) {
                            String data = object.getString("data");
                            String aesDecrypt = AESUtil.aesDecrypt(data);
                            Logger.d("解密后:   " + aesDecrypt);
                            JSONArray array = new JSONArray(aesDecrypt);
                            for (int i = 0; i < array.length(); i++) {
                                String string = array.getString(i);
                                ScenesEntity scenesEntity = gson.fromJson(string, ScenesEntity.class);
                                String settingValue = scenesEntity.getSettingValue();
                                SettingValueEntity settingValueEntity = gson.fromJson(settingValue, SettingValueEntity.class);
                                scenesEntity.setSettingValueEntity(settingValueEntity);
                                entityList.add(scenesEntity);
                            }
                        }
                        Logger.d(entityList);
                        return entityList;
                    }
                })
                .doOnNext(new Consumer<List<ScenesEntity>>() {
                    @Override
                    public void accept(List<ScenesEntity> scenesEntities) throws Exception {
                        if (reset) {
                            mDataListCopy = scenesEntities;
                        } else {
                            mDataList = scenesEntities;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ScenesEntity>>() {
                    @Override
                    public void accept(List<ScenesEntity> scenesEntityList) throws Exception {
                        if (scenesEntityList == null) {
                            multipleStatusView.showError();
                        }
                        if (reset) {
                            mDataListCopy = scenesEntityList;
                            for (int i = 0; i < mDataList.size(); i++) {
                                ScenesEntity list = mDataList.get(i);
                                SettingValueEntity settingValueEntity = mDataList.get(i).getSettingValueEntity();
                                assert mDataListCopy != null;
                                ScenesEntity list1 = mDataListCopy.get(i);
                                SettingValueEntity settingValueEntity1 = mDataList.get(i).getSettingValueEntity();
                                settingValueEntity.setStartTime(settingValueEntity1.getStartTime());
                                settingValueEntity.setEndTime(settingValueEntity1.getEndTime());
                                list.setProjectId(list1.getProjectId());
                                list.setHardwareName(list1.getHardwareName());
                            }

                        } else {
                            mDataList = scenesEntityList;
                        }
                        if (mDataList.isEmpty() || mDataList == null) {
                            multipleStatusView.showEmpty();
                        } else {
                            mSceneAdapter.setScenesEntity(mDataList);
                            mSceneAdapter.notifyDataSetChanged();
                            multipleStatusView.showContent();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.d(throwable.getMessage());
                        multipleStatusView.showError();
                    }
                });
    }

    /**
     * 发送netty消息
     *
     * @return
     */
    private Msg.Message generateScenesMsg() {
        List<Msg.Scene> scenesList = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            Object o = mDataList.get(i);
            if (o != null) {
                ScenesEntity adjusts = (ScenesEntity) o;
                if (adjusts.getSettingValueEntity() != null) {
                    scenesList.add(
                            Msg.Scene.newBuilder()
                                    .setProtocolId(adjusts.getProtocolId())   //todo 更换硬件协议号
                                    .setEndTime(adjusts.getSettingValueEntity().getEndTime())
                                    .setStartTime(adjusts.getSettingValueEntity().getStartTime())
                                    .setSwitchName(adjusts.getHardwareName())
                                    .build()
                    );
                }
            }
        }
        return Msg.Message.newBuilder()
                .setMessageType(Msg.MessageType.SCENE_SMART)
                .setSceneMessage(Msg.SceneMessage.newBuilder()
                        .setProjectId(mProjectId)  //todo   projectId
                        .addAllSceneList(scenesList)
                ).build();
    }


    @Override
    public void imageLeftBtnclick() {
        finish();
    }

    private int mNetStatus;

    @Override
    public void onNetChange(int netWorkState) {
        this.mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            ToastUtils.showLong(this, "当前网络不可用");
            multipleStatusView.showNetError();
        }
    }


    @OnClick({R.id.btn_reset_control, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_control:
                doRequestData(true);
                break;
            case R.id.tv_commit:
                String json = AppUtils.getGson().toJson(mSceneAdapter.getScenesEntity());
                Logger.json(json);
                if (mNettyUtil != null) {
                    mNettyUtil.sendMsg(generateScenesMsg());
                }
                LoadingDialog.INSTANCE.showDialog(ScencesActivity.this, "正在设置中");
                break;
            default:
                break;
        }
    }

    /**
     * spinner选中事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mProjectId = mProjectEntityList.get(position).getProjectId();
        Logger.d(mProjectId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNettyUtil.unBind();
    }


    @Override
    public void messageReceived(Msg.SceneMessage msg) {
        Logger.d(msg.toString());
        if (msg.getStatus() == 1) {
            ToastUtils.showShort(this, "设置成功");
        } else {
            ToastUtils.showShort(this, "设置失败，请重试2");
        }
        LoadingDialog.INSTANCE.dimissDialog();
    }

}

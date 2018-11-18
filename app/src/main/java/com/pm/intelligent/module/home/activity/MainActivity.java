package com.pm.intelligent.module.home.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.Contants;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.R;
import com.pm.intelligent.UrlConstant;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.bean.HomeShelterEntity;
import com.pm.intelligent.greendao.DaoSession;
import com.pm.intelligent.greendao.HomeShelterEntityDao;
import com.pm.intelligent.module.alarm.activity.AlarmInfoActivity;
import com.pm.intelligent.module.box.BoxCheckActivity;
import com.pm.intelligent.module.control.SmartControlActivity;
import com.pm.intelligent.module.electricity.ElectricityAcitity;
import com.pm.intelligent.module.faultTracking.FaultTrackingActivity;
import com.pm.intelligent.module.hardware.HardWareCheckActivity;
import com.pm.intelligent.module.home.StationItemSelectListener;
import com.pm.intelligent.module.home.adapter.HomeGridAdapter;
import com.pm.intelligent.module.home.adapter.StationTabsAdapter;
import com.pm.intelligent.module.home.dao.RequestShelterDao;
import com.pm.intelligent.module.patrol.activity.PatrolActivity;
import com.pm.intelligent.module.patrol.activity.SinglePatroActivity;
import com.pm.intelligent.module.scenes.ScencesActivity;
import com.pm.intelligent.module.system.SystemActivity;
import com.pm.intelligent.module.weather.WeatherActivity;
import com.pm.intelligent.utils.ActivityUtil;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.SPUtils;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.intelligent.widget.HomePop;
import com.pm.intelligent.widget.LoadingDialog;
import com.pm.intelligent.widget.SlideMenu;
import com.pm.nettyService.NettyService;
import com.pm.nettyService.NettyUtil;
import com.pm.okgolibrary.okInit.OkCallBack;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements
        BaseToolBar.MyToolBarEditClick,
        AdapterView.OnItemClickListener,
        StationItemSelectListener,
        BaseToolBar.MyToolBarLeftBtnListener {
    private static String TAG = "MainActivity";

    @BindView(R.id.home_slide_menu)
    SlideMenu mMenu;
    @BindView(R.id.home_toolbar)
    BaseToolBar mToolBar;
    @BindView(R.id.lv_stations_tab)
    ListView lvStationsTab;
    @BindView(R.id.tv_last_time)
    TextView tvLastTime;
    @BindView(R.id.iv_reflush)
    ImageView ivReflush;
    @BindView(R.id.tv_current_station)
    TextView tvCurrentStation;
    @BindView(R.id.home_grid_menu_top)
    GridView homeGridMenuTop;
    @BindView(R.id.container)
    RelativeLayout mRoot;
    //slideList适配器
    private StationTabsAdapter mStationsAdapter;
    //slideList数据
    private List<HomeShelterEntity> datas;
    private int isExit;
    private ImageButton mRight;
    private HomeShelterEntityDao mShelterDao;
    private DaoSession mDaosSession;
    private String mCurrentStation;
    private int mNetStatus;

    public static final int ONMESSAGELIST = 1;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
        mRight = mToolBar.findViewById(R.id.toolbar_rightImgBtn);
    }

    @Override
    protected void initViews() {
        //初始化toolbar
        mContext = this;
        mToolBar.setMyToolBarEditClcik(this);
        //mToolBar.setMyToolBarRightTextClick(this);
        mToolBar.setMyToolBarBtnListenter(new BaseToolBar.MyToolBarBtnListenter() {
            @Override
            public void imageRightBtnclick() {
                // showPopupWindow();
                HomePop.INSTANCE.showPop(mContext, mRight, new HomePop.PopListener() {
                    @Override
                    public void groupCheck() {
                        ActivityUtil.INSTANCE.startActivity(PatrolActivity.class);
                    }

                    @Override
                    public void singleCheck() {
                        ActivityUtil.INSTANCE.startActivity(SinglePatroActivity.class);
//                        ConfirmDialog.INSTANCE.showConfirmDialog(MainActivity.this, "是否进行点检", new ConfirmDialog.OnConfirmListener() {
//                            @Override
//                            public void onConfirmClick() {
//                                ToastUtils.showShort(MainActivity.this, "点检");
//                                ActivityUtil.INSTANCE.startActivity(SinglePatroActivity.class);
//                            }
//                        });
                    }
                });
            }
        });
        mToolBar.setMyToolBarLeftBtnClick(this);
        mToolBar.setLeftBtnPadding(CommonUtils.dip2px(this, 10), 0, 0, 0);

        mToolBar.isEditTextFocus(false);
//        mRoot.setPadding(0,getStatusBarHeight(),0,0);

        HomeGridAdapter mTopGridAdapter = new HomeGridAdapter(this, Contants.menuIcons, Contants.menuNames);
        homeGridMenuTop.setAdapter(mTopGridAdapter);
        //item中设置了padding,点击效果不太好看,去掉gridView默认点击的效果
        homeGridMenuTop.setSelector(new ColorDrawable(Color.TRANSPARENT));
        homeGridMenuTop.setOnItemClickListener(this);
    }


    @Override
    protected void initDatas() {
        /**
         * app升级
         */
//        updateApp(view);
        /**
         * 开启netty服务
         */
        Intent intent = new Intent(this, NettyService.class);
        bindService(intent, nettyConn, BIND_AUTO_CREATE);
        NettyUtil.init(this, UrlConstant.SERVER_IP,UrlConstant.SERVER_PORT);

        /*请求数据*/
//        doRequestShelter();

        /*设置数据*/
        setShelterData();
    }


    private ServiceConnection nettyConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof NettyService.MyBinder) {
                NettyService.MyBinder binder = (NettyService.MyBinder) service;
                NettyService nettyService = binder.getNettyService();
            }
//            if (service instanceof NoficationServices) {
//                NoficationServices.MyBinder binder = (NoficationServices.MyBinder) service;
//                mNotificationService = binder.getNotificationService();
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(HomeShelterEntity entity) {
        String shelterName = entity.getShelterName();
        ToastUtils.showShort(this, shelterName);
        tvCurrentStation.setText(shelterName);
        CommonUtils.iccid = entity.getIccid();
        CommonUtils.shelterName = entity.getShelterName();
        Logger.t("Subscribe HomeShelterEntity").i(shelterName);
//        mStationsAdapter.setSelectItem();
//        lvStationsTab.smoothScrollToPosition();
    }

    /**
     * 站台列表
     */
    private void setShelterData() {
        mDaosSession = MyApplication.getDaoSession();
        mShelterDao = mDaosSession.getHomeShelterEntityDao();
        mStationsAdapter = new StationTabsAdapter(this);
        mStationsAdapter.setSelectItem(0);
        lvStationsTab.setAdapter(mStationsAdapter);
        mStationsAdapter.setStationItemListener(this);
        lvStationsTab.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        /**进入加载数据库数据*/
       doRequestShelter();
//        datas = RequestShelterDao.getHistory();
//        if (datas.size() == 0) {
//            doRequestShelter();
//        } else {
//            mStationsAdapter.setStationsBeanList(datas);
//            mCurrentStation = datas.get(0).getShelterName();
//            CommonUtils.iccid = datas.get(0).getIccid();
//            CommonUtils.shelterName = datas.get(0).getShelterName();
//            tvCurrentStation.setText(mCurrentStation);
//        }
    }

    /**
     * 请求首页站台列表数据
     */
    private void doRequestShelter() {
        LoadingDialog.INSTANCE.showDialog(this, "正在加载中...");
        HashMap<String, String> params = new HashMap<>();
        String token = SPUtils.getInstance(this).getUserToken();
        params.put("token", token);
        RequestShelterDao.doGetShelter(Contants.sheter, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                datas = (List<HomeShelterEntity>) obj;
                mStationsAdapter.setStationsBeanList(datas);
                mStationsAdapter.notifyDataSetChanged();
//                tvCurrentStation.setText(mCurrentStation);
                LoadingDialog.INSTANCE.dimissDialog();
                if (datas.size() > 0) {
                    mCurrentStation = datas.get(0).getShelterName();
                    CommonUtils.iccid = datas.get(0).getIccid();
                    CommonUtils.shelterName = datas.get(0).getShelterName();
                    tvCurrentStation.setText(mCurrentStation);
                } else {
                    tvCurrentStation.setText("获取数据失败");
                }
            }

            @Override
            public void failed(int error, Object obj) {
                LoadingDialog.INSTANCE.dimissDialog();
                ToastUtils.showShort(MainActivity.this, "数据加载失败,请稍后重试");
            }
        });
    }


    @OnClick({R.id.rl_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_main:
                if (mMenu.isShowMenu()) {
                    mMenu.hideMenu();
                }
                break;
            default:
        }
    }



    /**
     * ToolBar左侧按钮点击
     * 弹出slide list
     */
    @Override
    public void imageLeftBtnclick() {
        if (mMenu.isShowMenu()) {
            mMenu.hideMenu();
        } else {
            mMenu.showMenu();
        }
    }

    /**
     * 输入框点击
     */
    @Override
    public void editTextClick() {
//        ActivityUtil.INSTANCE.startActivity(SearchActivity.class);
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            HomeShelterEntity entity = data.getParcelableExtra("homeShelterEntity");
            Logger.d(entity.toString());
            tvCurrentStation.setText(entity.getShelterName());
            CommonUtils.iccid = entity.getIccid();
            CommonUtils.shelterName = entity.getShelterName();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: 点击!!!!!");

        if (parent.getId() == R.id.home_grid_menu_top) {

            switch (position) {
                case 0:
                    //盒子检测
                    ActivityUtil.INSTANCE.startActivity(BoxCheckActivity.class);
                    break;
                case 1:
                    //硬件检测
                    ActivityUtil.INSTANCE.startActivity(HardWareCheckActivity.class);
                    break;
                case 2:
                    //系统监控
                    ActivityUtil.INSTANCE.startActivity(SystemActivity.class);
                    break;
                case 3:
                    //智能控制
                    ActivityUtil.INSTANCE.startActivity(SmartControlActivity.class);
                    break;
                case 4://气象监测
                    ActivityUtil.INSTANCE.startActivity(WeatherActivity.class);
                    break;
                case 5:
                    //用电检测
                    ActivityUtil.INSTANCE.startActivity(ElectricityAcitity.class);
                    break;
                case 6:
                    //实时报警
                    ActivityUtil.INSTANCE.startActivity(AlarmInfoActivity.class);
                    break;
                case 7:
                    //情景智能
                    ActivityUtil.INSTANCE.startActivity(ScencesActivity.class);
                    break;
                case 8:
                    //异常跟踪
                    ActivityUtil.INSTANCE.startActivity(FaultTrackingActivity.class);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onStationItemSelect(int position) {
        mStationsAdapter.setSelectItem(position);
        mStationsAdapter.notifyDataSetChanged();
        mCurrentStation = datas.get(position).getShelterName();
        tvCurrentStation.setText(mCurrentStation);
//        mIccid = datas.get(position).getIccid();
        CommonUtils.iccid = datas.get(position).getIccid();
        CommonUtils.shelterName = datas.get(position).getShelterName();
        mMenu.hideMenu();
    }



    @Override
    public void onNetChange(int netWorkState) {
        this.mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            ToastUtils.showLong(this, "当前网络不可用");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(nettyConn);
    }

//    @Override
//    public void onBackPressed() {
//        moveTaskToBack(true);
//    }

        @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMenu.isShowMenu()) {
                mMenu.hideMenu();
                return false;
            } else {
                isExit++;
                if (isExit == 1) {
                    showToast("再按一次退出APP");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isExit = 0;
                        }
                    }, 2500);
                }
                if (isExit == 2) {
                    finish();
                    System.exit(0);
                }
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

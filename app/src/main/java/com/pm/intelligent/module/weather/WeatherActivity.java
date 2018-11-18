package com.pm.intelligent.module.weather;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.pm.intelligent.Contants;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.base.CommonRecycleViewAdapter;
import com.pm.intelligent.module.hardware.HardWareGroupBean;
import com.pm.intelligent.module.hardware.HardWareItemBean;
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

import butterknife.BindView;

/**
 * Created by WeiSir on 2018/8/31.
 */
public class WeatherActivity extends BaseActivity implements
        BaseToolBar.MyToolBarLeftBtnListener {
    @BindView(R.id.toolbar)
    BaseToolBar toolbar;
    @BindView(R.id.lv_box_weather)
    RecyclerView lvBoxWeather;
    @BindView(R.id.status_layout_box)
    MultiStatusLayout statusLayout;

    private int mNetStatus;

    private List<HardWareItemBean> mDataList = new ArrayList<>();
    private List<HardWareGroupBean> mGroupList = new ArrayList<>();


    @Override
    protected int setLayoutId() {
        return R.layout.weather_activity_layout;
    }

    @Override
    protected void findViews() {
        toolbar.setMyToolBarLeftBtnClick(this);
    }

    @Override
    protected void initViews() {
        lvBoxWeather.setLayoutManager(new LinearLayoutManager(WeatherActivity.this, LinearLayoutManager.VERTICAL, false));
        lvBoxWeather.setAdapter(weatherAdapter);
    }

    @Override
    protected void initDatas() {
        doRequsetData();
    }

    /***
     * 请求数据
     */
    private void doRequsetData() {
        statusLayout.showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("iccid", CommonUtils.iccid);  //todo iccid
        params.put("token", SPUtils.getInstance(this).getUserToken());
        HardWareDao.doRequsetHard(Contants.HARDWARES, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                mGroupList = (List<HardWareGroupBean>) obj;
                if (mGroupList.isEmpty()) {
                    statusLayout.showEmpty();
                } else {
                    for (int j = 0; j < mGroupList.size(); j++) {
                        if (mGroupList.get(j).getHardwareName().equals("气象监测")) {
                            mDataList.addAll(mGroupList.get(j).getHardwareParamVos());
                        }
                    }
                    weatherAdapter.notifyDataSetChanged();
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

    CommonRecycleViewAdapter weatherAdapter = new CommonRecycleViewAdapter<HardWareItemBean>(R.layout.box_list_result_item, mDataList) {
        @Override
        protected void convert(BaseViewHolder helper, HardWareItemBean item) {
            super.convert(helper, item);
            String name = item.getHardwareInfo();
            helper.setText(R.id.tv_1, name);

            String values = item.getParamValue();
            if (values == null) {
                helper.setText(R.id.tv_3, "--");
            } else {
                switch (name) {
                    case "湿度":
                        helper.setText(R.id.tv_3, values + "%");
                        break;
                    case "PM2.5":
                        helper.setText(R.id.tv_3, values + "μg/m³");
                        break;
                    case "噪音":
                        helper.setText(R.id.tv_3, values + "分贝");
                        break;
                    case "风力":
                        helper.setText(R.id.tv_3, values + "级");
                        break;
                    case "风向":
                        helper.setText(R.id.tv_3, values + "");
                        break;
                    case "PM10":
                        helper.setText(R.id.tv_3, values + "μg/m³");
                        break;
                    case "气压":
                        helper.setText(R.id.tv_3, values + "百帕");
                        break;
                    case "温度":
                        helper.setText(R.id.tv_3, values + "°");
                        break;
                    default:
                        helper.setText(R.id.tv_3, values);
                        break;
                }

            }

            TextView status = helper.getView(R.id.tv_2);
            if (item.getParamState()!=null) {
                if (item.getParamState().equals("0")) {
                    status.setTextColor(mContext.getResources().getColor(R.color.text_black));
                    status.setText("正常");
                } else if (item.getParamState().equals("-1")) {
                    status.setVisibility(View.INVISIBLE);
                } else {
                    status.setTextColor(mContext.getResources().getColor(R.color.text_red));
                    status.setText("异常");
                }
            }
            if (item.getHardwareInfo().equals("风向")) {
                status.setVisibility(View.INVISIBLE);
            } else {
                status.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onNetChange(int netWorkState) {
        super.onNetChange(netWorkState);
        mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            ToastUtils.showShort(this, "网络未连接");
        }
    }

    @Override
    public void imageLeftBtnclick() {
        finish();
    }


}

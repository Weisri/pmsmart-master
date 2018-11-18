package com.pm.intelligent.module.box;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.Contants;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.base.CommonRecycleViewAdapter;
import com.pm.intelligent.bean.BoxDataBean;
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
 * Created by WeiSir on 2018/7/4.
 */

public class BoxCheckActivity extends BaseActivity implements
        BaseToolBar.MyToolBarLeftBtnListener {
    @BindView(R.id.home_toolbar)
    BaseToolBar mToolbar;
    @BindView(R.id.rv_box_check)
    RecyclerView rvBoxCheck;
    @BindView(R.id.status_layout_box)
    MultiStatusLayout statusLayoutBox;

    @Override
    protected int setLayoutId() {
        return R.layout.boxcheck_activity_layout;
    }

    @Override
    protected void findViews() {
        mContext = this;
    }

    @Override
    protected void initViews() {
        mToolbar.setMyToolBarLeftBtnClick(this);
        rvBoxCheck.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvBoxCheck.setAdapter(boxCheckAdapter);
        loadData();
    }

    private void loadData() {
        statusLayoutBox.showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getInstance(this).getUserToken());
        params.put("iccid", CommonUtils.iccid);  //TODO ICCID
        BoxResultDao.doBoxResultDao(Contants.BOX, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                mDataList = (List<BoxDataBean>) obj;
                boxCheckAdapter.replaceData(mDataList);
                boxCheckAdapter.notifyDataSetChanged();
                Logger.d(mDataList.toString());
                statusLayoutBox.showContent();
            }

            @Override
            public void failed(int error, Object obj) {
                if (error == 200 || error == 300) {
                    statusLayoutBox.showEmpty();
                }

                statusLayoutBox.showError();
                ToastUtils.showShort(BoxCheckActivity.this, "获取数据失败");
            }
        });
    }

    private List<BoxDataBean> mDataList = new ArrayList<>();
    CommonRecycleViewAdapter boxCheckAdapter = new CommonRecycleViewAdapter<BoxDataBean>(R.layout.boxlist_result_item, mDataList) {
        @Override
        protected void convert(BaseViewHolder helper, BoxDataBean item) {
            helper.setText(R.id.tv_title_box, item.getName())
                    .setText(R.id.tv_status_box, item.getStatus())
                    .setText(R.id.tv_info_box, item.getInfo());

            if (!TextUtils.isEmpty(item.getStatus())) {
                helper.setVisible(R.id.tv_status_box, true);
            }
            if (item.getName().equals("病毒：") && item.getInfo().equals("是")) {
                helper.setTextColor(R.id.tv_info_box, getResources().getColor(R.color.text_red));
            } else {
                helper.setTextColor(R.id.tv_info_box, getResources().getColor(R.color.text_black));
            }
            if (item.getName().equals("网络信号：")) {
                helper.setText(R.id.tv_info_box, "");
                String singal = item.getInfo();
                switch (singal) {
                    case "5":
                        helper.setBackgroundRes(R.id.tv_info_box, R.mipmap.signal4);
                        break;
                    case "4":
                        helper.setBackgroundRes(R.id.tv_info_box, R.mipmap.signal4);
                        break;
                    case "3":
                        helper.setBackgroundRes(R.id.tv_info_box, R.mipmap.signal3);
                        break;
                    case "2":
                        helper.setBackgroundRes(R.id.tv_info_box, R.mipmap.signal2);
                        break;
                    case "1":
                        helper.setBackgroundRes(R.id.tv_info_box, R.mipmap.signal1);
                        break;
                    case "0":
                        helper.setText(R.id.tv_info_box, "无");
                        helper.setText(R.id.tv_status_box, "异常");
                        break;
                    default:
                        break;
                }
            }
            if (item.getName().equals("当前状态：")) {
                if (item.getInfo().equals("1")) {
                    helper.setText(R.id.tv_info_box, "在线");
                } else {
                    helper.setText(R.id.tv_info_box, "离线");
                }
            }
            if (item.getName().equals("内存使用：")) {
                helper.setVisible(R.id.tv_status_box, true);
                if (Integer.parseInt(item.getInfo()) >= 90) {
                    helper.setText(R.id.tv_info_box, item.getInfo() + "%");
                    helper.setText(R.id.tv_status_box, "异常");
                    helper.setTextColor(R.id.tv_status_box, Color.RED);
                } else if (Integer.parseInt(item.getInfo()) < 90) {
                    helper.setText(R.id.tv_status_box, "正常");
                    helper.setText(R.id.tv_info_box, item.getInfo() + "%");
                }
            }
        }
    };


    @Override
    protected void initDatas() {
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
        }
    }

}

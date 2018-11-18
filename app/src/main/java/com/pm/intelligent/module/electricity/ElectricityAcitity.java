package com.pm.intelligent.module.electricity;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.bean.ElectricMonthBean;
import com.pm.intelligent.bean.ElectricWeekBean;
import com.pm.intelligent.utils.AESUtil;
import com.pm.intelligent.utils.AppUtils;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.SPUtils;
import com.pm.intelligent.utils.net.API;
import com.pm.intelligent.utils.net.RetrofitUtil;
import com.pm.intelligent.widget.BaseToolBar;
import com.pm.intelligent.widget.charts.LineChartManager;
import com.wall_e.multiStatusLayout.MultiStatusLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by WeiSir on 2018/8/31.
 */
public class ElectricityAcitity extends BaseActivity implements BaseToolBar.MyToolBarLeftBtnListener, OnChartValueSelectedListener {
    @BindView(R.id.toolbar)
    BaseToolBar toolbar;
    @BindView(R.id.status_layout_box)
    MultiStatusLayout statusLayout;
    @BindView(R.id.chart)
    LineChart mCharts;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    private XAxis xAxis;
    private YAxis yLeftAxis, yRightAxis;
    private LineChartManager mLineChartManger,mLineChartManger1;
    private List<ElectricMonthBean> mMonthList = new ArrayList<>();
    private List<ElectricWeekBean> mWeekList = new ArrayList<>();

    private String[] months = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
    private String[] weeks = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};


    @Override
    protected int setLayoutId() {
        return R.layout.electrcity_activity_layout;
    }

    @Override
    protected void findViews() {
        toolbar.setMyToolBarLeftBtnClick(this);
    }

    @Override
    protected void initViews() {
        mLineChartManger = new LineChartManager(lineChart);
        mLineChartManger1 = new LineChartManager(mCharts);

    }

    @Override
    protected void initDatas() {
//        initBarCharts();
        doRequesetData();
    }

    @SuppressLint("CheckResult")
    private void doRequesetData() {

        statusLayout.showLoading();
        String userToken = SPUtils.getInstance(this).getUserToken();
        String iccid = CommonUtils.iccid;//todo iccid
        API instance = RetrofitUtil.getInstence();
        final Observable<ResponseBody> electricMonth = instance.getElectricMonth(userToken, iccid);
        final Observable<ResponseBody> electricWeek = instance.getElectricWeek(userToken, iccid);
        electricMonth.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, List<ElectricMonthBean>>() {
                    @Override
                    public List<ElectricMonthBean> apply(ResponseBody responseBody) throws Exception {
                        String json = responseBody.string();
                        Logger.json(json);
                        JSONObject jsonObject = new JSONObject(json);
                        int status = jsonObject.getInt("status");
                        List<ElectricMonthBean> monthBeanList = new ArrayList<>();
                        if (status == 200) {
                            String data = jsonObject.getString("data");
                            String aesDecrypt = AESUtil.aesDecrypt(data);
                            Logger.d("解密后：  " + aesDecrypt);
                            JSONArray array = new JSONArray(aesDecrypt);
                            for (int i = 0; i < array.length(); i++) {
                                ElectricMonthBean monthBean = AppUtils.getGson().fromJson(array.getString(i), ElectricMonthBean.class);
                                monthBeanList.add(monthBean);
                            }
                        } else {
                            return monthBeanList;
                        }
                        return monthBeanList;
                    }
                })
                .doOnNext(new Consumer<List<ElectricMonthBean>>() {
                    @Override
                    public void accept(List<ElectricMonthBean> electricMonthBeans) throws Exception {
                        mMonthList = electricMonthBeans;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<List<ElectricMonthBean>, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(List<ElectricMonthBean> electricMonthBeans) throws Exception {
                        return electricWeek;
                    }
                })
                .map(new Function<ResponseBody, List<ElectricWeekBean>>() {
                    @Override
                    public List<ElectricWeekBean> apply(ResponseBody responseBody) throws Exception {
                        String json = responseBody.string();
                        Logger.json(json);
                        JSONObject object = new JSONObject(json);
                        int status = object.getInt("status");
                        List<ElectricWeekBean> list = new ArrayList<>();
                        if (status == 200) {
                            String data = object.getString("data");
                            String aesDecrypt = AESUtil.aesDecrypt(data);
                            Logger.d("解密后：  " + aesDecrypt);
                            JSONArray array = new JSONArray(aesDecrypt);
                            for (int i = 0; i < array.length(); i++) {
                                ElectricWeekBean electricWeekBean = AppUtils.getGson().fromJson(array.getString(i), ElectricWeekBean.class);
                                list.add(electricWeekBean);
                            }
                        } else {
                            return list;
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ElectricWeekBean>>() {
                    @Override
                    public void accept(List<ElectricWeekBean> electricWeekBeans) throws Exception {
                        mWeekList = electricWeekBeans;
                        statusLayout.showContent();
                        initMonthLineChart();
                        initWeekLineChart();


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.d(throwable.getMessage());
                        statusLayout.showError();
                    }
                });


    }

    private void initMonthLineChart() {
        //去掉右侧Y轴
        yRightAxis = mCharts.getAxisRight();
        yRightAxis.setEnabled(false);


        //设置x轴方向上的标签数据
        final List<String> xValsDate = new ArrayList<String>();
        for (int i = 0; i < mMonthList.size(); i++) {
            xValsDate.add(mMonthList.get(i).getMonth());
        }
        mLineChartManger.setXVals(xValsDate);
        //x轴数据
        List<Float> xAXisDate = new ArrayList<>();
        for (int i = 0; i < mMonthList.size(); i++) {
            xAXisDate.add((float) i);
        }
        //y轴数据
        List<Float> yAXisData = new ArrayList<>();
        for (int i = 0; i < mMonthList.size(); i++) {
            yAXisData.add((float) mMonthList.get(i).getElectricTotal());
        }
        mLineChartManger.showLineChart(xAXisDate, yAXisData, "月用电量", Color.GRAY);
//        mLineChartManger.setDescription("月用电量统计图");
        mLineChartManger.setDescriptionEnable(false);
        mLineChartManger.setYAxis(200, 0, 11, true);
        mLineChartManger.setHighLimitLine(85, "", Color.RED);
    }

    private void initWeekLineChart() {
        yRightAxis = lineChart.getAxisRight();
        yRightAxis.setEnabled(false);

        //设置x轴方向上的标签数据
        final List<String> xValsDate = new ArrayList<String>();
        for (int i = 0; i < mWeekList.size(); i++) {
            xValsDate.add(mWeekList.get(i).getWeek());
        }
        mLineChartManger1.setXVals(xValsDate);
        //x轴数据
        List<Float> xAXisDate = new ArrayList<>();
        for (int i = 0; i < mWeekList.size(); i++) {
            xAXisDate.add((float) i);
        }
        //y轴数据
        List<Float> yAXisData = new ArrayList<>();
        for (int i = 0; i < mWeekList.size(); i++) {
            yAXisData.add((float) mWeekList.get(i).getElectricTotal());
        }
        mLineChartManger1.showLineChart(xAXisDate, yAXisData, "日用电量", Color.GRAY);
//        mLineChartManger1.setDescription("日用电量统计图");
        mLineChartManger1.setDescriptionEnable(false);
        mLineChartManger1.setYAxis(200, 0, 11, true);
        mLineChartManger1.setHighLimitLine(85, "", Color.RED);
    }


    @Override
    public void imageLeftBtnclick() {
        finish();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
    }
}

package com.pm.intelligent.widget.charts;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiSir on 2018/10/12.
 * 折线图相关封装
 */
public class LineChartManager {

    private LineChart mLineChart;
    //X轴
    private XAxis mXAxis;
    //左边Y轴
    private YAxis mLeftYAxis;
    //右边Y轴
    private YAxis mRightYAxis;


    public LineChartManager(LineChart mLineChart) {
        this.mLineChart = mLineChart;
        mXAxis = mLineChart.getXAxis();
        mLeftYAxis = mLineChart.getAxisLeft();
        mRightYAxis = mLineChart.getAxisRight();
    }

    /**
     * 初始化LineChart
     */
    private void initLineChart() {
        //取消背景网格
        mLineChart.setDrawGridBackground(false);
        //设置右侧Y轴不现实
        mRightYAxis.setDrawLabels(false);
        mLineChart.setNoDataText("还没有任何数据");
        //不现实边界
        mLineChart.setDrawBorders(false);
        //设置动画效果
        mLineChart.animateY(1000, Easing.EasingOption.Linear);
        mLineChart.animateX(1000, Easing.EasingOption.Linear);

        //折线图例、标签设置
        Legend legend = mLineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        //X、Y轴的显示位置
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mXAxis.setAxisMinimum(0f);
        mXAxis.setGranularity(1f);
        //取消网格
        mXAxis.setDrawGridLines(false);
        //保证Y轴从零开始，不然会上移一点
        mLeftYAxis.setAxisMinimum(0f);
        mLeftYAxis.setAxisMaximum(0f);
    }

    /**
     * 初始化曲线
     * 一个LineDataSet代表一条线
     *
     * @param lineDataSet
     * @param color
     * @param mode        折线是否填充
     */
    private void initLineDateSet(LineDataSet lineDataSet, int color, boolean mode) {
        //折线图原点颜色
        lineDataSet.setCircleColor(color);
        lineDataSet.setColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置折现圆点是实心还是空心
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setValueTextSize(9f);
        //设置折线图填充
        lineDataSet.setDrawFilled(mode);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        //填充颜色、透明度
//        lineDataSet.setFillColor(color);
        lineDataSet.setFillAlpha(35);
        //折线模式，默认圆滑曲线
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }

    /**
     * Show line chart.
     * 显示一条折线图
     *
     * @param xAxisValues the x axis values
     * @param yAxisValues the y axis values
     * @param label       the label
     * @param color       the color
     */
    public void showLineChart(List<Float> xAxisValues, List<Float> yAxisValues, String label, int color) {
        initLineChart();
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < xAxisValues.size(); i++) {
            entries.add(new Entry(xAxisValues.get(i), yAxisValues.get(i)));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, label);
        //线条平滑
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        initLineDateSet(lineDataSet, color, true);
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet);
        LineData lineData = new LineData(iLineDataSets);
        //设置x轴的刻度数
        mXAxis.setLabelCount(xAxisValues.size(), true);
        mLineChart.setData(lineData);
    }


    /**
     * Show line chart.
     * 显示多条折线
     *
     * @param xAxisValues the x axis values
     * @param yAxisValues the y axis values
     * @param labels      the labels
     * @param colors      the colors
     */
    public void showLineChart(List<Float> xAxisValues, List<List<Float>> yAxisValues, List<String> labels, List<Integer> colors) {
        initLineChart();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < yAxisValues.size(); i++) {
            ArrayList<Entry> entries = new ArrayList<>();
            for (int j = 0; j < yAxisValues.get(i).size(); j++) {
                if (j >= xAxisValues.size()) {
                    j = xAxisValues.size() - 1;
            }
                entries.add(new Entry(xAxisValues.get(j), yAxisValues.get(i).get(j)));
            }
            LineDataSet lineDataSet = new LineDataSet(entries,labels.get(i));
            initLineDateSet(lineDataSet,colors.get(i),true);
            dataSets.add(lineDataSet);
        }
        LineData lineData = new LineData(dataSets);
        mXAxis.setLabelCount(xAxisValues.size(),true);
        mLineChart.setData(lineData);
    }

    /**
     * Sets y axis.
     * 设置y轴的值
     * @param max        the max
     * @param min        the min
     * @param labelCount the label count
     * @param force      the force
     */
    public void setYAxis(float max, float min, int labelCount,boolean force) {
        mLeftYAxis.setAxisMaximum(max);
        mLeftYAxis.setAxisMinimum(min);
        mLeftYAxis.setLabelCount(labelCount,force);

        mRightYAxis.setAxisMinimum(min);
        mRightYAxis.setAxisMaximum(max);
        mRightYAxis.setLabelCount(labelCount,force);
        mLineChart.invalidate();
    }


    /**
     * Sets x vals.
     * 设置X轴方向的标签数据
     * @param xVals the x vals
     */
    public void setXVals(final List<String> xVals) {
        if (xVals.size()>0) {
            mXAxis = mLineChart.getXAxis();
            mXAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return xVals.get((int) value);
                }
            });
        }
    }

    /**
     * Sets x axis.
     * 设置X轴的值
     * @param max        the max
     * @param min        the min
     * @param labekCount the labek count
     * @param force      the force
     */
    public void setXAxis(float max, float min, int labekCount, boolean force) {
        mXAxis.setAxisMaximum(max);
        mXAxis.setAxisMinimum(min);
        mXAxis.setLabelCount(labekCount,force);
        mLineChart.invalidate();
    }

    /**
     * Sets high limit line.
     * 设置高限制线
     * @param high  the high
     * @param name  the name
     * @param color the color
     */
    public void setHighLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine limitLine = new LimitLine(high, name);
        limitLine.setLineColor(color);
        limitLine.setLineWidth(0.5f);
        limitLine.setTextSize(10f);
        limitLine.setTextColor(color);
        mLeftYAxis.addLimitLine(limitLine);
        mLineChart.invalidate();
    }

    /**
     * Sets low limit line.
     * 设置低限制线
     * @param low   the low
     * @param name  the name
     * @param color the color
     */
    public void setLowLimitLine(float low, String name, int color) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine lowLimitLine = new LimitLine(low,name);
        lowLimitLine.setLineWidth(4f);
        lowLimitLine.setLineColor(color);
        lowLimitLine.setTextSize(10f);
        lowLimitLine.setTextColor(color);
        mLeftYAxis.addLimitLine(lowLimitLine);
        mLineChart.invalidate();

    }

    /**
     * Sets label rotation angle.
     * 设置x轴的标签旋转角度
     * @param rotationAngle the rotation angle
     */
    public void setLabelRotationAngle(float rotationAngle) {
        mLineChart.getXAxis().setLabelRotationAngle(rotationAngle);

    }

    /**
     * Sets description.
     * 设置表描述
     * @param description the description
     */
    public void setDescription(String description) {
        Description description1 = new Description();
        description1.setText(description);
        mLineChart.setDescription(description1);
        mLineChart.invalidate();
    }

    /**
     * Sets description.
     * 设置表描述
     * @param enable the description
     */
    public void setDescriptionEnable(boolean enable) {
        Description description1 = new Description();
        mLineChart.getDescription().setEnabled(enable);
    }

    /**
     * Sets legend enable.
     * 设置图列
     * @param enable the enable
     */
    public void setLegendEnable(boolean enable) {
        Legend legend = mLineChart.getLegend();
        legend.setEnabled(enable);

    }

    /**
     * Sets empty data.
     * 设置空数据
     */
    public void setEmptyData() {
        mLineChart.setData(new LineData());
        mLineChart.invalidate();
    }


}

package com.pm.intelligent.module.control;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.pm.intelligent.R;
import com.pm.intelligent.bean.AdjustsEntity;
import com.pm.intelligent.bean.SwitchsEntity;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.widget.SwichView;
import com.pm.intelligent.widget.charts.LineChartManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by WeiSir on 2018/7/13.
 */

public class ControlExpandLisAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mGroupList = new ArrayList<>();
    private List<List> mChildList = new ArrayList<>();
    private ExpandableListView mListView;
    private SeatSwitchStatusCallBack seatSwitchStatusCallBack;
    //x轴上的标签数据
    private List<String> xValsDate = new ArrayList<String>();
    //x轴上的数据
    private List<Float> xAXisDate = new ArrayList<>();
    //y轴上的数据
    private List<Float> yAXisData = new ArrayList<>();

    public void setxValsDate(List<String> xValsDate) {
        this.xValsDate = xValsDate;
    }

    public void setxAXisDate(List<Float> xAXisDate) {
        this.xAXisDate = xAXisDate;
    }

    public void setyAXisData(List<Float> yAXisData) {
        this.yAXisData = yAXisData;
    }

    public List<List> getChildList() {
        return mChildList;
    }


    public void setChildData(List<List> childData) {
        mChildList.clear();
        mChildList.addAll(childData);
    }

    public enum ItemType {
//        智能调节_RB3  智能调节_RB2
        /**
         * item类型
         */
        智能开关, 智能调节,
    }


    public ControlExpandLisAdapter(Context mContext, List<String> mGroupList, List<List> mChildList, ExpandableListView listView) {
        this.mContext = mContext;
        this.mGroupList = mGroupList;
        this.mListView = listView;
        setChildData(mChildList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return mChildList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.control_expandlist_item_parent, null);
            viewHolder.tvGroupName = convertView.findViewById(R.id.tv_parent_name);
            viewHolder.ivUp = convertView.findViewById(R.id.iv_up);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        viewHolder.tvGroupName.setText(mGroupList.get(groupPosition));
        if (isExpanded) {
            viewHolder.ivUp.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.less));
        } else {
            viewHolder.ivUp.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.more_unfold));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        SwitchViewHolder switchViewHolder = null;
        SeekViewHolder seekViewHolder = null;
        LcdViewHolder lcdViewHolder = null;
        int childType = getChildType(groupPosition, childPosition);


        /**
         * 智能开关
         */
        if (childType == ItemType.智能开关.ordinal()) {
            SwitchsEntity switchsEntity = (SwitchsEntity) getChild(groupPosition, childPosition);
            convertView = null;//todo  加item暂时去除复用
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.control_expandlist_item_child_switch, null);
                switchViewHolder = new SwitchViewHolder(convertView, switchsEntity);
                convertView.setTag(switchViewHolder);
            } else {
                switchViewHolder = (SwitchViewHolder) convertView.getTag();
            }
            switchViewHolder.setSwitchName(switchsEntity.getSwitchName());
            if (switchsEntity.getParamState() == null) {
                switchsEntity.setParamState("0");
                switchViewHolder.setSwitch(switchsEntity.getParamState().equals("1"));
            } else {
                switchViewHolder.setSwitch(switchsEntity.getParamState().equals("1"));
            }
            /**如果是加热座椅 且开关打开 显示图表*/
            if (switchsEntity.getHardwareId() == 14) {
                if (switchViewHolder.aSwitch.isOpen()) {
                    switchViewHolder.llChart.setVisibility(View.VISIBLE);
//                    if (seatSwitchStatusCallBack != null) {
//                        seatSwitchStatusCallBack.isOpen(true);
//                    }
                    CommonUtils.isSeatOpen = true;
                } else {
                    switchViewHolder.llChart.setVisibility(View.GONE);
                }
            }
            return convertView;
        }


        /**
         * 智能调节
         */
        if (childType == ItemType.智能调节.ordinal()) {
            AdjustsEntity adjustsEntity = (AdjustsEntity) getChild(groupPosition, childPosition);
            convertView = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.control_expandlist_item_child_seekbar, null);
                seekViewHolder = new SeekViewHolder(convertView, adjustsEntity);
                convertView.setTag(seekViewHolder);
            } else {
                seekViewHolder = (SeekViewHolder) convertView.getTag();
            }
            String adjustName = adjustsEntity.getHardwareInfo();
            if (adjustsEntity.getParamValue() != null && !adjustsEntity.getParamValue().isEmpty()) {
                seekViewHolder.setSeek(Integer.parseInt(adjustsEntity.getParamValue()));
            } else {
                seekViewHolder.setSeek(0);
            }
            seekViewHolder.setSeekName(adjustName);
            return convertView;
        }
//        /**
//         * 智能调节_2
//         */
//        if (childType == ItemType.智能调节_RB3.ordinal()) {
//            AdjustsEntity adjustsEntity = (AdjustsEntity) getChild(groupPosition, childPosition);
//
//            if (convertView == null) {
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.control_expandlist_item_child_lcd, null);
//                lcdViewHolder = new LcdViewHolder(convertView,adjustsEntity);
//                convertView.setTag(lcdViewHolder);
//            } else {
//                lcdViewHolder = (LcdViewHolder) convertView.getTag();
//            }
//
//            String adjustName = adjustsEntity.getAdjustName();
//            if ("风扇转速".equals(adjustName)){
//                lcdViewHolder.setTvName(adjustName);
//                lcdViewHolder.setRadioChecked(adjustsEntity.getAdjustValue());
//            }
//            return convertView;
//        }
        return convertView;
    }


    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if ("智能开关".equals(mGroupList.get(groupPosition))) {
            return ItemType.智能开关.ordinal();
        } else if ("智能调节".equals(mGroupList.get(groupPosition))) {

            AdjustsEntity entity = (AdjustsEntity) getChild(groupPosition, childPosition);
//
//            if ("风扇转速".equals(entity.getAdjustName())){
//                return ItemType.智能调节_RB3.ordinal();
//            }

            return ItemType.智能调节.ordinal();

        }

        return super.getChildType(groupPosition, childPosition);
    }

    @Override
    public int getChildTypeCount() {
        return ItemType.values().length;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupViewHolder {
        TextView tvGroupName;
        ImageView ivUp;
    }

    private class SwitchViewHolder {
        TextView tvSwitchName;
        SwichView aSwitch;
        LinearLayout llChart;
        LineChart mLineChart;

        SwitchViewHolder(View convertView, final SwitchsEntity switchsEntity) {
            tvSwitchName = convertView.findViewById(R.id.tv_name);
            aSwitch = convertView.findViewById(R.id.switch1);
            llChart = convertView.findViewById(R.id.ll_chart);
            mLineChart = convertView.findViewById(R.id.realChart);

            aSwitch.setOnClickListener(new SwichView.OnSwichViewClickListener() {
                @Override
                public void onSwichClicked(SwichView swichView) {
                    boolean open = swichView.isOpen();
                    switchsEntity.setParamState(open ? "1" : "0");
                    if (switchsEntity.getHardwareId() == 14) {
                        if (swichView.isOpen()) {
                            llChart.setVisibility(View.VISIBLE);
                        } else {
                            llChart.setVisibility(View.GONE);
                        }
                    }
                    //高度需要动态测量,写固定值也能解决父高度测量问题
                    ViewGroup.LayoutParams params = mListView.getLayoutParams();
                    params.height = 2000;
                    mListView.setLayoutParams(params);
                }
            });
            init();
        }

        private void init() {
            /**
             * 初始化lineChart
             */
            LineChartManager lineChartManager = new LineChartManager(mLineChart);
            mLineChart.getXAxis().setTextSize(8f);
            //不显示X轴
//            mLineChart.getXAxis().setEnabled(false);
            mLineChart.setNoDataText("还没有数据");
            mLineChart.getAxisRight().setEnabled(false);
            //设置一个自定义的最大值为这条轴，如果设置了，这个值将不会依赖于提供的数据自动计算。
//            mLineChart.getXAxis().setAxisMaximum(120f);
            //x轴标签旋转
            lineChartManager.setLabelRotationAngle(30);
            lineChartManager.setDescriptionEnable(false);
            lineChartManager.setYAxis(60, 0, 10, true);
            lineChartManager.setLegendEnable(false);
            if (xAXisDate.size() == 1 && xAXisDate.get(0) == 0 && yAXisData.get(0) == 0) {
                lineChartManager.setEmptyData();
                mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            } else {
                if (xAXisDate.size() > 0 && yAXisData.size() > 0) {
                    lineChartManager.showLineChart(xAXisDate, yAXisData, "温度（℃）", Color.GRAY);
                }
                if (xValsDate.size() > 0) {
                    lineChartManager.setXVals(xValsDate);
                }
                mLineChart.invalidate();
            }


        }

        public void setSwitchName(String switchName) {
            tvSwitchName.setText(switchName);
        }

        public void setSwitch(boolean open) {
            aSwitch.setOpen(open);
        }


    }

    /**
     * 加热座椅状态回调
     */
    public interface SeatSwitchStatusCallBack {
        void isOpen(boolean isOpen);
    }

    public void setSeatSwitchStatusCallBack(SeatSwitchStatusCallBack seatSwitchStatusCallBack) {
        this.seatSwitchStatusCallBack = seatSwitchStatusCallBack;
    }

    private class SeekViewHolder {
        TextView tvSeekName;
        TextView tvSeekNameProgress;
        SeekBar seekBar;

        SeekViewHolder(View view, final AdjustsEntity adjustsEntity) {
            seekBar = view.findViewById(R.id.seekbar);
            tvSeekName = view.findViewById(R.id.tv_seek_name);
            tvSeekNameProgress = view.findViewById(R.id.tv_seek_progress);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tvSeekNameProgress.setText(progress + "%");
                    adjustsEntity.setParamValue(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        private void setSeekName(String name) {
            tvSeekName.setText(name);
        }

        private void setSeek(int progress) {
            seekBar.setProgress(progress);
            tvSeekNameProgress.setText(progress + "%");
        }

        private int getSeekProgress() {
            return seekBar.getProgress();
        }

    }

    private class LcdViewHolder {
        TextView tvName;
        RadioGroup radioGroup;

        LcdViewHolder(View view, final AdjustsEntity adjustsEntity) {
            tvName = view.findViewById(R.id.tv_name);
            radioGroup = view.findViewById(R.id.rg);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rb_1:
                            adjustsEntity.setParamValue("低速");
                            break;
                        case R.id.rb_2:
                            adjustsEntity.setParamValue("中速");
                            break;
                        case R.id.rb_3:
                            adjustsEntity.setParamValue("高速");
                            break;
                        default:
                    }
                }
            });


        }


        private void setTvName(String name) {
            tvName.setText(name);
        }

        private void setRadioChecked(String type) {
            switch (type) {
                case "低速":
                    radioGroup.check(R.id.rb_1);
                    break;

                case "中速":
                    radioGroup.check(R.id.rb_2);
                    break;
                case "高速":
                    radioGroup.check(R.id.rb_3);
                    break;
                default:
            }

        }


    }


}

package com.pm.intelligent.module.scenes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.pm.intelligent.R;
import com.pm.intelligent.bean.ScenesEntity;
import com.pm.intelligent.bean.SettingValueEntity;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.widget.SwichView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by WeiSir on 2018/9/7.
 */
public class SceneListAdapter extends BaseAdapter {

    private Context mContext;

    private List<ScenesEntity> mScenesEntity = new ArrayList<>();

    public SceneListAdapter(Context mContext, List<ScenesEntity> mScenesEntity) {
        this.mContext = mContext;
        this.mScenesEntity = mScenesEntity;
    }

    public void setScenesEntity(List<ScenesEntity> mScenesEntity) {
        this.mScenesEntity = mScenesEntity;
    }

    public List<ScenesEntity> getScenesEntity() {
        return mScenesEntity;
    }

    @Override
    public int getCount() {
        return mScenesEntity.size();
    }

    @Override
    public Object getItem(int position) {
        return mScenesEntity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScenesEntity scenesEntity = mScenesEntity.get(position);
        ScenesHolder scenesHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.control_expandlist_item_child_switch_expand, null);
            scenesHolder = new ScenesHolder(convertView, scenesEntity);
            convertView.setTag(scenesHolder);
        } else {
            scenesHolder = (ScenesHolder) convertView.getTag();
        }

        scenesHolder.setName(scenesEntity.getHardwareName());
        if (scenesEntity.getSettingValueEntity() != null) {
            scenesHolder.setStartTime(scenesEntity.getSettingValueEntity().getStartTime());
            scenesHolder.setEndTime(scenesEntity.getSettingValueEntity().getEndTime());
        }
        return convertView;
    }


    private class ScenesHolder implements View.OnClickListener {
        private final ScenesEntity scenesEntity;
        SwichView swichView;
        TextView textView1;
        TextView textView2;
        TextView startTime;
        TextView endTime;
        RelativeLayout rlSeekbar;
        RelativeLayout rlStart;
        RelativeLayout rlEnd;
        SeekBar seekBar;
        LinearLayout ll_expand;

        ScenesHolder(View convertView, ScenesEntity scenesEntity) {
            swichView = convertView.findViewById(R.id.switch1);
            textView1 = convertView.findViewById(R.id.tv_name);
            textView2 = convertView.findViewById(R.id.tv_name2);
            startTime = convertView.findViewById(R.id.tv_start2);
            endTime = convertView.findViewById(R.id.tv_end2);
            rlSeekbar = convertView.findViewById(R.id.rl_seekbar);
            seekBar = convertView.findViewById(R.id.seekbar);
            ll_expand = convertView.findViewById(R.id.ll_expand);
            rlStart = convertView.findViewById(R.id.rl_start);
            rlEnd = convertView.findViewById(R.id.rl_end);

            this.scenesEntity = scenesEntity;
            init();

        }

        private void setName(String name) {
            textView1.setText(name);
        }

        private void setStartTime(String name) {
            startTime.setText(name);
        }

        private void setEndTime(String name) {
            endTime.setText(name);
        }

        private void showTimeSelectDialog(final int type) {

            String hourStr = type == 1 ? startTime.getText().toString() : endTime.getText().toString();
            int[] hour = CommonUtils.hour2int(hourStr);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour[0]);
            calendar.set(Calendar.MINUTE, hour[1]);

            //时间选择器
            TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    String time = CommonUtils.parseDate(date, "HH:mm");
                    if (type == 1) {
                        startTime.setText(time);
                        if (scenesEntity.getSettingValueEntity() != null) {
                            scenesEntity.getSettingValueEntity().setStartTime(time);
                        }
                    } else {
                        endTime.setText(time);
                        if (scenesEntity.getSettingValueEntity() != null) {
                            scenesEntity.getSettingValueEntity().setEndTime(time);
                        }
                    }
                    //notifyDataSetChanged();
                }
            }).setCancelText("取消")
                    .setSubmitText("确定")
                    .setSubmitColor(mContext.getResources().getColor(R.color.f333333))
                    .setCancelColor(mContext.getResources().getColor(R.color.f333333))
                    .isDialog(true)
                    .setLabel(null, null, null, "时", "分", null)
                    .setType(new boolean[]{false, false, false, true, true, false})
                    .setOutSideCancelable(true)
                    .setDate(calendar)
                    .build();
            pvTime.show();
        }


        private void init() {
            rlStart.setOnClickListener(this);
            rlEnd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_start:
                    showTimeSelectDialog(1);
                    break;
                case R.id.rl_end:
                    showTimeSelectDialog(2);
                    break;
                default:
                    break;
            }
        }
    }
}

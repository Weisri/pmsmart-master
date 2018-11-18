package com.pm.intelligent.module.alarm.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pm.intelligent.R;
import com.pm.intelligent.bean.AlarmInfoEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiSir on 2018/8/7.
 */
public class AlarmExListAdapter extends BaseExpandableListAdapter {
    private Activity mContext;
    private List<String> mGrpouplsit = new ArrayList<>();
    private List<List> mChildList= new ArrayList<>();
    private OnChildItemClick childItemClick;

    public AlarmExListAdapter(Activity mContext, List<String> mGrpouplsit, List<List> mChildList) {
        this.mContext = mContext;
        this.mGrpouplsit = mGrpouplsit;
        this.mChildList = mChildList;
    }

    @Override
    public int getGroupCount() {
        return mGrpouplsit.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<AlarmInfoEntity> itemBeanList = mChildList.get(groupPosition);
        return itemBeanList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGrpouplsit.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<AlarmInfoEntity> itemBeanList = mChildList.get(groupPosition);
        return itemBeanList.get(childPosition);
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
        GroupViewHolder viewHolder =null;
        if (convertView == null) {
            viewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.control_expandlist_item_parent, null);
            viewHolder.tvGroupName = convertView.findViewById(R.id.tv_parent_name);
            viewHolder.ivUp = convertView.findViewById(R.id.iv_up);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (GroupViewHolder) convertView.getTag();
        }
        viewHolder.tvGroupName.setText(mGrpouplsit.get(groupPosition));

        if (isExpanded) {
            viewHolder.ivUp.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.less));
        } else {
            viewHolder.ivUp.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.more_unfold));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alarm_list_item_info, null);
            childViewHolder.data = convertView.findViewById(R.id.tv_data);
            childViewHolder.time = convertView.findViewById(R.id.tv_time);
            childViewHolder.station = convertView.findViewById(R.id.tv_station);
            childViewHolder.alarmInfo = convertView.findViewById(R.id.tv_info);
            childViewHolder.status = convertView.findViewById(R.id.tv_isOk);
            childViewHolder.relativeLayout = convertView.findViewById(R.id.rl_item_alarm);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        AlarmInfoEntity infoBean = (AlarmInfoEntity) getChild(groupPosition, childPosition);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        String date = format.format(infoBean.getAlarmTime());
        String time1 = format1.format(infoBean.getAlarmTime());
        childViewHolder.data.setText(date);
        childViewHolder.time.setText(time1);
        childViewHolder.station.setText(infoBean.getShelterName());
        childViewHolder.alarmInfo.setText(infoBean.getAlarmName());
        if (infoBean.getAlarmStatus()==0) {
            childViewHolder.status.setText("未处理");
        } else if (infoBean.getAlarmStatus() == 1) {
            childViewHolder.status.setText("处理中");
        } else {
            childViewHolder.status.setText("已处理");
        }
//        childViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                childItemClick.onChildItemClick();
//            }
//        });
        return convertView;
    }

    private interface OnChildItemClick{
        void onChildItemClick();
    }

    public void setChildItemClick(OnChildItemClick childItemClick) {
        this.childItemClick = childItemClick;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupViewHolder{
        TextView tvGroupName;
        ImageView ivUp;
    }
    private class ChildViewHolder{
        TextView data,time,station,alarmInfo,status;
        RelativeLayout relativeLayout;
    }
}

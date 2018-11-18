package com.pm.intelligent.module.alarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pm.intelligent.R;
import com.pm.intelligent.bean.AlarmDetialBean;

import java.util.List;

/**
 * Created by WeiSir on 2018/7/17.
 */

public class AlarmInfoDetialsAapter extends BaseAdapter {
    private Context mContext;
    private List<AlarmDetialBean> mDatas;

    public AlarmInfoDetialsAapter(Context mContext, List<AlarmDetialBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alarminfo_list_item, null);
            viewHolder.tvDescribe = convertView.findViewById(R.id.tv_info);
            viewHolder.tvData = convertView.findViewById(R.id.tv_data);
            viewHolder.ivIndicaor = convertView.findViewById(R.id.iv_indicator);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvData.setText(mDatas.get(position).getTime());
        AlarmDetialBean bean = mDatas.get(position);
        if (bean.getStatus()==0) {
            viewHolder.tvDescribe.setText(mContext.getText(R.string.unHandle));
            viewHolder.ivIndicaor.setBackground(mContext.getResources().getDrawable(R.drawable.alarm_indicator_footer));
            return convertView;
        }
        if (bean.getStatus() == 1) {
            viewHolder.tvDescribe.setText(mContext.getText(R.string.handling));
            viewHolder.ivIndicaor.setBackground(mContext.getResources().getDrawable(R.drawable.alarm_indicator));
            return convertView;
        }
        if (bean.getStatus() == 2) {
            viewHolder.tvDescribe.setText(mContext.getText(R.string.handled));
            viewHolder.ivIndicaor.setBackground(mContext.getResources().getDrawable(R.drawable.alarm_indicator_header));
        }
        return convertView;
    }

    private class ViewHolder{
        TextView tvDescribe,tvData;
        ImageView ivIndicaor;
    }
}

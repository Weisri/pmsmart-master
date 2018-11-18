package com.pm.intelligent.module.faultTracking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pm.intelligent.R;
import com.pm.intelligent.bean.FaultTrackEntity;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by pumin on 2018/8/11.
 */

public class FaultTrackingListAdapter extends BaseAdapter {
    private List<FaultTrackEntity> mList;
    private Context mContext;


    public FaultTrackingListAdapter(List<FaultTrackEntity> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_fault_tracking_list_item, null);
            viewHolder.data = view.findViewById(R.id.tv_fault_data);
            viewHolder.time = view.findViewById(R.id.tv_fault_time);
            viewHolder.station = view.findViewById(R.id.tv_fault_station);
            viewHolder.alarmInfo = view.findViewById(R.id.tv_fault_info);
            viewHolder.status = view.findViewById(R.id.tv_fault_isOk);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        FaultTrackEntity infoBean = mList.get(i);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        String time = format.format(infoBean.getTroubleTime());
        String time1 = format1.format(infoBean.getTroubleTime());
        viewHolder.data.setText(time);
        viewHolder.time.setText(time1);
        viewHolder.station.setText(infoBean.getShelterName());
        if (infoBean.getTroubleStatus()==0) {
            viewHolder.status.setText("未修复");
        } else if (infoBean.getTroubleStatus() == 1) {
            viewHolder.status.setText("修复中");
        } else {
            viewHolder.status.setText("已修复");
        }
        viewHolder.alarmInfo.setText(infoBean.getTroubleName());
        return view;
    }

    public class ViewHolder {
        TextView data, time, station, alarmInfo, status;
    }
}

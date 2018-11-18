package com.pm.intelligent.module.scenes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pm.intelligent.R;
import com.pm.intelligent.bean.ProjectEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiSir on 2018/9/10.
 */
public class SpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private List<ProjectEntity> mDataList = new ArrayList<>();

    public SpinnerAdapter(Context mContext, List<ProjectEntity> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.scene_spinner_item, null);
            holder.projectName = convertView.findViewById(R.id.tv_project);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProjectEntity entity = mDataList.get(position);
        holder.projectName.setText(entity.getProjectName());
        return convertView;
    }

    class ViewHolder {
        TextView projectName;
    }
}

package com.pm.intelligent.module.hardware;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WeiSir on 2018/7/12.
 * ExpandableListView adapter
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<HardWareGroupBean> mGroupList = new ArrayList<>();
    private List<List> mChildList = new ArrayList<>();
    private Map<String, List<HardWareItemBean>> datasets = new HashMap<>();
    private String[] pList;


    public MyExpandableListAdapter(Context mContext, Map<String, List<HardWareItemBean>> datasets, String[] pList) {
        this.mContext = mContext;
        this.datasets = datasets;
        this.pList = pList;
    }

    @Override
    public int getGroupCount() {
        if (datasets == null) {
            return 0;
        }
        return datasets.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        List<HardWareItemBean> itemBeanList = mChildList.get(groupPosition);
//        return itemBeanList.size();
        if (datasets.get(pList[groupPosition]) == null) {
            return 0;
        }
        return datasets.get(pList[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datasets.get(pList[groupPosition]);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
//        List<HardWareItemBean> itemBeanList = mChildList.get(groupPosition);
//        return itemBeanList.get(childPosition);
        return datasets.get(pList[groupPosition]).get(childPosition);
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
        GroupViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.control_expandlist_item_parent, null);
            viewHolder.tvGroupName = convertView.findViewById(R.id.tv_parent_name);
            viewHolder.ivUp = convertView.findViewById(R.id.iv_up);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
//        HardWareGroupBean groupBean = mGroupList.get(groupPosition);
//        viewHolder.tvGroupName.setText(groupBean.getHardwareType());
        viewHolder.tvGroupName.setText(pList[groupPosition]);
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
        convertView = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.box_list_result_item, null);
            childViewHolder.tvName = convertView.findViewById(R.id.tv_1);
            childViewHolder.tvIsOk = convertView.findViewById(R.id.tv_2);
            childViewHolder.tvStatus = convertView.findViewById(R.id.tv_3);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
//        HardWareItemBean itemBean = (HardWareItemBean) getChild(groupPosition,childPosition);
        HardWareItemBean itemBean = datasets.get(pList[groupPosition]).get(childPosition);
        String name = itemBean.getHardwareInfo();
        childViewHolder.tvName.setText(name);

//        childViewHolder.tvIsOk.setText(itemBean.getParamState());

        if ( itemBean.getParamValue() != null && !itemBean.getParamValue().equals("null")) {
            childViewHolder.tvStatus.setText(itemBean.getParamValue());
            switch (name) {
                case "湿度":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "%");
                    break;
                case "温度":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "℃");
                    break;
                case "PM2.5":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "μg/m³");
                    break;
                case "噪音":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "分贝");
                    break;
                case "风力":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "级");
                    break;
                case "风向":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "");
                    break;
                case "PM10":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "μg/m³");
                    break;
                case "气压":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "百帕");
                    break;
                case "电流":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "A");
                    break;
                case "电压":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "V");
                    break;
                case "电能":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "J");
                    break;
                case "电功率":
                    childViewHolder.tvStatus.setText(itemBean.getParamValue() + "w");
                    break;
                default:
                    childViewHolder.tvStatus.setText(itemBean.getParamValue());
                    break;
            }
        } else {
            childViewHolder.tvStatus.setText("--");
        }





        if (itemBean.getParamState() != null && !itemBean.getParamState().equals("null")) {
            if (itemBean.getParamState().equals("1")) {
                childViewHolder.tvIsOk.setTextColor(mContext.getResources().getColor(R.color.text_red));
                childViewHolder.tvIsOk.setText("异常");
            } else {
                childViewHolder.tvIsOk.setTextColor(mContext.getResources().getColor(R.color.text_black));
                childViewHolder.tvIsOk.setText("正常");
            }
        } else {
            childViewHolder.tvIsOk.setTextColor(mContext.getResources().getColor(R.color.text_black));
            childViewHolder.tvIsOk.setText("--");
        }
        if (itemBean.getHardwareInfo().equals("网络状态")) {
            childViewHolder.tvStatus.setBackgroundResource(R.mipmap.signal4);
            childViewHolder.tvStatus.setText("");
        } else {
            childViewHolder.tvStatus.setBackgroundResource(0);
        }
        if (itemBean.getHardwareInfo().equals("风向")) {
            childViewHolder.tvIsOk.setVisibility(View.INVISIBLE);
        } else {
            childViewHolder.tvIsOk.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class GroupViewHolder {
        TextView tvGroupName;
        ImageView ivUp;
    }

    private class ChildViewHolder {
        TextView tvName;
        TextView tvStatus;
        TextView tvIsOk;
    }
}

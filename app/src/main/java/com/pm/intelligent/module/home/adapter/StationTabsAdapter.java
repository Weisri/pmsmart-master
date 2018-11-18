package com.pm.intelligent.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pm.intelligent.R;
import com.pm.intelligent.bean.HomeShelterEntity;
import com.pm.intelligent.bean.HomeStationsEntity;
import com.pm.intelligent.module.home.StationItemSelectListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by WeiSir on 2018/6/25.
 *
 */

public class StationTabsAdapter extends BaseAdapter  {
    private Context mContext;
    private StationItemSelectListener mListener;
    private List<HomeShelterEntity> mStationsBeanList = new ArrayList<>();
    private int selectItem = -1;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
    private LayoutInflater inflater;
    private TreeSet<Integer> set = new TreeSet<Integer>();


    public StationTabsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setStationsBeanList(List<HomeShelterEntity> mStationsBeanList) {
        this.mStationsBeanList = mStationsBeanList;
    }

    public void setStationItemListener(StationItemSelectListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        return mStationsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStationsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
//        int type = mStationsBeanList.get(position).getProjectId();
//        if (type==0) {
//            return 0;
//        } else if (type==1) {
//            return 1;
//        }
//        return 1;

        return set.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type==0) {
            //站台
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.home_list_item_stations_tabs, null);
                holder.tvName = convertView.findViewById(R.id.tv_station_name);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvName.setText(mStationsBeanList.get(position).getShelterName());

            if (selectItem == position) {
                holder.tvName.setSelected(true);
                holder.tvName.setPressed(true);
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.toolbar_bg));
                holder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.all_bg));

            } else {
                holder.tvName.setSelected(false);
                holder.tvName.setPressed(false);
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.slide_list_head_bg));

            }
            holder.tvName.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    mListener.onStationItemSelect(position);
                }
            });
//        } else if (type==1) {
//            //项目名
//            ViewHolder2 viewHolder2 = null;
//            if (convertView == null) {
//                viewHolder2 = new ViewHolder2();
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.home_list_item_station_group, null);
//                viewHolder2.tvGroup = convertView.findViewById(R.id.tv_station_group);
//                convertView.setTag(viewHolder2);
//            } else {
//                viewHolder2 = (ViewHolder2) convertView.getTag();
//            }
//            viewHolder2.tvGroup.setText(mStationsBeanList.get(position).getShelterName());
        }
        return convertView;
    }



    private class  ViewHolder{
         TextView tvName;
    }

    private class ViewHolder2 {
        TextView tvGroup;
    }
}

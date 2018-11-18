package com.pm.intelligent.module.patrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pm.intelligent.R;
import com.pm.intelligent.bean.PatrolResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiSir on 2018/9/13.
 */
public class PollingPatrolRecAdapter extends RecyclerView.Adapter<PollingPatrolRecAdapter.MyViewHolder>  {
    private Context mContext;
    private List<PatrolResultBean> mList = new ArrayList<>();


    public PollingPatrolRecAdapter(Context mContext, List<PatrolResultBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.patrol_recycle_patroling_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PatrolResultBean resultBean = mList.get(position);
        holder.mStationName.setText(resultBean.getmStationName());
        holder.mFinish.setText("检测完成");
        if (resultBean.getCheckStatus() == 0) {
            holder.mIvRight.setImageResource(R.mipmap.patrol_item_ok);
        } else {
            holder.mIvRight.setImageResource(R.mipmap.patrol_item_error);
        }
    }

    /**
     * Add item.添加条目
     * @param position the position
     */
    public void addItem(int position) {
        mList.add(position, mList.get(position));
        notifyItemInserted(position);
    }

    /**
     * Remove item.删除条目
     *
     * @param position the position
     */
    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mStationName;
        TextView mFinish;
        ImageView mIvRight;
        public MyViewHolder(View itemView) {
            super(itemView);
            mStationName =  itemView.findViewById(R.id.tv_stationName);
            mFinish = itemView.findViewById(R.id.tv_finish);
            mIvRight = itemView.findViewById(R.id.iv_right);
        }
    }
}

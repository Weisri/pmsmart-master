package com.pm.intelligent.module.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pm.intelligent.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiSir on 2018/6/28.
 * 搜索recyclerView adapter
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private OnItemClickListener itemClickListener;

    //填充历史数据
    private List<String> mHistoryList;
    public List<String> getmHistoryList() {
        return mHistoryList;
    }

    public void setmHistoryList(List<String> mHistoryList) {
        this.mHistoryList = mHistoryList;
    }

    public interface OnItemClickListener {
        void OnItemClick(View view,int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * Instantiates a new My recycler adapter.
     *
     * @param mContext the m context
     */
    public MyRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        mHistoryList = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e("tag", "onBindViewHolder: "+mHistoryList.get(position) );
            holder.mSearchWord.setText(mHistoryList.get(position));
            holder.mSearchWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.OnItemClick(v,position);
                }
            });
    }

    @Override
    public int getItemCount() {
        Log.e("aaa", "getItemCount: "+mHistoryList.size() );
        return mHistoryList.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder ;
        viewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.home_search_recyclerview_item, parent, false));
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mSearchWord;

        public ViewHolder(View itemView) {
            super(itemView);
            mSearchWord = (TextView) itemView.findViewById(R.id.tv_head);
        }
    }
}

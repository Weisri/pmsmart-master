package com.pm.intelligent.module.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pm.intelligent.R;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.HomeGridItemLayoutUtil;
import com.pm.intelligent.utils.ScreenUtils;

import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by WeiSir on 2018/7/20.
 */

public class HomeGridAdapter extends BaseAdapter {
    private Context mContext;
    private int[] menuIcons = new int[]{};
    private String[] menuNames = new String[]{};
    private LinearLayout.LayoutParams params;
    /**
     * 首页 GridView item动态布局设置工具类
     */
    private HomeGridItemLayoutUtil mGridItemUtil;

    public HomeGridAdapter(Context mContext, int[] menuIcons, String[] menuNames) {
        this.mContext = mContext;
        this.menuIcons = menuIcons;
        this.menuNames = menuNames;
//        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.CENTER;
        mGridItemUtil = HomeGridItemLayoutUtil.create(mContext);
    }

    @Override
    public int getCount() {
        return menuIcons.length;
    }

    @Override
    public Object getItem(int position) {
        return menuIcons[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint("CutPasteId")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TopViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new TopViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_grid_item_type1, null);
            //设置圆点提示
            viewHolder.badge = new QBadgeView(mContext).bindTarget(convertView.findViewById(R.id.rl_bg));
            viewHolder.badge.setBadgeTextSize(12, true);

            viewHolder.background = convertView.findViewById(R.id.rl_bg);
            viewHolder.name = convertView.findViewById(R.id.tv_grid_item_top);
            convertView.setTag(viewHolder);
            //动态设置GridView的item布局宽高
            mGridItemUtil.layoutItem(position, convertView);
        } else {
            viewHolder = (TopViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(menuNames[position]);
        Drawable drawable = mContext.getResources().getDrawable(menuIcons[position]);
        drawable.setBounds(0,
                0,
                ScreenUtils.dp2px(mContext, 45),
                ScreenUtils.dp2px(mContext, 45));
        viewHolder.name.setCompoundDrawables(null, drawable, null, null);

        switch (position) {
            case 0:
                viewHolder.background.setBackgroundResource(R.drawable.shap_home_tiem_bg);
                break;
            case 1:
                viewHolder.background.setBackgroundResource(R.drawable.shap_home_item_bg_2);
                break;
            case 2:
                viewHolder.background.setBackgroundResource(R.drawable.shap_home_item_bg_3);
                break;
            case 3:
                viewHolder.background.setBackgroundResource(R.drawable.shap_home_item_bg_6);
                break;
            case 4:
                viewHolder.background.setBackgroundResource(R.drawable.shap_home_item_bg_5);
                break;
            case 5:
                viewHolder.background.setBackgroundResource(R.drawable.shap_home_item_bg_10);
                break;
            case 6:
                viewHolder.background.setBackgroundResource(R.drawable.shap_home_item_bg_4);
                viewHolder.badge.setBadgeNumber(0);
                break;
            case 7:
                viewHolder.background.setBackgroundResource(R.drawable.shap_home_item_bg_7);
                break;
            case 8:
                viewHolder.background.setBackgroundResource(R.drawable.shap_home_item_bg_8);
                viewHolder.badge.setBadgeNumber(0);
                break;
            default:
                break;
        }
        return convertView;
    }


    private class TopViewHolder {
        LinearLayout background;
        TextView name;
        Badge badge;
    }


}

package com.pm.intelligent.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pm.intelligent.R;
import com.pm.intelligent.utils.ScreenUtils;


/**
 * Created by pumin on 2018/8/10.
 */

public enum HomePop {
    INSTANCE;
    private View view;
    private PopupWindow popupWindow;

    public void showPop(Context context, View parentView, final PopListener popListener) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.pop_main, null);
        }

        TextView tv1 = view.findViewById(R.id.tv1);
        TextView tv2 = view.findViewById(R.id.tv2);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    // ActivityUtil.INSTANCE.startActivity(PatrolActivity.class);
                    popListener.groupCheck();
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    //TODO 点检
                    popListener.singleCheck();
                }
            }
        });

        if (popupWindow == null) {
            popupWindow = new PopupWindow(view, 300, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
        }

        int[] location = new int[2];
        parentView.getLocationOnScreen(location);
        //相对某个控件的位置，有偏移;xoff表示x轴的偏移，正值表示向左，负值表示向右；yoff表示相对y轴的偏移，正值是向下，负值是向上；
        Log.e("logger", "showPop: "+location[0] +"   "+ ScreenUtils.getScreenWidth(context));
        popupWindow.showAsDropDown(parentView,0,0);
        //popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, location[0]-300+(ScreenUtils.getScreenWidth(context)-location[0]), location[1]+100);
    }

    public interface PopListener {
        void groupCheck();

        void singleCheck();
    }
}

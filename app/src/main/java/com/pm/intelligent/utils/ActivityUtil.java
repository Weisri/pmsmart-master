package com.pm.intelligent.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;

import java.util.ArrayList;



/**
 * Created by Struggle on 2017/2/25 10:55
 * Author mailbox zhuhuitao_struggle@163.com
 */

public enum  ActivityUtil {
    INSTANCE;

    private static ActivityUtil instance;




    /**
     * 不携带参数
     *
     * @param c
     */
    public void startActivity(Class c) {
        Activity activity = (Activity) BaseActivity.mContext;
        Intent intent = new Intent(activity, c);
        activity.startActivity(intent);
        doActivityAnimation(activity);

    }

    public void startActivity(Class c, CharSequence str) {
        CharSequence result = str;
        Activity activity = (Activity) BaseActivity.mContext;
        Intent intent = new Intent(activity, c);
        intent.putExtra("charSequence", result);
        activity.startActivity(intent);
        doActivityAnimation(activity);
        result = null;
    }

    /**
     * 携带对象
     *
     * @param c
     * @param data
     */
    public void startActivity(Class c, Parcelable data) {
        Activity activity = (Activity) BaseActivity.mContext;
        Intent intent = new Intent(activity, c);
        intent.putExtra("ent", data);
        activity.startActivity(intent);
        doActivityAnimation(activity);
    }

    public void startActivity(Class c, ArrayList<? extends Parcelable> list) {
        Activity activity = (Activity) BaseActivity.mContext;
        Intent intent = new Intent(activity, c);
        intent.putParcelableArrayListExtra("ent", list);
        activity.startActivity(intent);
        doActivityAnimation(activity);
    }

    /**
     * activity跳转动画
     *
     * @param activity
     */
    private void doActivityAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


}

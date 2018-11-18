package com.pm.intelligent.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/12.
 */

public class CommonUtils {


    public static String token;
    public static String iccid = "2";
    public static String shelterName = "";
    //加热座椅是否打开
    public static boolean isSeatOpen;

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static float getVersionCode(Context mContext) {
        float versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        return String.valueOf(System.currentTimeMillis());
    }


    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNum
     */
    public static void diallPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }


    /**
     * @param time
     * @return
     */
    public static String parseDate(long time) {
        return parseDate(time, "yyyy-MM-dd");
    }

    /**
     * @param time
     * @return
     */
    public static String parseDate(Date time) {
        return parseDate(time, "yyyy-MM-dd");
    }

    /**
     * 时间戳
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String parseDate(long time, String pattern) {
        SimpleDateFormat sdr = new SimpleDateFormat(pattern);
        @SuppressWarnings("unused")
        String times = sdr.format(new Date(time));
        return times;
    }

    public static String parseDate(Date time, String pattern) {
        SimpleDateFormat sdr = new SimpleDateFormat(pattern);
        @SuppressWarnings("unused")
        String times = sdr.format(time);
        return times;
    }


    /**
     * 流转字符串
     *
     * @param inputStream
     * @return
     */
    public static String stream2String(InputStream inputStream) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 启动默认无数据活动
     *
     * @param context
     * @param clazz
     */
    public static void actionStart(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public static void setWindowAlpha(Activity context,float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        context.getWindow().setAttributes(lp);

    }


    public static int[] hour2int(String startTime) {
        String[] split = new String[0];
        try {
            split = startTime.split(":");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{Integer.parseInt(split[0]),Integer.parseInt(split[1])};
    }
}

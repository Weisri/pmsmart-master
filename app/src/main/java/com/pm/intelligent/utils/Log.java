package com.pm.intelligent.utils;



import java.util.MissingFormatArgumentException;

/**
 * Created by pumin on 2018/7/13.
 */

public final class Log {


    public static void i(String msg, Object... args) {
        try {
            if (BuildConfig.DEBUG) {
                android.util.Log.d(BuildConfig.TAG, String.format(msg, args));
            }
        } catch (MissingFormatArgumentException e) {
            android.util.Log.e(BuildConfig.TAG, "com.pm.intelligent" + e);
            android.util.Log.i(BuildConfig.TAG, msg);
        }

    }

    public static void i(Object... args) {
        try {
            android.util.Log.i(BuildConfig.TAG, String.format(BuildConfig.TAG, args));
        } catch (MissingFormatArgumentException e) {
            android.util.Log.e(BuildConfig.TAG, "com.pm.intelligent" + e);
            android.util.Log.i(BuildConfig.TAG, "日志输出错误！！！！");
        }
    }
}

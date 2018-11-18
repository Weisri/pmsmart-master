package com.pm.intelligent;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.base.BaseApplication;
import com.pm.intelligent.greendao.DaoMaster;
import com.pm.intelligent.greendao.DaoSession;
import com.pm.okgolibrary.okInit.OkUtil;

/**
 * Created by WeiSir on 2018/6/20.
 */

public class MyApplication extends BaseApplication {
    private static MyApplication sInstances;
    private static DaoSession mDaoSession;
    private static Context context;
    public static Gson gson;

    public static MyApplication getsInstances() {
        return sInstances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstances = this;
        gson = new Gson();

//        initLeakCanary();
        initLogger();
        initGreenDao();
        OkUtil.INSTANCE.initOKGo(getsInstances());
    }

    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "proprietor.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

    //    //内存泄漏检查工具
//    private void initLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//    }


}

package com.pm.nettyService;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.pm.intelligent.module.home.activity.MainActivity;

public class ServiceManager {

    private NettyService mNettyService;


    private ServiceManager(MainActivity activity,String ip,int port) {
        initService(activity,ip,port);
    }

    private static volatile ServiceManager instance;


    public NettyService getNettyService() {
        return mNettyService;
    }

    public static ServiceManager registerInstance(MainActivity activity,String ip,int port) {
        if (instance == null) {
            synchronized (ServiceManager.class) {
                if (instance == null) {
                    instance = new ServiceManager(activity,ip,port);
                }
            }
        }
        return instance;

    }

    public void initService(MainActivity activity,String ip,int port){
        Intent intent = new Intent(activity, NettyService.class);
        Bundle bundle = new Bundle();
        com.orhanobut.logger.Logger.d(ip+ "     " +port);
        bundle.putString("ip",ip);
        bundle.putInt("port",port);
        intent.putExtras(bundle);
        activity.bindService(intent, nettyConn, activity.BIND_AUTO_CREATE);
    }

    private ServiceConnection nettyConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof NettyService.MyBinder) {
                NettyService.MyBinder binder = (NettyService.MyBinder) service;
                mNettyService = binder.getNettyService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { }
    };
}

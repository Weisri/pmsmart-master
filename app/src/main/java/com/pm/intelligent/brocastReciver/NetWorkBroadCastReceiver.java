package com.pm.intelligent.brocastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.pm.intelligent.utils.net.EventManager;





public class NetWorkBroadCastReceiver extends BroadcastReceiver {
    private NetWorkListener netEvent ;

    public void setNetEvent(NetWorkListener netEvent){
        this.netEvent = netEvent ;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            //检查网络状态的类型
            int netWorkState = EventManager.getInstance().getNetWorkState(context);
            if (netEvent != null)
                // 接口回传网络状态的类型
                netEvent.onNetChange(netWorkState);
        }
    }


}

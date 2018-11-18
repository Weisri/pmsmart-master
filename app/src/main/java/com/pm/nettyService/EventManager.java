package com.pm.nettyService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class EventManager {
    private static final EventManager eventManager = new EventManager();

    private EventManager (){

    }
    public static synchronized EventManager getInstance(){

        return eventManager ;

    }

    /**
     * 没有连接网络
     */
    private static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    private static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    private static final int NETWORK_WIFI = 1;
    /**
     * 互联网
     */
    private static final int TYPE_ETHERNET = 9;

    public int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            int type = activeNetworkInfo.getType();
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET){
                return TYPE_ETHERNET;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    private String event ;
    public static String REFRESH_CHAT_CONTRACTS ="REFRESH_CHAT_CONTRACTS";
    public static String CONNECT_TO_SERVER ="CONNECT_TO_SERVER";
    public static String REFRESH_GROUP_MEMBER ="REFRESH_GROUP_MEMBER";
    public static String GROUP_CHAT_DISSOLVED ="GROUP_CHAT_DISSOLVED";

}

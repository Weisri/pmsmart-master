package com.pm.intelligent;


import com.pm.intelligent.utils.SPUtils;

public class UrlConstant {

    /**
     * 测试服
     */
//    String SERVER_IP_TEST = "192.168.1.118";
    public static final int SERVER_PORT_TEST = 10012;

    /**
     * 正式服
     */
    public static final String SERVER_IP = "47.99.157.232";
    public static final int SERVER_PORT = 10012;

    /**
     * netty登录用户名
     */
    public static final String ICCID = SPUtils.getInstance(MyApplication.getsInstances().getApplicationContext()).getUserId();
    public static final String TOKEN = SPUtils.getInstance(MyApplication.getsInstances().getApplicationContext()).getUserPwd();
}
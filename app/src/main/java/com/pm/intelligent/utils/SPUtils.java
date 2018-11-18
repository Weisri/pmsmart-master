package com.pm.intelligent.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharedPreferences工具类(单例模式)
 */
public class SPUtils {
    private static final String SP_NAME = "IMChat";
    private static final String USER_ID = "USER_ID";
    private static final String USER_PWD = "USER_PWD";
    private static final String LOGIN_STATE = "LOGIN_STATE";
    private static final String RECONNECT = "RECONNECT";
    private static final String USER_TOKEN = "USER_TOKEN";
    private static SPUtils mSpUtils;
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private SPUtils(Context context) {
        this.context = context;
        sp = this.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SPUtils getInstance(Context context) {

        if (mSpUtils == null) {
            synchronized (SPUtils.class) {
                if (mSpUtils == null) {
                    mSpUtils = new SPUtils(context);
                    return mSpUtils;
                }
            }
        }

        return mSpUtils;

    }

    public void  setUserId(String userId){
        editor.putString(USER_ID,userId);
        editor.commit();
    }

    public String getUserId(){
        return sp.getString(USER_ID, null);
    }

    public void setUserPwd(String pwd) {
        editor.putString(USER_PWD, pwd);
        editor.commit();
    }

    public  String getUserPwd() {
        return sp.getString(USER_PWD,"");
    }

    public void setUserToken(String userToken){
        editor.putString(USER_TOKEN,userToken);
        editor.commit();
    }

    public String getUserToken(){
        return sp.getString(USER_TOKEN,"");
    }

    public void setLoginState(int state){
        editor.putInt(LOGIN_STATE,state);
    }

    public int getLoginState(){
        return sp.getInt(LOGIN_STATE, 0);
    }

    public void isReconnect(boolean can){
        editor.putBoolean(RECONNECT,can);
    }

    public boolean reconnect(){
        return sp.getBoolean(RECONNECT, false);
    }

}
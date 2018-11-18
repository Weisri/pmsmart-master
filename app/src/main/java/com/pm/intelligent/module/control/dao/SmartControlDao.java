package com.pm.intelligent.module.control.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.bean.AdjustsEntity;
import com.pm.intelligent.bean.ScenesEntity;
import com.pm.intelligent.bean.SwitchsEntity;
import com.pm.intelligent.greendao.AdjustsEntityDao;
import com.pm.intelligent.greendao.DaoSession;
import com.pm.intelligent.greendao.ScenesEntityDao;
import com.pm.intelligent.greendao.SwitchsEntityDao;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.pm.okgolibrary.okInit.OkGoManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WeiSir on 2018/8/29.
 */
public class SmartControlDao {

    /**
     * Do request switch.
     *  智能开关
     * @param url      the url
     * @param params   the params
     * @param callBack the call back
     */
    public static void doRequestSwitch(String url, HashMap<String, String> params, final OkCallBack callBack) {
        OkGoManager.INSTANCE.doPost(url, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                String strObj = (String) obj;
                Logger.d(strObj);
                //解决Gson解析date格式问题
                GsonBuilder builder = new GsonBuilder();
                // Register an adapter to manage the date types as long values
                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });
                Gson gson = builder.create();
                try {
                    JSONObject jsonObject = new JSONObject(strObj);
                    String data = jsonObject.getString("data");

                    Logger.d(data);

                    List<SwitchsEntity> list = new ArrayList<>();
                    SwitchsEntity entity = null;
                    DaoSession daoSession = MyApplication.getDaoSession();
                    SwitchsEntityDao entityDao = daoSession.getSwitchsEntityDao();
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        entity = gson.fromJson(array.getString(i), SwitchsEntity.class);
                        list.add(entity);
                        entityDao.insertOrReplace(entity);
                    }
                    callBack.success(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(int error, Object obj) {
                callBack.failed(error, obj);
            }
        });

    }

    /**
     * Do request scence.
     * 情景智能
     * @param url      the url
     * @param params   the params
     * @param callBack the call back
     */
    public static void doRequestScence(String url, HashMap<String, String> params, final OkCallBack callBack) {
        OkGoManager.INSTANCE.doPost(url, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                String strObj = (String) obj;
                Logger.d(strObj);
                //解决Gson解析date格式问题
                GsonBuilder builder = new GsonBuilder();
                // Register an adapter to manage the date types as long values
                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });
                Gson gson = builder.create();
                try {
                    JSONObject jsonObject = new JSONObject(strObj);
                    String data = jsonObject.getString("data");
                    List<ScenesEntity> list = new ArrayList<>();
                    ScenesEntity entity = null;
                    DaoSession daoSession = MyApplication.getDaoSession();
                    ScenesEntityDao entityDao = daoSession.getScenesEntityDao();
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        entity = gson.fromJson(array.getString(i), ScenesEntity.class);
                        list.add(entity);
                        entityDao.insertOrReplace(entity);
                    }
                    callBack.success(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(int error, Object obj) {
                callBack.failed(error, obj);
            }
        });

    }

    /**
     * Do request adjust.
     * 总电源开关
     * @param url      the url
     * @param params   the params
     * @param callBack the call back
     */
    public static void doRequestAdjust(String url, HashMap<String, String> params, final OkCallBack callBack) {
        OkGoManager.INSTANCE.doPost(url, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                String strObj = (String) obj;
                Logger.d(strObj);
                //解决Gson解析date格式问题
                GsonBuilder builder = new GsonBuilder();
                // Register an adapter to manage the date types as long values
                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });
                Gson gson = builder.create();
                try {
                    JSONObject jsonObject = new JSONObject(strObj);
                    String data = jsonObject.getString("data");
                    List<AdjustsEntity> list = new ArrayList<>();
                    AdjustsEntity entity = null;
                    DaoSession daoSession = MyApplication.getDaoSession();
                    AdjustsEntityDao entityDao = daoSession.getAdjustsEntityDao();
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        entity = gson.fromJson(array.getString(i), AdjustsEntity.class);
                        list.add(entity);
                        entityDao.insertOrReplace(entity);
                    }
                    callBack.success(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(int error, Object obj) {
                callBack.failed(error, obj);
            }
        });

    }

}

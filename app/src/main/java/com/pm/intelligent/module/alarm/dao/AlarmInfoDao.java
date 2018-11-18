package com.pm.intelligent.module.alarm.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.bean.AlarmInfoEntity;
import com.pm.intelligent.greendao.AlarmInfoEntityDao;
import com.pm.intelligent.greendao.DaoSession;
import com.pm.intelligent.utils.AESUtil;
import com.pm.intelligent.utils.AppUtils;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.pm.okgolibrary.okInit.OkGoManager;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WeiSir on 2018/8/28.
 */
public class AlarmInfoDao {
    public static void doRequsetAlarm(String url, HashMap<String, String> params, final OkCallBack callBack) {
        OkGoManager.INSTANCE.doPost(url, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                String strObj = (String) obj;
                Logger.json(strObj);
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
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        String data = jsonObject.getString("data");
                        String aesDecrypt = AESUtil.aesDecrypt(data);
                        Logger.d("解密后" + aesDecrypt);
                        List<AlarmInfoEntity> list = new ArrayList<>();
                        AlarmInfoEntity entity = null;
                        DaoSession daoSession = MyApplication.getDaoSession();
                        AlarmInfoEntityDao entityDao = daoSession.getAlarmInfoEntityDao();
                        JSONArray array = new JSONArray(aesDecrypt);
                        for (int i = 0; i < array.length(); i++) {
                            entity = AppUtils.getGson().fromJson(array.getString(i), AlarmInfoEntity.class);
                            list.add(entity);
                            entityDao.insertOrReplace(entity);
                        }
                        callBack.success(list);
                    } else if (status == 201 || status == 203) {
                        callBack.failed(status, obj);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.failed(201, obj);
                }
            }

            @Override
            public void failed(int error, Object obj) {
                callBack.failed(error, obj);
            }
        });
    }

    /**
     * Do unique list.
     * 条件查询
     *
     * @param status the key 修复状态
     * @param type   the alarm type
     * @return the list 返回实体集合
     */
    public static List<AlarmInfoEntity> doUnique(int status, String type) {
        DaoSession mDaoSession = MyApplication.getDaoSession();
        AlarmInfoEntityDao mEntityDao = mDaoSession.getAlarmInfoEntityDao();
        List<AlarmInfoEntity> list = new ArrayList<>();
        QueryBuilder<AlarmInfoEntity> qb = mEntityDao.queryBuilder()
                .where(AlarmInfoEntityDao.Properties.AlarmStatus.eq(status), AlarmInfoEntityDao.Properties.AlarmType.eq(type));
        for (AlarmInfoEntity entity : qb.list()) {
            list.add(entity);
        }
        return list;
    }
}

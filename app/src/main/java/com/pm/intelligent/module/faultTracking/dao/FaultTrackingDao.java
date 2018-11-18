package com.pm.intelligent.module.faultTracking.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.bean.FaultTrackEntity;
import com.pm.intelligent.greendao.DaoSession;
import com.pm.intelligent.greendao.FaultTrackEntityDao;
import com.pm.intelligent.utils.AESUtil;
import com.pm.intelligent.utils.AppUtils;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.pm.okgolibrary.okInit.OkGoManager;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WeiSir on 2018/8/27.
 */
public class FaultTrackingDao {


    /**
     * Do request fault track.
     * @param url      the url
     * @param params   the params
     * @param callBack the call back
     */
    public static void doRequestFaultTrack(String url, HashMap<String,String> params, final OkCallBack callBack) {
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
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        String data = jsonObject.getString("data");
                        String aesDecrypt = AESUtil.aesDecrypt(data);
                        Logger.d("解密后"+ aesDecrypt);
                        List<FaultTrackEntity> list = new ArrayList<>();
                        FaultTrackEntity entity = null;
                        DaoSession daoSession = MyApplication.getDaoSession();
                        FaultTrackEntityDao entityDao = daoSession.getFaultTrackEntityDao();
                        JSONArray array = new JSONArray(aesDecrypt);
                        for (int i = 0; i < array.length(); i++) {
                            entity = AppUtils.getGson().fromJson(array.getString(i), FaultTrackEntity.class);
                            list.add(entity);
                            entityDao.insertOrReplace(entity);
                        }
                        callBack.success(list);
                    } else if (status==201 || status== 203){
                        callBack.failed(status,obj);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(int error, Object obj) {
                super.failed(error, obj);
                callBack.failed(error,obj);
            }
        });
    }

    /**
     * De unique list.
     *  条件查询
     * @param status the key 修复状态
     * @return the list 返回实体集合
     */
    public static List<FaultTrackEntity> deUnique(int status) {
         DaoSession mDaoSession = MyApplication.getDaoSession();
         FaultTrackEntityDao mEntityDao = mDaoSession.getFaultTrackEntityDao();
        List<FaultTrackEntity> list = new ArrayList<>();
//        List<FaultTrackEntity> list = mEntityDao.queryBuilder().where(FaultTrackEntityDao.Properties.TroubleStatus.eq(status)).list();
        QueryBuilder<FaultTrackEntity> qb = mEntityDao.queryBuilder().where(FaultTrackEntityDao.Properties.TroubleStatus.eq(status));
        for (FaultTrackEntity entity : qb.list()) {
            list.add(entity);
        }
        Logger.t("查询Fault").d(list);
        return list;
    }
}

package com.pm.intelligent.module.home.dao;


import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.bean.HomeShelterEntity;
import com.pm.intelligent.greendao.DaoSession;
import com.pm.intelligent.greendao.HomeShelterEntityDao;
import com.pm.intelligent.utils.AESUtil;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.pm.okgolibrary.okInit.OkGoManager;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WeiSir on 2018/8/23.
 */
public class RequestShelterDao {

   private static DaoSession mDaoSession = MyApplication.getDaoSession();
    private static HomeShelterEntityDao mShelterEntityDao = mDaoSession.getHomeShelterEntityDao();

    /**
     * Do get shelter.
     *
     * @param url      the url
     * @param params   the params
     * @param callback the callback
     */
    public static void doGetShelter(String url, HashMap<String,String> params, final OkCallBack callback) {
        OkGoManager.INSTANCE.doPost(url, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                String jsonStr = (String) obj;
                Logger.json(obj.toString());
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        String data = jsonObject.getString("data");
                        String aesDecrypt = AESUtil.aesDecrypt(data);
                        Logger.d("解密后"+aesDecrypt);
                        JSONArray array = new JSONArray(aesDecrypt);
                        List<HomeShelterEntity> shelterEntityList = new ArrayList<>();
                        HomeShelterEntity entity = null;
                        DaoSession daoSession = MyApplication.getDaoSession();
                        HomeShelterEntityDao shelterEntityDao = daoSession.getHomeShelterEntityDao();
                        for (int i = 0; i < array.length(); i++) {
                            entity = gson.fromJson(array.getString(i), HomeShelterEntity.class);
                            shelterEntityDao.insertOrReplace(entity);
                            shelterEntityList.add(entity);
                        }
                        callback.success(shelterEntityList);
                    } else {
                        callback.failed(status,obj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.failed(201| 203,obj);
                }
            }
            @Override
            public void failed(int error, Object obj) {
                callback.failed(error,obj);
            }
        });
    }

    /**
     * 查出历史记录
     *
     * @return list
     */
    public static List<HomeShelterEntity> getHistory() {
        List<HomeShelterEntity> list = new ArrayList<>();
        QueryBuilder<HomeShelterEntity> qb = mShelterEntityDao.queryBuilder();
        for(HomeShelterEntity s:qb.list()){
            list.add(s);
        }
        return list;
    }
}

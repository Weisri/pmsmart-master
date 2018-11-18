package com.pm.intelligent.module.system.dao;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.bean.SystemDataBean;
import com.pm.intelligent.utils.AESUtil;
import com.pm.intelligent.utils.Log;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.pm.okgolibrary.okInit.OkGoManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WeiSir on 2018/8/22.
 */
public class SystemReportDao {

    public static void doSystemRequest(String url, HashMap<String, String> params, final OkCallBack callback) {
        OkGoManager.INSTANCE.doPost(url, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                String objStr = (String) obj;
                Logger.json(objStr);
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(objStr);
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        String data = jsonObject.getString("data");
                        String aesDecrypt = AESUtil.aesDecrypt(data);
                        Logger.d("解密后   " + aesDecrypt);
                        JSONArray jsonArray = new JSONArray(aesDecrypt);
                        List<SystemDataBean> list = new ArrayList<>();
                        SystemDataBean bean = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            bean = gson.fromJson(jsonArray.getString(i), SystemDataBean.class);
                            list.add(bean);
                        }
                        callback.success(list);
                    } else {
                        callback.failed(201, obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(int error, Object obj) {
                Log.i(error);
                callback.failed(error, obj);
            }
        });

    }


}

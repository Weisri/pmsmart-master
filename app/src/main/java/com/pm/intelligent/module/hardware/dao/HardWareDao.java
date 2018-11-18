package com.pm.intelligent.module.hardware.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.module.hardware.HardWareGroupBean;
import com.pm.intelligent.utils.AESUtil;
import com.pm.intelligent.utils.AppUtils;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.pm.okgolibrary.okInit.OkGoManager;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by WeiSir on 2018/8/27.
 * 硬件检测请求类
 */
public class HardWareDao {
    public static void doRequsetHard(String url, HashMap<String, String> params, final OkCallBack callBack) {
        OkGoManager.INSTANCE.doPost(url, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                Gson gson = new GsonBuilder()
                        .serializeNulls()
                        .registerTypeAdapter(String.class, AppUtils.STRING)
                        .create();
                String strObj = (String) obj;
                JsonObject object = new JsonParser().parse(strObj).getAsJsonObject();
                int status1 = object.get("status").getAsInt();
                if (status1 == 200) {
                    String data = object.get("data").getAsString();
                    String aesDecrypt = AESUtil.aesDecrypt(data);
                    Logger.d("解密后  " + aesDecrypt);
                    JsonReader jsonReader = null;
                    if (aesDecrypt != null) {
                        jsonReader = new JsonReader(new StringReader(aesDecrypt));
                        jsonReader.setLenient(true);
                        JsonArray array = new JsonParser().parse(jsonReader).getAsJsonArray();
                        Type listType = new TypeToken<ArrayList<HardWareGroupBean>>() {}.getType();
                        ArrayList<HardWareGroupBean> beanArrayList = gson.fromJson(array, listType);
                        Logger.d(beanArrayList);
                        callBack.success(beanArrayList);
                    }

                } else {
                    callBack.failed(status1, obj);
                }
            }

            @Override
            public void failed(int error, Object obj) {
                callBack.failed(error, obj);
            }
        });
    }



}

package com.pm.intelligent.module.control.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.bean.SeatHeatBean;
import com.pm.intelligent.utils.AppUtils;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.pm.okgolibrary.okInit.OkGoManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WeiSir on 2018/11/5.
 */
public class SeatHeatDao {


    public static void doRequestSeatHeat(String url, HashMap<String, String> params, final OkCallBack callBack) {
        OkGoManager.INSTANCE.doPost(url, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                String strObj = (String) obj;
                Logger.json(strObj);
                try {
                    JSONObject object = new JSONObject(strObj);
                    int status = object.getInt("status");
                    List<SeatHeatBean> heatBeanList = new ArrayList<>();
                    if (status == 200) {
                        String data = object.getString("data");
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            SeatHeatBean seatHeatBean = AppUtils.getGson().fromJson(array.getString(i), SeatHeatBean.class);
                            heatBeanList.add(seatHeatBean);
                        }
                    }
                    callBack.success(heatBeanList);
            } catch(
            JSONException e)

            {
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

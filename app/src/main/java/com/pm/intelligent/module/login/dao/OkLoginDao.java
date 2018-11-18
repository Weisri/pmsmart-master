package com.pm.intelligent.module.login.dao;

import com.pm.intelligent.utils.Log;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.pm.okgolibrary.okInit.OkGoManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by WeiSir on 2018/8/21.
 */
public class OkLoginDao {
    public static void doLoginRequest(String url, HashMap<String, String> params, final OkCallBack callback) {

        OkGoManager.INSTANCE.doPost(url, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                String jsonStr = (String) obj;
                Log.i(obj.toString());
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.getInt("status");
//                    JSONObject dataobj = jsonObject.getJSONObject("data");
//                    String token = String.valueOf(dataobj.get("token"));
                    String token = jsonObject.getString("data");

                    Log.i(obj.toString());
                    if (status == 200) {
                        callback.success(token);
                    } else {
                        Log.i("登录失败" + status);
                        callback.failed(status, token);
                    }
//                    callback.success(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.failed(203, obj);
                }
            }

            @Override
            public void failed(int error, Object obj) {
                super.failed(error, obj);
                callback.failed(error, obj);
            }
        });

    }
}

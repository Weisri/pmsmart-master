package com.pm.intelligent.module.login.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.pm.intelligent.bean.UserInfo;
import com.pm.intelligent.utils.net.BaseCallBack;
import com.pm.intelligent.utils.net.OkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by WeiSir on 2018/6/22.
 *
 */

public class LoginDao {

    /**
     * Do login request.
     *
     * @param url      the url
     * @param params   the params
     * @param callback the callback
     */
    public static void doLoginRequest(String url, HashMap<String,String> params, final BaseCallBack callback) {
        OkManager.getInstance().asynJsonStringByUrl(url, params, new BaseCallBack() {
            @Override
            public void success(Object data) {
                try {
//                    JSONArray array = new JSONArray(data.toString());
//                    com.pm.intelligent.utils.Log.i(data.toString());
//                    UserInfo info = null;
//                    Gson gson = new Gson();
//                    int len = array.length();
//                    for (int i = 0; i < len; i++) {
//                        info = gson.fromJson(array.getString(i),UserInfo.class);
//                    }
//                    callback.success(info);
                    JSONObject jsonObject = new JSONObject(data.toString());
                    int status = jsonObject.getInt("status");
                    String token = jsonObject.getString("token");
                    com.pm.intelligent.utils.Log.i(data.toString());
                    if (status == 200) {
                        callback.success(token);
                    } else {
                        callback.failed(status,data);
                        com.pm.intelligent.utils.Log.i("登录失败"+status);
                    }
//                    callback.success(token);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(int errorCode, Object data) {
                callback.failed(errorCode,data.toString()+"欸呵");
            }
        });


    }
}

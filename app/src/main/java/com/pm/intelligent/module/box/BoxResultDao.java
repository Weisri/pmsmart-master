package com.pm.intelligent.module.box;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.bean.BoxDataBean;
import com.pm.intelligent.utils.AESUtil;
import com.pm.intelligent.utils.AppUtils;
import com.pm.intelligent.utils.DateUtils;
import com.pm.okgolibrary.okInit.OkCallBack;
import com.pm.okgolibrary.okInit.OkGoManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WeiSir on 2018/8/17.
 */
public class BoxResultDao {
    public static void doBoxResultDao(String url, HashMap<String,String> params, final OkCallBack okCallBack) {
        OkGoManager.INSTANCE.doPost(url, params, new OkCallBack() {
            @Override
            public void success(Object obj) {
                String jsonStr = obj.toString();
                Logger.json(jsonStr);

                Gson gson = new GsonBuilder()
                        .serializeNulls()
                        .registerTypeAdapter(String.class, AppUtils.STRING)
                        .create();
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
//                        JSONObject data = jsonObject.getJSONObject("data");
                        String data = jsonObject.getString("data");
                        if (data!=null) {
                        String aesDecrypt = AESUtil.aesDecrypt(data);
                        Logger.d("解密后" + aesDecrypt);
                            BoxResultBean resultBean = gson.fromJson(aesDecrypt, BoxResultBean.class);
                            Logger.d(resultBean);
                            JSONObject object = new JSONObject(aesDecrypt);
                            List<BoxDataBean> list = new ArrayList<>();
                            list.add(new BoxDataBean("报告编号：", object.getString("reportNumber"), null));
                            list.add(new BoxDataBean("盒子名称：", object.getString("boxName"), ""));
                            list.add(new BoxDataBean("当前状态：", String.valueOf(object.getString("boxState")), ""));
                            list.add(new BoxDataBean("内存使用：", String.valueOf(object.getInt("memoryPer")), "1"));
                            list.add(new BoxDataBean("网络信号：", String.valueOf(object.getString("netSignal")), ""));
                            if (resultBean.getStartTime()!=null) {
                                list.add(new BoxDataBean("最后登录时间：", DateUtils.formatDatetime(resultBean.getStartTime()), ""));
                            }
                            if (resultBean.getEndTime()!=null) {
                                list.add(new BoxDataBean("最后离线时间：", DateUtils.formatDatetime(resultBean.getEndTime()), ""));
                            }
//                        list.add(new BoxDataBean("病毒：", object.getString("virus"), ""));
                            okCallBack.success(list);
                        } else {
                            okCallBack.failed(300,obj);
                        }

                    } else {
                        okCallBack.failed(201,obj);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    okCallBack.failed(201,obj);
                }
            }

            @Override
            public void failed(int error, Object obj) {
                okCallBack.failed(error, obj);
            }
        });

    }


}

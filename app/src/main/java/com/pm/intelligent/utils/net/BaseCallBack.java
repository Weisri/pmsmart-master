package com.pm.intelligent.utils.net;

/**
 * Created by Struggle on 2017/5/11.
 */

public abstract class BaseCallBack {
    /**
     * 请求成功
     *
     * @param data object 对象
     */
    public abstract void success(Object  data);

    /**
     * 请求失败
     *
     * @param errorCode 错误码
     * @param data      消息实体
     */
    public abstract void failed(int errorCode, Object data);
}

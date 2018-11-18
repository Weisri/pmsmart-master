package com.pm.okgolibrary.okInit;

/**
 * Created by pumin on 2018/8/15.
 */

public abstract class OkCallBack {
    /**
     * 成功回调
     *
     * @param obj
     */

    public void success(Object obj) {

    }

    /**
     * 失败回调
     *
     * @param error
     * @param obj
     */

    public void failed(int error, Object obj) {

    }
}

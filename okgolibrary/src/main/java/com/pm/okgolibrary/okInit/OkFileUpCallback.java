package com.pm.okgolibrary.okInit;

import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;


/**
 * Created by pumin on 2018/8/15.
 */

public abstract class OkFileUpCallback {
    /**
     * 开始上传回调
     */
    public void start() {

    }

    /**
     * 成功回调
     */
    public void onSuccess(Response<String> response) {

    }

    /**
     * 失败回调
     */
    public void onError(Response<String> response) {

    }

    /**
     * 进度回调，可以再次更新ui层进度条
     */
    public void upProgress(Progress progress) {

    }
}

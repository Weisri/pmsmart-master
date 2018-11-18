package com.pm.okgolibrary.okInit;

import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;

/**
 * Created by pumin on 2018/8/15.
 */

public abstract class OkFileDownCallBack {
    /**
     * 开下载回调
     */
    public void start() {

    }

    /**
     * 成功回调
     */
    public void onSuccess(Response<File> response) {

    }

    /**
     * 失败回调
     */
    public void onError(Response<File> response) {

    }

    /**
     * 进度回调，可以更新ui层进度条
     */
    public void downloadProgress(Progress progress) {

    }

}

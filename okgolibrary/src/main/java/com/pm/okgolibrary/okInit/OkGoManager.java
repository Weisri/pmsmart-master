package com.pm.okgolibrary.okInit;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pumin on 2018/8/15.
 */

public enum OkGoManager {
    INSTANCE;
    private final static String TAG = "okGoManager";

    /**
     * OkGo get请求方式
     *
     * @param url        url地址
     * @param params     参数
     * @param okCallBack 回调
     */
    public void doGet(String url, HashMap<String, String> params, final OkCallBack okCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put(params);
        OkGo.<String>get(url)
                .tag(this)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程
                        // String data = response.body();//这个就是返回来的结果
                        okCallBack.success(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        okCallBack.failed(0, response);
                    }
                });

    }


    /**
     * OkGo post请求方式
     *
     * @param url        url地址
     * @param params     参数
     * @param okCallBack 回调
     */
    public void doPost(String url, HashMap<String, String> params, final OkCallBack okCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put(params);

        OkGo.<String>post(url)
                .tag(this)
                .isSpliceUrl(false)//是否强制拼接params的参数到url后面 默认false
                .params(httpParams)
                .isMultipart(true)

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程
                        // String data = response.body();//这个就是返回来的结果
                        okCallBack.success(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        okCallBack.failed(0, response);
                    }
                });
    }

    /**
     * 文件上传
     *
     * @param url              url地址
     * @param params           参数
     * @param files            文件路径集合
     * @param okFileUpCallback 回调
     */
    public void doUpload(String url, HashMap<String, String> params, List<File> files, final OkFileUpCallback okFileUpCallback) {
        //拼接参数
        HttpParams httpParams = new HttpParams();
        httpParams.put(params);
        OkGo.<String>post(url)//
                .tag(this)//
                .params(httpParams)//请求参数
//                .params("file1",new File("文件路径"))   //这种方式为一个key，对应一个文件
                .addFileParams("filename", files)           // 这种方式为同一个key，上传多个文件
                .execute(new JsonCallback<String>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<String, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        okFileUpCallback.start();//开始上传
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        okFileUpCallback.onSuccess(response);//上传成功
                    }

                    @Override
                    public void onError(Response<String> response) {
                        okFileUpCallback.onError(response);//上传失败
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        okFileUpCallback.upProgress(progress);//上传进度
                    }
                });
    }

    /**
     * 文件下载
     *
     * @param url            url地址
     * @param params         参数
     * @param fileName       上传文件名称
     * @param okFileCallBack 上传回调
     */
    public void doFileDownload(String url, HashMap<String, String> params, String fileName, final OkFileDownCallBack okFileCallBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put(params);
        OkGo.<File>get(url)
                .tag(this)
                .headers("header1", "headerValue1")//头部
                .params(httpParams)//添加参数
                .execute(new FileCallback(fileName) {

                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<File, ? extends com.lzy.okgo.request.base.Request> request) {
                        okFileCallBack.start();//开始下载
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        okFileCallBack.onSuccess(response);//下载成功
                    }

                    @Override
                    public void onError(Response<File> response) {
                        okFileCallBack.onError(response);//下载失败
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        okFileCallBack.downloadProgress(progress);//下载进度
                    }
                });
    }

}

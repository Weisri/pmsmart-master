package com.pm.intelligent.utils.net;

import android.support.annotation.NonNull;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.vector.update_app.HttpManager;

import java.io.File;
import java.util.Map;

/**
 * Created by WeiSir on 2018/8/16.
 * app更新实现HttpManager接口
 */
public class AppUpdateHttpUtils implements HttpManager {
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
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
                        callBack.onResponse(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }
                });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        HttpParams httpParams = new HttpParams();
        httpParams.put(params);

        OkGo.<String>post(url)
                .tag(this)
                .params(httpParams)
                .isMultipart(true)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程
                        // String data = response.body();//这个就是返回来的结果
                        callBack.onResponse(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }
                });

    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        OkGo.<File>get(url).execute(new com.lzy.okgo.callback.FileCallback(path, fileName) {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                callback.onResponse(response.body());
            }

            @Override
            public void onStart(com.lzy.okgo.request.base.Request<File, ? extends com.lzy.okgo.request.base.Request> request) {
                super.onStart(request);
                callback.onBefore();
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<File> response) {
                super.onError(response);
                callback.onError("异常");
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);

                callback.onProgress(progress.fraction, progress.totalSize);
            }
        });
    }

}

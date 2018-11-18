package com.pm.intelligent.utils.net;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkManager {
    private OkHttpClient client;
    private volatile static OkManager manager;
    private final String TAG = OkManager.class.getSimpleName();//获得类名
    private Handler handler;
    //提交字符串
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交json数据
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    private String mSavePath = "";
    private String mApkPath = "";
    private String mTempPath = "";

    private OkManager() {
        client = new OkHttpClient();
        client.connectTimeoutMillis();
        // client.readTimeoutMillis();
        handler = new Handler(Looper.getMainLooper());
    }

    public static OkManager getInstance() {
        OkManager instance = null;
        if (manager == null) {
            synchronized (OkManager.class) {
                if (manager == null) {
                    instance = new OkManager();
                    manager = instance;
                }
            }
        }

        return manager;
    }

    /**
     * 请求返回的结果是json字符串
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonMethod(final String jsonValue, final BaseCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.success(jsonValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 请求指定url返回的是json字符串
     *
     * @param url
     * @param params
     * @param callBack
     */
    public void asynJsonStringByUrl(String url, HashMap<String, String> params, final BaseCallBack callBack) {

        StringBuffer sb = new StringBuffer();
        sb.append(url);
        if (params != null) {
            Set<Map.Entry<String, String>> sets = params.entrySet();
            for (Map.Entry<String, String> entry : sets) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        final Request request = new Request.Builder().url(sb.toString()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.failed(0, e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //Log.i(TAG, "onResponse: "+response.body().string());
                    onSuccessJsonMethod(response.body().string(), callBack);
                }
            }
        });
    }

    public void downloadFile(String url,String packageName, final BaseCallBack baseCallBack) {
        String apkName = packageName.replace("com.pm.","");
        String fileName = new StringBuffer(apkName).append(".apk").toString();
        String tempName = new StringBuffer(apkName).append(".temp").toString();
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            mSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/update/";
            File file = new File(mSavePath);
            if (!file.exists()) {
                file.mkdir();
            }
            mApkPath = mSavePath + fileName;
            mTempPath = mSavePath + tempName;
        }

        final File apkFile = new File(mApkPath);
        final File tempFile = new File(mTempPath);

        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                baseCallBack.failed(0, "下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                is = response.body().byteStream();
                try {
                    fos = new FileOutputStream(tempFile);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    tempFile.renameTo(apkFile);
                    baseCallBack.success(apkFile.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    baseCallBack.failed(0, "下载失败");
                } catch (IOException e) {
                    e.printStackTrace();
                    baseCallBack.failed(0, "下载失败");
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}

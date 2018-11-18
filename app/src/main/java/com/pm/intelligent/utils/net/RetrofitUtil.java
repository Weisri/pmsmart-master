package com.pm.intelligent.utils.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/8/13.
 */

public class RetrofitUtil {
    private static API api;

    static {
        api = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient())
                .build()
                .create(API.class);

    }

    private RetrofitUtil(){}

    public static API getInstence(){

        return api;
    }
}

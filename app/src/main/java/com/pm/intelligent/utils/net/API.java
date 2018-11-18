package com.pm.intelligent.utils.net;

import com.pm.intelligent.Contants;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/8/30.
 */

public interface API {

    String BASE_URL = Contants.SERVER_URL;

    /**
     * 智能开关
     * @param token
     * @param iccid
     * @return
     */
    @POST("controlApp/getSwitchs")
    Observable<ResponseBody> getSwitchs(@Query("token") String token, @Query("iccid")String iccid);

    /**
     * 智能控制
     * @param token
     * @param iccid
     * @return
     */
    @POST("controlApp/getAdjusts")
    Observable<ResponseBody> getAdjusts(@Query("token") String token,@Query("iccid")String iccid);

    /**
     * 加热座椅实时温度
     * @return
     */
    @POST("controlApp/heatedseat")
    Observable<ResponseBody> heatedSeat();

    /**
     * 情景智能
     * @param token
     * @param iccid
     * @return
     */
    @POST("controlApp/getScenes")
    Observable<ResponseBody> getScenes(@Query("token") String token,@Query("iccid")String iccid);

    /**
     * 月用电量
     * @param token
     * @param iccid
     * @return
     */
    @POST("controlApp/getElectricMonth")
    Observable<ResponseBody> getElectricMonth(@Query("token") String token, @Query("iccid") String iccid);

    /**
     * 一周用电量
     * @param token
     * @param iccid
     * @return
     */
    @POST("controlApp/getElectricWeek")
    Observable<ResponseBody> getElectricWeek(@Query("token") String token, @Query("iccid") String iccid);









}

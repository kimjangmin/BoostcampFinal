package com.jm.gon.triphelper.Network_MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 김장민 on 2017-02-17.
 */

public interface NetworkMethod {
    @GET("/main")
    Call<List<NetworkDataTable>> getAllData();
    @GET("/openapi/service/rest/KorService/areaBasedList")
    Call<List<ApiDataTable>> getData(@Query("ServiceKey") String key, @Query("contentTypeId") String null1,
                                @Query("areaCode") String null2 , @Query("sigunguCode") String null3,
                                @Query("cat1") String null4 ,@Query("cat2") String null5,
                                @Query("cat3") String null6, @Query("listYN") String Y,
                                @Query("MobileOS") String AND, @Query("MobileApp") String TripHelper,
                                @Query("arrange") String A, @Query("numOfRows") String num20 , @Query("_type") String jsonn);
}

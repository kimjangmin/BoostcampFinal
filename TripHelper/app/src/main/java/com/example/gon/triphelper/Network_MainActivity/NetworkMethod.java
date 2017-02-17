package com.example.gon.triphelper.Network_MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by 김장민 on 2017-02-17.
 */

public interface NetworkMethod {
    @GET("/main")
    Call<List<NetworkDataTable>> getAllData();
}

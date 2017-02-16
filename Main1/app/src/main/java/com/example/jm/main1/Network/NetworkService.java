package com.example.jm.main1.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jm on 2017-02-13.
 */

public interface NetworkService {
    @GET("/main")
    Call<List<Main_DataTable>> getAllData();
}

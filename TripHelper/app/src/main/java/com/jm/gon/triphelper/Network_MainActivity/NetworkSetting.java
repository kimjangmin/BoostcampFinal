package com.jm.gon.triphelper.Network_MainActivity;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 김장민 on 2017-02-17.
 */

public class NetworkSetting{

    private String Url;
    public NetworkSetting(){
        NetworkSetting.instance = this;
    }

    private static NetworkSetting instance;
    public static NetworkSetting getInstance(){
        return instance;
    }

    private NetworkMethod networkMethod;
    public NetworkMethod getNetworkMethod(){
        return networkMethod;
    }
    public void buildNetworkService(String address, int port){
        synchronized (NetworkSetting.class){
            if(networkMethod == null){
                Url = String.format("http://%s:%d",address,port);
                setting();
            }
        }
    }
    public void buildAPIService(String address){
        synchronized (NetworkSetting.class){
            if(networkMethod == null){
                Url = String.format("http://%s",address);
                setting();
            }
        }
    }
    public void setting(){
        Log.i("TAG","NetworkSetting Url = "+ Url);
        Gson gson = new GsonBuilder().create();

        GsonConverterFactory factory = GsonConverterFactory.create(gson);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url)
                .addConverterFactory(factory)
                .build();

        networkMethod = retrofit.create(NetworkMethod.class);
        Log.i("TAG","retrofit access = "+retrofit.toString());
    }

}

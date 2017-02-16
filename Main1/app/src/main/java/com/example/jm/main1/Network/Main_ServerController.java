package com.example.jm.main1.Network;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jm on 2017-02-13.
 */

public class Main_ServerController extends Application{
    private static Main_ServerController instance;
    public static Main_ServerController getInstance(){
        return instance;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        Main_ServerController.instance = this;
    }
    private NetworkService networkService;
    public NetworkService getNetworkService(){
        return networkService;
    }
    private String Url;
    public void buildNetworkService(String ip, int port){
        synchronized (Main_ServerController.class){
            if(networkService == null){
                Url = String.format("http://%s:%d",ip,port);
                Log.i("TAG","URL = "+Url);
                Gson gson = new GsonBuilder().create();

                //json으로 데이터를 보기쉽게 변환하기 위해.
                GsonConverterFactory factory = GsonConverterFactory.create(gson);

                //retrofit 연결.
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Url)
                        .addConverterFactory(factory)
                        .build();
                networkService = retrofit.create(NetworkService.class);
                Log.i("TAG","retrofit access");
            }
        }
    }
}

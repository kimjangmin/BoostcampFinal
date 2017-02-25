package com.jm.gon.triphelper.functionplan3;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jm.gon.triphelper.CustomClickListener;
import com.jm.gon.triphelper.DetailActivity;
import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.functionplan2.TimeLineModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class FunctionPlan3Fragment2 extends Fragment implements CustomClickListener {
    RecyclerView rv_fragment2;
    Fragment2Adapter fragment2Adapter;
    private TimeLineModel bd_timeLineModel;
    private List<TimeLineModel> timeLineModelList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.function_plan3_frgment2, container, false);

        rv_fragment2 = (RecyclerView)view.findViewById(R.id.rv_fragment2);

        bd_timeLineModel = getArguments().getParcelable("model");
        Log.i("GAG","22222222222222222222222222");
        Log.i("TAG","why "+bd_timeLineModel.getTitle());
        Log.i("TAG","why2 "+bd_timeLineModel.getMapx());
        Log.i("TAG","why!!!!!!!!!!!!!!!!!!!!!!!!!!");

        timeLineModelList = new ArrayList<>();
        new FunctionPlan3Fragment2.Frament2Async().execute();

        fragment2Adapter = new Fragment2Adapter((ArrayList<TimeLineModel>) timeLineModelList, getContext(), this);
        //여기서 동기화 문제가 일어나지 않을까...?


        rv_fragment2.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv_fragment2.setLayoutManager(layoutManager);
        rv_fragment2.setAdapter(fragment2Adapter);

        return view;

    }

    @Override
    public void onClick(TimeLineModel timeLineModel) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("title",timeLineModel.getTitle());
        startActivity(intent);
    }

    @Override
    public void onClick(String url) {
    }

    private class Frament2Async extends AsyncTask<Void, Void, String> {

        String result;
        @Override
        protected String doInBackground(Void... params) {
            try{
                URL url = new URL(maekUrl());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                String tmp;
                while ((tmp = rd.readLine()) != null) {
                    result = tmp;
                    Log.i("TAG","result = "+result);
                }
                rd.close();
                conn.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parsingJson(s);
            Log.i("TAG","Fragment before datasetChange");
            fragment2Adapter.notifyDataSetChanged();
            Log.i("TAG","Fragment after datasetChange");
            fragment2Adapter.update((ArrayList<TimeLineModel>) timeLineModelList);
            Log.i("TAG","Fragment after update");
        }

        private String maekUrl(){
            StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList");
            try {
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
                urlBuilder.append("&" + URLEncoder.encode("contentTypeId","UTF-8") + "=" + URLEncoder.encode("39","UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("AND", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("TripHelper", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("mapX","UTF-8") + "=" + URLEncoder.encode(bd_timeLineModel.getMapx(),"UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("mapY","UTF-8") + "=" + URLEncoder.encode(bd_timeLineModel.getMapy(),"UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("radius","UTF-8") + "=" + URLEncoder.encode("5000","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("numOfRows","UTF-8")+"=" +URLEncoder.encode("100","UTF-8"));
                Log.i("GAG","Fragment2 url = "+urlBuilder.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return urlBuilder.toString();
        }
        private void parsingJson(String str){
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
                JSONObject object = new JSONObject(jsonObject1.getJSONObject("body").toString());
                JSONObject oobject = new JSONObject(object.getJSONObject("items").toString());
                JSONArray jsonArray = oobject.getJSONArray("item");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tmp = jsonArray.getJSONObject(i);
                    TimeLineModel timeLineModel = new TimeLineModel();
                    timeLineModel.setTitle(tmp.getString("title"));
                    if (tmp.has("firstimage")) {
                        timeLineModel.setUrl(tmp.getString("firstimage"));
                    }
                    if (tmp.has("tel")) {
                        timeLineModel.setTel(tmp.getString("tel"));
                    }
                    if (tmp.has("addr1")) {
                        timeLineModel.setAddr(tmp.getString("addr1"));
                    }
                    timeLineModelList.add(timeLineModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

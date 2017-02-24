package com.jm.gon.triphelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jm.gon.triphelper.functionplan2.TimeLineModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by 김장민 on 2017-02-23.
 */

public class HttpConnectControl extends Thread{
    public final String LOCATIONBASE = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList";
    public final String AREABASE = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList";
    public final String KEYWORD = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword";
    public final String FESTIVAL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchFestival";

    private String asyncResult;
    private ArrayList<TimeLineModel> modelList;
    private Context context;

    MainActivityAdapter adapter;

    private String str;
    private String[] arrStr;

    public HttpConnectControl(Context ctx, MainActivityAdapter adapter1){
        asyncResult = null;
        modelList = new ArrayList<>();
        context = ctx;
        adapter = adapter1;
    }
    public void run(){
        getResult(str, arrStr);
        Log.i("TAG","Trhead size = "+modelList.size());
    }

    public ArrayList<TimeLineModel> getResult(String keyword, String[] params){
        Log.i("TAG","startgetresult");
        try {
            asyncResult = new Async().execute(makeUrl(keyword, params)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i("TAG","end get reuslt ");
        Log.i("TAG","kkkkkkkkkkkkkkkkkkk");
        Log.i("TAG","before result modellist size = "+modelList.size());
        Log.i("TAG","kkkkkkkkkkkkkkkkkk");
        return (ArrayList<TimeLineModel>) modelList;
    }

    public String makeUrl(String url, String[] params){
        StringBuilder urlBuilder = new StringBuilder();
        try {
            switch (url){
                case LOCATIONBASE :
                    urlBuilder.append(LOCATIONBASE);
                    urlBuilder.append("?"+URLEncoder.encode("mapX","UTF-8") + "=" + URLEncoder.encode(params[0],"UTF-8"));
                    urlBuilder.append("&"+URLEncoder.encode("mapY","UTF-8") + "=" + URLEncoder.encode(params[1],"UTF-8"));
                    urlBuilder.append("&"+URLEncoder.encode("radius","UTF-8") + "=" + URLEncoder.encode("2000","UTF-8"));
                    break;
                case AREABASE :
                    urlBuilder.append(AREABASE);
                    urlBuilder.append("?"+URLEncoder.encode("areaCode","UTF-8") + "=" + URLEncoder.encode(params[0],"UTF-8"));
                    break;
                case KEYWORD :
                    urlBuilder.append(KEYWORD);
                    urlBuilder.append("?" + URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(params[0],"UTF-8"));
                    break;
                case FESTIVAL:
                    urlBuilder.append(FESTIVAL);
                    urlBuilder.append("?" + URLEncoder.encode("eventStartDate", "UTF-8") + "=" + URLEncoder.encode(params[0],"UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("eventEndDate", "UTF-8") + "=" + URLEncoder.encode(params[1],"UTF-8"));
                    break;
            }
            urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("AND", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("TripHelper", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("TAG","url = "+urlBuilder.toString());
        return urlBuilder.toString();
    }
    public TimeLineModel doJson(JSONObject jsonStr){
        TimeLineModel timeLineModel = new TimeLineModel();

        try {
            timeLineModel.setTitle(jsonStr.getString("title"));
            if(jsonStr.has("addr1")){
                timeLineModel.setAddr(jsonStr.getString("addr1"));
            } else{
                timeLineModel.setAddr("");
            }
            if (jsonStr.has("tel")) {
                timeLineModel.setTel(jsonStr.getString("tel"));
            } else {
                timeLineModel.setTel("");
            }
            timeLineModel.setMapx(jsonStr.getString("mapx"));
            timeLineModel.setMapy(jsonStr.getString("mapy"));
            if (jsonStr.has("firstimage")) {
                timeLineModel.setUrl(jsonStr.getString("firstimage"));
            } else {
                timeLineModel.setUrl("");
            }
            if(jsonStr.has("eventstartdate")){
                timeLineModel.setStartdate(jsonStr.getString("eventstartdate"));
            }else{
                timeLineModel.setStartdate("");
            }
            if(jsonStr.has("eventenddate")){
                timeLineModel.setEnddate(jsonStr.getString("eventenddate"));
            }else{
                timeLineModel.setEnddate("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("TAG","title = "+timeLineModel.getTitle());
        Log.i("TAG","addr = "+ timeLineModel.getTel());
        Log.i("TAG","tel = "+ timeLineModel.getTel());
        Log.i("TAG","mapX = "+timeLineModel.getMapx());
        Log.i("TAG","mapY = "+timeLineModel.getMapy());
        Log.i("TAG","firstimage = "+timeLineModel.getUrl());
        Log.i("TAG","eventstartdate = "+timeLineModel.getStartdate());
        Log.i("TAG","eventenddate = "+timeLineModel.getEnddate());
        return timeLineModel;
    }
    public void parsingJson(String parsingStr){
        try {
            JSONObject jsonObject = new JSONObject(parsingStr);
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
            JSONObject jsonObject2 = new JSONObject(jsonObject1.getJSONObject("body").toString());
            JSONObject jsonObject3 = new JSONObject(jsonObject2.getJSONObject("items").toString());
            Object object = jsonObject3.get("item");
            Log.i("TAG","parsingJson start");
            if(object instanceof JSONArray){
                Log.i("TAG"," this is jsonArray");
                for (int k = 0; k < ((JSONArray)object).length(); k++) {
                    Log.i("TAG"," this is parsing k = "+k);
                    modelList.add( doJson( ((JSONArray) object).getJSONObject(k) ) );
                    Log.i("TAG","complete parsing modellist size = "+modelList.size());
                }
            }else{
                Log.i("TAG","this is jsonobject");
                modelList.add( doJson( (JSONObject)object ) );
                Log.i("TAG","complete parsing modellist size = "+modelList.size());
            }
            if(adapter != null) {
                adapter.update(modelList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class Async extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                String line;
                while ((line = rd.readLine()) != null) {
                    asyncResult = line;
                    Log.i("TAG","result = "+line);
                }
                rd.close();
                conn.disconnect();

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return asyncResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("TAG","HttpConnect onpostexecute");
            parsingJson(result);
        }
    }

}

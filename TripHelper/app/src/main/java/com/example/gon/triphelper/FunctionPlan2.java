package com.example.gon.triphelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

public class FunctionPlan2 extends AppCompatActivity {
    private Stack<Integer> dfs_node;
    private Stack<Integer> dfs_shortestNode;
    private int[][] dfs_map;
    private double dfs_min;
    private boolean[] dfs_visit;

    private String[][] locationXY;
    private int spotCount;

    private RecyclerView rv_FunctionPlan2_timeline;
    private List<TimeLineModel> modelList;
    private ArrayList<String> spotList;

    private int date;
    private String city;
    private String theme;

    private final String LOCATIONBASE = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList";
    private final String AREABASE = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList";
    private final String KEYWORD = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_plan2);
        Intent intent = getIntent();
        date = intent.getIntExtra("date",-1);
        theme = intent.getStringExtra("theme");
        city = intent.getStringExtra("city");
        spotList = (ArrayList<String>) intent.getSerializableExtra("spot");
        spotCount = spotList.size();
        dfs_min =999999999;
        locationXY = new String[spotCount][spotCount];
        dfs_map = new int[spotCount][spotCount];
        dfs_visit = new boolean[spotCount];
        Arrays.fill(dfs_visit, Boolean.FALSE);
        modelList = new ArrayList<>();
        dfs_node = new Stack<>();
        dfs_shortestNode = new Stack<>();
        try {
            boolean t = new getLocation().execute(spotList).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        rv_FunctionPlan2_timeline = (RecyclerView)findViewById(R.id.rv_FunctionPlan2_timeline);
        rv_FunctionPlan2_timeline.setLayoutManager(new LinearLayoutManager(this));
        rv_FunctionPlan2_timeline.setHasFixedSize(true);

        if(spotCount <=date){
            for(int i=0;i<date; i++){
                if( i<= spotList.size()){
                    try {
                        String[] arr = new String[]{locationXY[dfs_shortestNode.get(i)][0], locationXY[dfs_shortestNode.get(i)][1]};
                        String result = new NetworkApi().execute(makeUrl(LOCATIONBASE, arr)).get();
                        parsingJson(result, i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    try{
                        String[] arr = new String[]{city};
                        String result = new NetworkApi().execute(makeUrl(AREABASE, arr)).get();
                        parsingJson(result, i);
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        TimeLineAdapter timeLineAdapter = new TimeLineAdapter(modelList);
        rv_FunctionPlan2_timeline.setAdapter(timeLineAdapter);

    }
    public String makeUrl(String url, String[] params){
        StringBuilder urlBuilder = new StringBuilder(url);
        try {
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("AND", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("TripHelper", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            switch (url){
                case LOCATIONBASE :
                    urlBuilder.append("&" + URLEncoder.encode("contentType", "UTF-8") + "=" );
                    urlBuilder.append("&"+URLEncoder.encode("mapX","UTF-8") + "=" + URLEncoder.encode(params[0],"UTF-8"));
                    urlBuilder.append("&"+URLEncoder.encode("mapY","UTF-8") + "=" + URLEncoder.encode(params[1],"UTF-8"));
                    urlBuilder.append("&"+URLEncoder.encode("radius","UTF-8") + "=" + URLEncoder.encode("2000","UTF-8"));
                    break;
                case AREABASE :
                    urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" );
                    urlBuilder.append("&"+URLEncoder.encode("areaCode","UTF-8") + "=" + URLEncoder.encode(params[0],"UTF-8"));
                    urlBuilder.append("&"+URLEncoder.encode("sigunguCode","UTF-8") + "=");
                    urlBuilder.append("&"+URLEncoder.encode("cat1","UTF-8") + "=");
                    urlBuilder.append("&"+URLEncoder.encode("cat2","UTF-8") + "=");
                    urlBuilder.append("&"+URLEncoder.encode("cat3","UTF-8") + "=");
                    break;
                case KEYWORD :
                    urlBuilder.append("&" + URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(params[0],"UTF-8"));
                    urlBuilder.append("&"+URLEncoder.encode("areaCode","UTF-8") + "=");
                    urlBuilder.append("&"+URLEncoder.encode("sigunguCode","UTF-8") + "=");
                    urlBuilder.append("&"+URLEncoder.encode("cat1","UTF-8") + "=");
                    urlBuilder.append("&"+URLEncoder.encode("cat2","UTF-8") + "=");
                    urlBuilder.append("&"+URLEncoder.encode("cat3","UTF-8") + "=");
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlBuilder.toString();
    }
    public void parsingJson(String parsingStr, int type){
        try {
            JSONObject jsonObject = new JSONObject(parsingStr);
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
            JSONObject jsonObject2 = new JSONObject(jsonObject1.getJSONObject("body").toString());
            JSONObject jsonObject3 = new JSONObject(jsonObject2.getJSONObject("items").toString());
            JSONArray jsonArray = jsonObject3.getJSONArray("item");
            if(type <= spotCount) {
                if (jsonArray.length() > 2) {
                    for (int k = 0; k < 2; k++) {
                        JSONObject tmp = jsonArray.getJSONObject(k);
                        TimeLineModel timeLineModel = new TimeLineModel();
                        timeLineModel.setTitle(tmp.getString("title"));
                        timeLineModel.setAddr(tmp.getString("addr1"));
                        if (tmp.has("tel")) {
                            timeLineModel.setTel(tmp.getString("tel"));
                        } else {
                            timeLineModel.setTel("");
                        }
                        timeLineModel.setMapx(tmp.getString("mapx"));
                        timeLineModel.setMapy(tmp.getString("mapy"));
                        modelList.add(timeLineModel);
                    }
                }
            }else{
                if(jsonArray.length()!=0) {
                    for (int k = type; k < date ; k++){
                        JSONObject tmp = jsonArray.getJSONObject(k);
                        TimeLineModel timeLineModel = new TimeLineModel();
                        timeLineModel.setTitle(tmp.getString("title"));
                        timeLineModel.setAddr(tmp.getString("addr1"));
                        if(tmp.has("tel")) {
                            timeLineModel.setTel(tmp.getString("tel"));
                        }else{
                            timeLineModel.setTel("");
                        }
                        timeLineModel.setMapx(tmp.getString("mapx"));
                        timeLineModel.setMapy(tmp.getString("mapy"));
                        modelList.add(timeLineModel);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class NetworkApi extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            String result=null;
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
                    result = line;
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
            return result;
        }
    }

    public class getTimeLine extends  AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            //여기서 총 날짜보다 갯수가 작을때인데 만약 찾는 갯수가 입력한 관광지보다 작을경우고 그게아닐경우 else
            if(spotCount <=date){
                for(int i=0;i<date;i++){
                    if(i<= spotCount) {
                        try {
                            URL url = new URL(mapUrl(locationXY[dfs_shortestNode.get(i)][0], locationXY[dfs_shortestNode.get(i)][1]));
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setRequestProperty("Content-type", "application/json");
                            BufferedReader rd;
                            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            } else {
                                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                            }
                            String strr= null;
                            String line;
                            while ((line = rd.readLine()) != null) {
                                strr = line;
                                Log.i("TAG","result = "+line);
                            }
                            rd.close();
                            conn.disconnect();

                            JSONObject jsonObject = new JSONObject(strr);
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
                            JSONObject jsonObject2 = new JSONObject(jsonObject1.getJSONObject("body").toString());
                            JSONObject jsonObject3 = new JSONObject(jsonObject2.getJSONObject("items").toString());
                            JSONArray jsonArray = jsonObject3.getJSONArray("item");
                            if(jsonArray.length()>2) {
                                for (int k = 0; k < 2 ; k++){
                                    JSONObject tmp = jsonArray.getJSONObject(k);
                                    TimeLineModel timeLineModel = new TimeLineModel();
                                    timeLineModel.setTitle(tmp.getString("title"));
                                    timeLineModel.setAddr(tmp.getString("addr1"));
                                    if(tmp.has("tel")) {
                                        timeLineModel.setTel(tmp.getString("tel"));
                                    }else{
                                        timeLineModel.setTel("");
                                    }
                                    timeLineModel.setMapx(tmp.getString("mapx"));
                                    timeLineModel.setMapy(tmp.getString("mapy"));
                                    modelList.add(timeLineModel);
                                }
                            }

                        }catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (NullPointerException e){
                            Log.i("TAG","NULL point = "+e.getMessage());
                        }
                        catch (JSONException e) {
                            Log.i("TAG","error = "+e.getMessage());
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            URL url = new URL(spotUrl(city));
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setRequestProperty("Content-type", "application/json");
                            BufferedReader rd;
                            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            } else {
                                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                            }
                            String strr= null;
                            String line;
                            while ((line = rd.readLine()) != null) {
                                strr = line;
                                Log.i("TAG","result = "+line);
                            }
                            rd.close();
                            conn.disconnect();

                            JSONObject jsonObject = new JSONObject(strr);
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
                            JSONObject jsonObject2 = new JSONObject(jsonObject1.getJSONObject("body").toString());
                            JSONObject jsonObject3 = new JSONObject(jsonObject2.getJSONObject("items").toString());
                            JSONArray jsonArray = jsonObject3.getJSONArray("item");
                            if(jsonArray.length()!=0) {
                                for (int k = i; k < date ; i = ++k){
                                    JSONObject tmp = jsonArray.getJSONObject(k);
                                    TimeLineModel timeLineModel = new TimeLineModel();
                                    timeLineModel.setTitle(tmp.getString("title"));
                                    timeLineModel.setAddr(tmp.getString("addr1"));
                                    if(tmp.has("tel")) {
                                        timeLineModel.setTel(tmp.getString("tel"));
                                    }else{
                                        timeLineModel.setTel("");
                                    }
                                    timeLineModel.setMapx(tmp.getString("mapx"));
                                    timeLineModel.setMapy(tmp.getString("mapy"));
                                    modelList.add(timeLineModel);
                                }
                            }
                        }catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (NullPointerException e){
                            Log.i("TAG","NULL point = "+e.getMessage());
                        }
                        catch (JSONException e) {
                            Log.i("TAG","error = "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }else{

            }

            return null;
        }
        private String mapUrl(String mapx, String mapy){
            StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList");
            try {
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
                urlBuilder.append("&" + URLEncoder.encode("contentType", "UTF-8") + "=" );
                urlBuilder.append("&"+URLEncoder.encode("mapX","UTF-8") + "=" + URLEncoder.encode(mapx,"UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("mapY","UTF-8") + "=" + URLEncoder.encode(mapy,"UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("radius","UTF-8") + "=" + URLEncoder.encode("2000","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("listYN","UTF-8") + "=" + URLEncoder.encode("Y","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("MobileOS","UTF-8") + "=" +URLEncoder.encode("AND","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("MobileApp","UTF-8") + "=" +URLEncoder.encode("TripHelper","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("arrange","UTF-8") + "=" + URLEncoder.encode("B","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("_type","UTF-8") + "=" +URLEncoder.encode("json","UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return urlBuilder.toString();
        }
        private String spotUrl(String city){
            StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList");
            try {
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
                urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" );
                urlBuilder.append("&"+URLEncoder.encode("areaCode","UTF-8") + "=" + URLEncoder.encode(city,"UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("sigunguCode","UTF-8") + "=");
                urlBuilder.append("&"+URLEncoder.encode("cat1","UTF-8") + "=");
                urlBuilder.append("&"+URLEncoder.encode("cat2","UTF-8") + "=");
                urlBuilder.append("&"+URLEncoder.encode("cat3","UTF-8") + "=");
                urlBuilder.append("&"+URLEncoder.encode("listYN","UTF-8") + "=" + URLEncoder.encode("Y","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("MobileOS","UTF-8") + "=" +URLEncoder.encode("AND","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("MobileApp","UTF-8") + "=" +URLEncoder.encode("TripHelper","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("arrange","UTF-8") + "=" + URLEncoder.encode("B","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("_type","UTF-8") + "=" +URLEncoder.encode("json","UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return urlBuilder.toString();
        }

    }

    public class getLocation extends AsyncTask<ArrayList<String>, Void, Boolean>{
        ProgressDialog progressDialog = new ProgressDialog(FunctionPlan2.this);
        ArrayList<String> tempList;

        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("loading...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(ArrayList<String>... params) {
            tempList = params[0];
            try {
                for(int i = 0; i< spotCount; i++){
                    String[] keyword = new String[]{tempList.get(i)};
                    URL url = new URL(makeUrl(KEYWORD, keyword));
//                    URL url = new URL(urlsetting(tempList.get(i)));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");
                    BufferedReader rd;
                    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    } else {
                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    }

                    String strr= null;
                    String line;
                    while ((line = rd.readLine()) != null) {
                        strr = line;
                        Log.i("TAG","result = "+line);
                    }
                    rd.close();
                    conn.disconnect();

                    JSONObject jsonObject = new JSONObject(strr);
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
                    JSONObject jsonObject2 = new JSONObject(jsonObject1.getJSONObject("body").toString());
                    JSONObject jsonObject3 = new JSONObject(jsonObject2.getJSONObject("items").toString());
                    JSONObject jsonObject4 = new JSONObject(jsonObject3.getJSONObject("item").toString());
                    Log.i("TAG","map x = "+jsonObject4.getString("mapx"));
                    Log.i("TAG","map y = "+jsonObject4.getString("mapy"));
                    locationXY[i][0] = jsonObject4.getString("mapx");
                    locationXY[i][1] = jsonObject4.getString("mapy");
                }
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                Log.i("TAG","NULL point = "+e.getMessage());
            }
            catch (JSONException e) {
                Log.i("TAG","error = "+e.getMessage());
                e.printStackTrace();
            }
            Log.i("TAG","doInBackGround end ");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            for(int i = 0; i < spotCount; i++){
                Location location = new Location("start");
                location.setLatitude(Double.parseDouble(locationXY[i][0]));
                location.setLongitude(Double.parseDouble(locationXY[i][1]));;
                for(int j = 0; j < spotCount; j++){
                    Location tmp = new Location("end");
                    tmp.setLatitude(Double.parseDouble(locationXY[j][0]));
                    tmp.setLongitude(Double.parseDouble(locationXY[j][1]));
                    dfs_map[i][j] = (int)location.distanceTo(tmp) / 1000;
                }
            }
            for(int i = 0; i< spotCount; i++){
                for (int j = 0; j< spotCount; j++){
                    Log.i("TAG","map = "+ dfs_map[i][j]);
                }
            }
            dfs_node.add(0);
            dfs(0,0);
            for(int i = 0; i< spotCount; i++) {
                Log.i("TAG", "shorest path = " + dfs_shortestNode.get(i));
            }
            progressDialog.dismiss();
        }
        private void dfs(int s, int weight){
            Log.i("TAG","start s = "+s);
            if(dfs_node.size() == spotCount && dfs_min > weight){
                dfs_min = weight;
                dfs_shortestNode = (Stack<Integer>) dfs_node.clone();
            }

            for(int i = 0; i< spotCount; i++){
                if(dfs_map[s][i] != 0 && !dfs_visit[i]){
                    Log.i("TAG","to i = "+i);
                    Log.i("TAG","weight = "+weight);
                    dfs_visit[s] = true;
                    dfs_node.add(i);
                    dfs(i, weight+dfs_map[s][i]);
                    dfs_node.pop();
                    dfs_visit[s] = false;
                }
            }
        }

        private String urlsetting(String keyword){

            StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword");

            try {
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
                urlBuilder.append("&" + URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(keyword,"UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("areaCode","UTF-8") + "=");
                urlBuilder.append("&"+URLEncoder.encode("sigunguCode","UTF-8") + "=");
                urlBuilder.append("&"+URLEncoder.encode("cat1","UTF-8") + "=");
                urlBuilder.append("&"+URLEncoder.encode("cat2","UTF-8") + "=");
                urlBuilder.append("&"+URLEncoder.encode("cat3","UTF-8") + "=");
                urlBuilder.append("&"+URLEncoder.encode("listYN","UTF-8") + "=" + URLEncoder.encode("Y","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("MobileOS","UTF-8") + "=" +URLEncoder.encode("AND","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("MobileApp","UTF-8") + "=" +URLEncoder.encode("TripHelper","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("arrange","UTF-8") + "=" + URLEncoder.encode("A","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("25248","UTF-8"));
                urlBuilder.append("&"+URLEncoder.encode("_type","UTF-8") + "=" +URLEncoder.encode("json","UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return urlBuilder.toString();
        }
    }

}


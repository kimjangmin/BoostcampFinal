package com.jm.gon.triphelper.functionplan2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.jm.gon.triphelper.CustomClickListener;
import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.functionplan3.FunctionPlan3;

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

public class FunctionPlan2 extends AppCompatActivity implements CustomClickListener {
    private Stack<Integer> dfs_node;
    private Stack<Integer> dfs_shortestNode;
    private int[][] dfs_map;
    private double dfs_min;
    private boolean[] dfs_visit;

    private String[][] locationXY;
    private int spotCount;

    private RecyclerView rv_FunctionPlan2_timeline;
    private List<TimeLineModel> modelList;
    private List<TimeLineModel> keywordmodelList;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_function_plan2);
        Intent intent = getIntent();
        date = intent.getIntExtra("date",-1);
        theme = intent.getStringExtra("theme");
        city = intent.getStringExtra("city");
        spotList = (ArrayList<String>) intent.getSerializableExtra("spot");
        spotCount = spotList.size();
        dfs_min =999999999;
        locationXY = new String[spotCount][2];
        dfs_map = new int[spotCount][spotCount];
        dfs_visit = new boolean[spotCount];
        Arrays.fill(dfs_visit, Boolean.FALSE);
        modelList = new ArrayList<>();
        keywordmodelList = new ArrayList<>();
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

        TimeLineAdapter timeLineAdapter = new TimeLineAdapter(modelList, this, this);
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
                    Log.i("TAG","makeUrl X = "+params[0] + " Y = "+params[1]);
                    urlBuilder.append("&"+URLEncoder.encode("mapX","UTF-8") + "=" + URLEncoder.encode(params[0],"UTF-8"));
                    urlBuilder.append("&"+URLEncoder.encode("mapY","UTF-8") + "=" + URLEncoder.encode(params[1],"UTF-8"));
                    urlBuilder.append("&"+URLEncoder.encode("radius","UTF-8") + "=" + URLEncoder.encode("2000","UTF-8"));
                    break;
                case AREABASE :
                    urlBuilder.append("&"+URLEncoder.encode("areaCode","UTF-8") + "=" + URLEncoder.encode(params[0],"UTF-8"));
                    break;
                case KEYWORD :
                    urlBuilder.append("&" + URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(params[0],"UTF-8"));
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlBuilder.toString();
    }
    private TimeLineModel doJson(JSONObject jsonStr){
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timeLineModel;
    }
    public void parsingJson(String parsingStr, int type){
        try {
            JSONObject jsonObject = new JSONObject(parsingStr);
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
            JSONObject jsonObject2 = new JSONObject(jsonObject1.getJSONObject("body").toString());
            JSONObject jsonObject3 = new JSONObject(jsonObject2.getJSONObject("items").toString());
            Object object = jsonObject3.get("item");
            if(type <= spotCount) {
                if(object instanceof JSONArray){
                    for (int k = 0; k < 2; k++) {
                        modelList.add( doJson( ((JSONArray) object).getJSONObject(k) ) );
                    }
                }else{
                    modelList.add( doJson( (JSONObject)object ) );
                }
            }else{
                if(object instanceof JSONArray){
                    for (int k = type; k < date ; k++){
                        modelList.add(doJson(((JSONArray)object).getJSONObject(k)));
                    }
                }else{
                    modelList.add(doJson( (JSONObject)object) );
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

        //
        @Override
        protected Boolean doInBackground(ArrayList<String>... params) {
            tempList = params[0];
            try {
                for(int i = 0; i< spotCount; i++){
                    String[] keyword = new String[]{tempList.get(i)};
                    Log.i("TAG","doInBackground keyword = "+keyword[0]);
                    URL url = new URL(makeUrl(KEYWORD, keyword));
                    Log.i("TAG","Url = "+url.toString());
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
                    Object object = jsonObject3.get("item");
                    if(object instanceof JSONArray){
                        JSONArray jsonArray = (JSONArray)object;
                        for(int m=0 ; m < jsonArray.length() ; m++){
                            JSONObject temp = jsonArray.getJSONObject(m);
                            if( temp.getString("title").equals(keyword[0]) ){
                                keywordmodelList.add(doJson(temp));
                                locationXY[i][0] = temp.getString("mapx");
                                locationXY[i][1] = temp.getString("mapy");
                                break;
                            }
                        }
                    }
                    else if(object instanceof JSONObject){
                        JSONObject temp = (JSONObject)object;
                        if( (temp.getString("title")).equals(keyword[0]) ) {
                            keywordmodelList.add(doJson(temp));
                            locationXY[i][0] = temp.getString("mapx");
                            locationXY[i][1] = temp.getString("mapy");
                        }
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
            Log.i("TAG","doInBackGround end ");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

            //거리맵그리기
            for(int i = 0; i < spotCount; i++){
                Location location = new Location("start");
                if(locationXY[i][0]!=null) {
                    location.setLatitude(Double.parseDouble(locationXY[i][0]));
                    location.setLongitude(Double.parseDouble(locationXY[i][1]));
                }
                for(int j = 0; j < spotCount; j++){
                    Location tmp = new Location("end");
                    if(locationXY[j][0]!=null) {
                        tmp.setLatitude(Double.parseDouble(locationXY[j][0]));
                        tmp.setLongitude(Double.parseDouble(locationXY[j][1]));
                    }
                    dfs_map[i][j] = (int)location.distanceTo(tmp) / 1000;
                }
            }
            for(int i = 0; i< spotCount; i++){
                for (int j = 0; j< spotCount; j++){
                    Log.i("TAG","map = "+ dfs_map[i][j]);
                }
            }
            dfs_node.add(0);
            //최단거리찾기 시작
            dfs(0,0);
            for(int i = 0; i< spotCount; i++) {
                Log.i("TAG", "shorest path = " + dfs_shortestNode.get(i));
            }

            progressDialog.dismiss();

            //입력한 거점 갯수가 날짜보다 작을때!
            if(spotCount <=date){
                for(int i=0;i<date; i++){
                    //내 입력 목록갯수 * 3만큼 구하려고.
                    if( i< spotList.size()){
                        try {
                            //내가 입력한 것 들부터 넣고.
                            //
//                            String[] mykey = new String[]{spotList.get(dfs_shortestNode.get(i))};
//                            String myspot = new NetworkApi().execute(makeUrl(KEYWORD, mykey)).get();
//                            parsingJson(myspot, 0);

                            Log.i("TAG","onPostExecute X = "+locationXY[dfs_shortestNode.get(i)][0] + " Y = "+locationXY[dfs_shortestNode.get(i)][1]);
                            modelList.add(keywordmodelList.get(dfs_shortestNode.get(i)));
                            String[] arr = new String[]{locationXY[dfs_shortestNode.get(i)][0], locationXY[dfs_shortestNode.get(i)][1]};
                            String result = new NetworkApi().execute(makeUrl(LOCATIONBASE, arr)).get();
                            parsingJson(result, i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        //입력하고 남으면
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

    @Override
    public void onClick(TimeLineModel timeLineModel) {
        Intent intent = new Intent(this, FunctionPlan3.class);
        intent.putExtra("model", timeLineModel);
        startActivity(intent);
    }

    @Override
    public void onClick(String url) {

    }
}


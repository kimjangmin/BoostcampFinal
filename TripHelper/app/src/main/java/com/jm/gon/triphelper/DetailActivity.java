package com.jm.gon.triphelper;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jm.gon.triphelper.functionplan2.TimeLineModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private String contentid;
    private String originaltitle;
    private boolean hasImage;
    String[] urlArr;
    String title, tel, addr, overview;
    private TextView tv_DetailActivity_title;
    private TextView tv_DetailActivity_tel;
    private TextView tv_DetailActivity_addr;
    private TextView tv_DetailActivity_overview;
    private ViewPager vp_DetailActivity;
    DetailActivityAdapter activityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);

        tv_DetailActivity_title = (TextView)findViewById(R.id.tv_DetailActivity_title);
        tv_DetailActivity_tel = (TextView)findViewById(R.id.tv_DetailActivity_tel);
        tv_DetailActivity_addr = (TextView)findViewById(R.id.tv_DetailActivity_addr);
        tv_DetailActivity_overview = (TextView)findViewById(R.id.tv_DetailActivity_overview);
        vp_DetailActivity = (ViewPager)findViewById(R.id.vp_DetailActivity);

        title=null;
        tel=null;
        addr=null;
        overview=null;
        Intent intent = getIntent();
        originaltitle = intent.getStringExtra("title");
        tv_DetailActivity_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tv_DetailActivity_tel.getText().toString()) );
                startActivity(callIntent);
            }
        });

        new FirstAsynctask().execute();
    }
    public class FirstAsynctask extends AsyncTask<Void, Void, String>{
        String firstresult;
        @Override
        protected String doInBackground(Void... params) {
            try{
                URL url = new URL(makeUrl(originaltitle));
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
                    firstresult = line;
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
            return firstresult;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parsingJson(s);
            new SecondAsynctask().execute();
        }
    }
    public class SecondAsynctask extends AsyncTask<Void,Void,String[]>{

        @Override
        protected String[] doInBackground(Void... params) {
            int ctmp = 1;
            if(hasImage == true) ctmp = 2;
            String[] strtmp = new String[2];
            for(int i=0 ; i<ctmp ; i++) {
                try {
                    URL url = new URL(makeUrl2()[i]);
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
                        strtmp[i] = line;
                        Log.i("TAG", "result = " + line);
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
            }
            return strtmp;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            parsingJson2(strings);

            tv_DetailActivity_title.setText(title);
            tv_DetailActivity_tel.setText(tel);
            tv_DetailActivity_addr.setText(addr);
            tv_DetailActivity_overview.setText(overview);

            Log.i("TAG","urlArr size = "+urlArr.length);

            activityAdapter = new DetailActivityAdapter(getApplicationContext(), urlArr);
            vp_DetailActivity.setAdapter(activityAdapter);

        }
    }
    public String[] makeUrl2() {
        StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon");
        StringBuilder urlBuilder2 = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailImage");
        try {
            urlBuilder.append("?" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(contentid, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("AND", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("TripHelper", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("defaultYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("firstImageYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("areacodeYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("catcodeYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("addrinfoYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("mapinfoYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("overviewYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("transGuideYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(hasImage == true){
            try {
                urlBuilder2.append("?" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(contentid, "UTF-8"));
                urlBuilder2.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
                urlBuilder2.append("&" + URLEncoder.encode("imageYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                urlBuilder2.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("AND", "UTF-8"));
                urlBuilder2.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("TripHelper", "UTF-8"));
                urlBuilder2.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Log.i("TAG", "url = " + urlBuilder.toString());
        Log.i("TAG"," url2 = "+urlBuilder2.toString());
        return new String[]{urlBuilder.toString(), urlBuilder2.toString()};
    }
    public String makeUrl(String params) {
        StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword");
        try {
            urlBuilder.append("?" + URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(params, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("AND", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("TripHelper", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("TAG", "url = " + urlBuilder.toString());
        return urlBuilder.toString();
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
                for (int k = 0; k < ((JSONArray)object).length(); k++) {
                    JSONObject tmp = ((JSONArray) object).getJSONObject(k);
                    Log.i("TAG"," this is parsing k = "+k);
                    if((tmp.getString("title")).equals(originaltitle))
                    {
                        if(tmp.has("firstimage")){
                            hasImage = true;
                        }
                        contentid = (tmp.getString("contentid"));
                        break;
                    }
                }
            }else{
                JSONObject tmp = (JSONObject)object;
                Log.i("TAG","this is jsonobject");
                if( (tmp.getString("title")).equals(originaltitle))
                {
                    Log.i("GAG","title same");
                    if(tmp.has("firstimage")){
                        hasImage = true;
                    }
                    contentid = tmp.getString("contentid");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parsingJson2(String[] parsingStr){
        int count=1;
        if(hasImage == true){
            count=2;
        }
        for(int i=0;i<count; i++) {
            try {
                JSONObject jsonObject = new JSONObject(parsingStr[i]);
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getJSONObject("body").toString());
                JSONObject jsonObject3 = new JSONObject(jsonObject2.getJSONObject("items").toString());
                Object object = jsonObject3.get("item");
                Log.i("TAG", "parsingJson start");
                if (object instanceof JSONArray) {
                    urlArr = new String[(((JSONArray) object)).length()];
                    for (int k = 0; k < ((JSONArray) object).length(); k++) {
                        JSONObject tmp = ((JSONArray) object).getJSONObject(k);
                        urlArr[k] = tmp.getString("originimgurl");
                    }
                } else {
                    Log.i("TAG", "this is jsonobject");
                    JSONObject tmp = (JSONObject)object;
                    if(tmp.has("title")){
                        title = tmp.getString("title");
                    }
                    if(tmp.has("tel")){
                        tel = tmp.getString("tel");
                    }
                    if(tmp.has("addr1")){
                        addr = tmp.getString("addr1");
                    }
                    if(tmp.has("overview")){
                        overview = tmp.getString("overview");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

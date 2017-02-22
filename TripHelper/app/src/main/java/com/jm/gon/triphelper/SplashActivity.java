package com.jm.gon.triphelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;
import com.jm.gon.triphelper.db.DbHelper;
import com.jm.gon.triphelper.db.DbTable;

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
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity implements OnProgressBarListener {
    private SQLiteDatabase sqLiteDatabase;
    private DbHelper dbHelper;
    private ImageView iv_SplashActivity_logo;
    private Timer timer;
    private TimerTask task;
    private NumberProgressBar pb_SplashActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Log.i("TAG","make Oncreate");
        iv_SplashActivity_logo = (ImageView)findViewById(R.id.iv_SplashActivity_logo);
        iv_SplashActivity_logo.setImageResource(R.drawable.logo);
        pb_SplashActivity = (NumberProgressBar)findViewById(R.id.pb_SplashActivity);
        dbHelper = new DbHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Log.i("TAG","setting finish");
        Cursor cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME,null,null,null,null,null,null);
        Log.i("TAG","cursor query finish");

        pb_SplashActivity.setOnProgressBarListener(this);

        timer = new Timer();

        if(cursor.getCount()==0){
            new InitData().execute();
        }
        else{
            Log.i("TAG","start else");
            timer.schedule(setTimeTask(), 100, 10);
        }
    }

    private TimerTask setTimeTask(){
        try {
            task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pb_SplashActivity.incrementProgressBy(1);
                        }
                    });
                }
            };
        }catch(Exception e){
            e.printStackTrace();
        }
        return task;
    }

    private class InitData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i("TAG","start onpreexecute");
            timer.schedule(setTimeTask(), 100, 500);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("TAG","start doinback");
            String tempStr=null;
            sqLiteDatabase.beginTransaction();
            try {
                URL url = new URL(urlsetting());

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
                    tempStr = line;
                    Log.i("TAG","result = "+line);
                }

                rd.close();
                conn.disconnect();
                JSONObject jsonObject = new JSONObject(tempStr);
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
                JSONObject object = new JSONObject(jsonObject1.getJSONObject("body").toString());
                JSONObject oobject = new JSONObject(object.getJSONObject("items").toString());
                JSONArray jsonArray = oobject.getJSONArray("item");

                for(int i = 0 ;i < jsonArray.length() ; i++){
                    JSONObject tmp = jsonArray.getJSONObject(i);
                    if(tmp.has("mapx")) {
                        String title = tmp.getString("title");
                        title = title.replaceAll("'", "");
                        String contenttypeid = tmp.getString("contenttypeid");
                        String areacode;
                        if(tmp.has("areacode")) {
                            areacode = tmp.getString("areacode");
                        }else{
                            areacode="";
                        }
                        sqLiteDatabase.execSQL("insert into " + DbTable.AutoCompleteTable.TABLENAME +
                                " ( " + DbTable.AutoCompleteTable.TITLE + "," + DbTable.AutoCompleteTable.CONTENTTYPEID + "," + DbTable.AutoCompleteTable.AREACODE + " ) "
                                + " values ( '" + title + "', '" + contenttypeid + "', '" + areacode + "' );");
                    }
                }
                sqLiteDatabase.setTransactionSuccessful();;
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.i("TAG","JSONException = "+e.getMessage());
                e.printStackTrace();
            }finally {
                sqLiteDatabase.endTransaction();
                sqLiteDatabase.close();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            timer.cancel();
            timer = new Timer();
            timer.scheduleAtFixedRate(setTimeTask(), 100, 50);
            super.onPreExecute();
        }

        private String urlsetting(){
            StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList");
            try {
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
                urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" );
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
    @Override
    public void onProgressChange(int current, int max) {
        if (current == max) {
            pb_SplashActivity.setProgress(0);
            timer.cancel();
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

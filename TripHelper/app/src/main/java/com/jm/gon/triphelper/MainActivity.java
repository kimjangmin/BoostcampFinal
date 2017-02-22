package com.jm.gon.triphelper;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.jm.gon.triphelper.Network_MainActivity.ApiDataTable;
import com.jm.gon.triphelper.Network_MainActivity.NetworkDataTable;
import com.jm.gon.triphelper.Network_MainActivity.NetworkMethod;
import com.jm.gon.triphelper.Network_MainActivity.NetworkSetting;
import com.jm.gon.triphelper.db.DbHelper;
import com.jm.gon.triphelper.db.DbTable;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 김장민 on 2017-02-17.
 */

public class MainActivity extends AppCompatActivity {

    HorizontalScrollView hsv_function;
    private NetworkMethod networkMethod;
    RecyclerView rv_newPhoto;
    //    MainPage_RVAdapter adapter;

    private Button main_camerabtn;
    private Button main_planbtn;
    private Button main_foodbtn;
    private Button main_staybtn;
    private Button main_spotbtn;
    private Button main_mybtn;

    ApiDataTable apiDataTable;
    Uri photoUrl;
    private SQLiteDatabase sqLiteDatabase;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        init();
        hsv_function = (HorizontalScrollView) findViewById(R.id.hsv_function);
        main_camerabtn = (Button) findViewById(R.id.main_camerabtn);
        main_planbtn = (Button)findViewById(R.id.main_planbtn);
        main_foodbtn = (Button)findViewById(R.id.main_foodbtn);
        main_staybtn = (Button)findViewById(R.id.main_staybtn);
        main_spotbtn = (Button)findViewById(R.id.main_spotbtn);
        main_mybtn = (Button)findViewById(R.id.main_mybtn);


        final PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "permission", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FunctionCamera.class);
                startActivity(intent);
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "reject" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };



        main_camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TedPermission(getApplicationContext())
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA)
                        .check();
            }
        });

        main_planbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FunctionPlan1.class);
                startActivity(intent);
            }
        });
        main_foodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searching("39");
            }
        });
        main_staybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searching("32");
            }
        });
        main_spotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searching("spot");
            }
        });
        main_mybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void searching(String type){
        Intent intent = new Intent(this, FunctionSearch.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
    private void init() {
        dbHelper = new DbHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME,null,null,null,null,null,null);
        if(cursor.getCount()==0){
            Log.i("TAG","async task start");
            new Asynccc().execute();
        }
    }

    private void network(String ip, int port){

        NetworkSetting networkSetting = NetworkSetting.getInstance();
        networkSetting.buildNetworkService(ip, port);
        networkMethod = NetworkSetting.getInstance().getNetworkMethod();
        Call<List<NetworkDataTable>> getAll = networkMethod.getAllData();
        getAll.enqueue(new Callback<List<NetworkDataTable>>() {
            @Override
            public void onResponse(Call<List<NetworkDataTable>> call, Response<List<NetworkDataTable>> response) {
                if (response.isSuccessful()) {
                    List<NetworkDataTable> dataTables = response.body();
                    String show = "";

                    for (NetworkDataTable dataTable : dataTables) {
                        show += "date = " + dataTable.getDate() + "\n url = " + dataTable.getPictureUrl() + "\n";
                    }
                    Log.i("TAG", show);
                } else
                    Log.i("TAG", "errorcode = " + response.code());
            }

            @Override
            public void onFailure(Call<List<NetworkDataTable>> call, Throwable t) {
                t.printStackTrace();
                Log.i("TAG", "network is fail errorcode = ");
            }
        });
    }

    private class Asynccc extends AsyncTask<String, Void, Void>{
        String serviceKey = "atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D";
        String contentTypeId = "";
        String areaCode ="";
        String sigunguCode ="";
        String cat1 = "";
        String cat2 = "";
        String cat3 = "";
        String listYN="Y";
        String MobileOS = "AND";
        String MobileApp = "TripHelper";
        String arrange = "A";
        String numOfRows = "20";
        String type = "json";
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("DB Update...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            /*Retrofit client = new Retrofit.Builder().baseUrl("http://api.visitkorea.or.kr/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build();
            NetworkMethod networkMethod = client.create(NetworkMethod.class);
            Call<List<ApiDataTable>> call = networkMethod.getData(serviceKey, contentTypeId, areaCode, sigunguCode, cat1, cat2, cat3, listYN, MobileOS, MobileApp, arrange, numOfRows, type);
            call.enqueue(new Callback<List<ApiDataTable>>() {
                @Override
                public void onResponse(Call<List<ApiDataTable>> call, Response<List<ApiDataTable>> response) {
                    if(response.isSuccessful()){
                        List<ApiDataTable> apiDataTables = response.body();
                        String show="";
                        for(ApiDataTable apiDataTable : apiDataTables) {
                            show = "title = "+apiDataTable.getTitle();
                            Log.i("TAG","title = " + show);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<ApiDataTable>> call, Throwable t) {
                    Log.i("TAG", "network is fail errorcode = " + t.getMessage());
                }
            });*/

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
                if(jsonObject.has("response")){
                    Log.i("TAG","rows int = "+object.getInt("numOfRows"));
                    Log.i("TAG","total count int = "+object.getInt("totalCount"));
                    Log.i("TAG","page int = "+(object.getInt("totalCount")/object.getInt("numOfRows")));
                    Log.i("TAG","mayyybe = "+jsonObject1.has("header"));
                    Log.i("TAG","maybe = "+jsonObject1.getJSONObject("body").has("items"));
                    Log.i("TAG","it is not null");
                };

                JSONArray jsonArray = oobject.getJSONArray("item");

                for(int i = 0 ;i < jsonArray.length() ; i++){
                    JSONObject tmp = jsonArray.getJSONObject(i);
                    if(tmp.has("mapx")) {
                        String title = tmp.getString("title");
                        title = title.replaceAll("'", "");
                        String contenttypeid = tmp.getString("contenttypeid");
                        sqLiteDatabase.execSQL("insert into " + DbTable.AutoCompleteTable.TABLENAME +
                                " ( " + DbTable.AutoCompleteTable.TITLE + "," + DbTable.AutoCompleteTable.CONTENTTYPEID + " ) "
                                + " values ( '" + title + "', '" + contenttypeid + "' );");
                        Log.i("TAG", "PARSING TITLE = " + title);
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
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
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
}

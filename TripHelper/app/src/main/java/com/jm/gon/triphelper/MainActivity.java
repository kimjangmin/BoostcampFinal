package com.jm.gon.triphelper;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jm.gon.triphelper.Network_MainActivity.ApiDataTable;
import com.jm.gon.triphelper.Network_MainActivity.NetworkMethod;
import com.jm.gon.triphelper.db.DbHelper;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jm.gon.triphelper.functionplan2.TimeLineModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 김장민 on 2017-02-17.
 */

public class MainActivity extends AppCompatActivity {

    HorizontalScrollView hsv_function;
    private NetworkMethod networkMethod;
    RecyclerView rv_newPhoto;
    //    MainPage_RVAdapter adapter;

    private ImageButton ibn_ActivityMain_camera;
    private ImageButton ibn_ActivityMain_plan;
    private ImageButton ibn_ActivityMain_restaurant;
    private ImageButton ibn_ActivityMain_room;
    private ImageButton ibn_ActivityMain_spot;
    private ImageButton ibn_ActivityMain_mypage;
    private RecyclerView rv_MainActivity;
    private ArrayList<TimeLineModel> modelList;
    public MainActivityAdapter adapter;
    HttpConnectControl control;

    private void tag(String str){
        Log.i("TAG", str);
    }
    private String[] dataFormat(){
        tag("start");
        String startdate, enddate;
        String year,month;
        String year1,month1;
        Calendar calendar = Calendar.getInstance();
        if((calendar.get(Calendar.MONTH)) == Calendar.DECEMBER) {
            tag("december");
            year = ((calendar.get(Calendar.YEAR)))+"";
            year1 = ((calendar.get(Calendar.YEAR))+1)+"";
            month = (calendar.get((Calendar.MONTH))+1)+"";
            month1 = "01";
        }else{
            tag("no december");
            year = (calendar.get(Calendar.YEAR))+"";
            year1 = (calendar.get(Calendar.YEAR))+"";
            if((calendar.get(Calendar.MONTH)+1) < 10){
                tag("10month down");
                month = "0"+(calendar.get((Calendar.MONTH))+1);
                if((calendar.get(Calendar.MONTH)+1) == 9) {
                    month1 = "" + (calendar.get((Calendar.MONTH)) + 1);
                }else{
                    month1 = "0" + (calendar.get((Calendar.MONTH)) + 1);
                }
            }else{
                tag("10month up");
                month = ""+(calendar.get(Calendar.MONTH)+1);
                month1 = "" + (calendar.get(Calendar.MONTH)+1);
            }
        }
        startdate = year+month+"01";
        enddate = year1+month1+"01";
        return new String[]{startdate, enddate};

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        hsv_function = (HorizontalScrollView) findViewById(R.id.hsv_function);
        ibn_ActivityMain_camera = (ImageButton) findViewById(R.id.ibn_MainActivity_camera);
        ibn_ActivityMain_plan = (ImageButton)findViewById(R.id.ibn_MainActivity_plan);
        ibn_ActivityMain_restaurant = (ImageButton)findViewById(R.id.ibn_MainActivity_restaurant);
        ibn_ActivityMain_room = (ImageButton)findViewById(R.id.ibn_MainActivity_room);
        ibn_ActivityMain_spot = (ImageButton)findViewById(R.id.ibn_MainActivity_spot);
        ibn_ActivityMain_mypage = (ImageButton)findViewById(R.id.ibn_MainActivity_mypage);
        rv_MainActivity = (RecyclerView)findViewById(R.id.rv_MainActivity);

        modelList = new ArrayList<>();

        control = new HttpConnectControl(this);

        modelList = (ArrayList<TimeLineModel>) control.getResult(control.FESTIVAL, dataFormat()).clone();

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

        ibn_ActivityMain_camera.setOnClickListener(new View.OnClickListener() {
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

        ibn_ActivityMain_plan.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FunctionPlan1.class);
                startActivity(intent);
            }
        });
        ibn_ActivityMain_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searching("39");
            }
        });
        ibn_ActivityMain_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searching("32");
            }
        });
        ibn_ActivityMain_spot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searching("spot");
            }
        });
        ibn_ActivityMain_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        adapter = new MainActivityAdapter(modelList);
        rv_MainActivity.setLayoutManager(new LinearLayoutManager(this));
        rv_MainActivity.setHasFixedSize(true);
        rv_MainActivity.setAdapter(adapter);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(adapter.modelList.size()!=0) {
                    adapter.notifyDataSetChanged();
                }else {
                    Log.i("TAG1","adapter modellist = "+adapter.modelList.size());
                    timer.cancel();
                }
            }
        }, 500, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 1000);


    }
    public void searching(String type){
        Intent intent = new Intent(this, FunctionSearch.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}

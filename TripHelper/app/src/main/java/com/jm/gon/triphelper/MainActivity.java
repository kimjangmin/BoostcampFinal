package com.jm.gon.triphelper;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jm.gon.triphelper.mypage.MyActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by 김장민 on 2017-02-17.
 */

public class MainActivity extends AppCompatActivity {

    HorizontalScrollView hsv_function;

    private ImageButton ibn_ActivityMain_camera;
    private ImageButton ibn_ActivityMain_plan;
    private ImageButton ibn_ActivityMain_restaurant;
    private ImageButton ibn_ActivityMain_room;
    private ImageButton ibn_ActivityMain_spot;
    private ImageButton ibn_ActivityMain_mypage;
    private TextView tv_main_festival;
    private RecyclerView rv_MainActivity;
    private ArrayList<DataModel> modelList;
    public MainActivityAdapter adapter;
    HttpConnectControl control;


    //실행시 스플래쉬화면후 최초로 보이는 메인화면입니다.
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
        tv_main_festival = (TextView)findViewById(R.id.tv_main_festival);
        tv_main_festival.setText(dataFormat()[0].substring(0,4)+"년 "+dataFormat()[0].substring(4,6)+"월 "+"행사");
        rv_MainActivity = (RecyclerView)findViewById(R.id.rv_MainActivity);

        modelList = new ArrayList<>();
        adapter = new MainActivityAdapter(modelList, this);
        control = new HttpConnectControl();
        control.startAsync(adapter, control.FESTIVAL, dataFormat());

        //마시멜로부터 퍼미션만으로 해결이 안되는 보안문제를 조금더 쉽게 해결할수 있는 소스입니다.
        final PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
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
                Intent intent = new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent);
            }
        });


        rv_MainActivity.setLayoutManager(new LinearLayoutManager(this));
        rv_MainActivity.setHasFixedSize(true);
        rv_MainActivity.setAdapter(adapter);

    }
    private String[] dataFormat(){
        String startdate, enddate;
        String year,month;
        String year1,month1;
        Calendar calendar = Calendar.getInstance();
        if((calendar.get(Calendar.MONTH)) == Calendar.DECEMBER) {
            year = ((calendar.get(Calendar.YEAR)))+"";
            year1 = ((calendar.get(Calendar.YEAR))+1)+"";
            month = (calendar.get((Calendar.MONTH))+1)+"";
            month1 = "01";
        }else{
            year = (calendar.get(Calendar.YEAR))+"";
            year1 = (calendar.get(Calendar.YEAR))+"";
            if((calendar.get(Calendar.MONTH)+1) < 10){
                month = "0"+(calendar.get((Calendar.MONTH))+1);
                if((calendar.get(Calendar.MONTH)+1) == 9) {
                    month1 = "" + (calendar.get((Calendar.MONTH)) + 1);
                }else{
                    month1 = "0" + (calendar.get((Calendar.MONTH)) + 1);
                }
            }else{
                month = ""+(calendar.get(Calendar.MONTH)+1);
                month1 = "" + (calendar.get(Calendar.MONTH)+1);
            }
        }
        startdate = year+month+"01";
        enddate = year1+month1+"01";
        return new String[]{startdate, enddate};

    }
    public void searching(String type){
        Intent intent = new Intent(this, FunctionSearch.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
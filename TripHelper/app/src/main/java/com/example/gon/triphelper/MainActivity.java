package com.example.gon.triphelper;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.example.gon.triphelper.Network_MainActivity.NetworkDataTable;
import com.example.gon.triphelper.Network_MainActivity.NetworkMethod;
import com.example.gon.triphelper.Network_MainActivity.NetworkSetting;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

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
    Button main_camerabtn;
    Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        titlebar 없애기
//        FEATURE_ACTION_BAR  - 액션바를 사용(기본 값)
//        FEATURE_ACTION_BAR_OVERLAY - 액션바와 액티비티의 내용을 중첩
//        FEATURE_CONTEXT_MENU- 컨텍스트 메뉴
//        FEATURE_CUSTOM_TITLE-  커스텀 타이틀바 사용
//        FEATURE_INDETERMINATE_PROGRESS  -액션바에 원형 프로그래스바 표시
//        FEATURE_LEFT_ICON -액션바 왼쪽에 아이콘 표시
//        FEATURE_NO_TITLE- 액션바(타이틀바) 생략
//        FEATURE_OPTIONS_PANEL-옵션 패널 표시
//        FEATURE_PROGRESS-액션바에 막대 프로그래스바 표시
//        FEATURE_RIGHT_ICON-액션바 오른쪽에 아이콘 표시

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        hsv_function = (HorizontalScrollView) findViewById(R.id.hsv_function);

        final PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Function_Camera.class);
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };


        main_camerabtn = (Button) findViewById(R.id.main_camerabtn);
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
        NetworkSetting networkSetting = new NetworkSetting();
        networkSetting = NetworkSetting.getInstance();

        networkSetting.buildNetworkService("111.118.57.126", 3000);
        networkMethod = NetworkSetting.getInstance().getNetworkMethod();
        Log.i("TAG","networkMethod = "+networkMethod.toString());
        Call<List<NetworkDataTable>> getAll = networkMethod.getAllData();
        getAll.enqueue(new Callback<List<NetworkDataTable>>() {
            @Override
            public void onResponse(Call<List<NetworkDataTable>> call, Response<List<NetworkDataTable>> response) {
                if (response.isSuccessful()) {
                    List<NetworkDataTable> dataTables = response.body();
                    String show = "";

//                    adapter = new MainPage_RVAdapter(dataTables);
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

}
//        main_rv = (RecyclerView)findViewById(R.id.main_rv);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        main_rv.setLayoutManager(layoutManager);
//
//        main_rv.setAdapter(adapter);

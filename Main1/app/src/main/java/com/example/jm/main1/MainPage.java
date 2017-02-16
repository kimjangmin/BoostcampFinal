package com.example.jm.main1;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.example.jm.main1.Network.Main_DataTable;
import com.example.jm.main1.Network.Main_ServerController;
import com.example.jm.main1.Network.NetworkService;
import com.example.jm.main1.RecyclerView.MainPage_RVAdapter;
import com.example.jm.main1.camera.Cameramain;

import java.io.File;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends AppCompatActivity {

    HorizontalScrollView hsv;
    private NetworkService networkService;
    RecyclerView main_rv;
    MainPage_RVAdapter adapter;
    Button camerabtn;
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

        setContentView(R.layout.activity_main_page);
        hsv = (HorizontalScrollView)findViewById(R.id.hsv);

        camerabtn = (Button)findViewById(R.id.camera);
        camerabtn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(MainPage.this, Cameramain.class);
                     startActivity(intent);
                 }
            });

/*
        Main_ServerController controller = Main_ServerController.getInstance();
        controller.buildNetworkService("127.0.0.1", 3000);
        networkService = Main_ServerController.getInstance().getNetworkService();
        Call<List<Main_DataTable>> getAll = networkService.getAllData();
        getAll.enqueue(new Callback<List<Main_DataTable>>() {
            @Override
            public void onResponse(Call<List<Main_DataTable>> call, Response<List<Main_DataTable>> response) {
                if(response.isSuccessful()){
                    List<Main_DataTable> dataTables = response.body();
                    String show = "";
                    adapter = new MainPage_RVAdapter(dataTables);
                    for(Main_DataTable dataTable : dataTables){
                        show += "date = "+dataTable.getDate()+"\n url = "+dataTable.getPictureUrl()+"\n";
                    }
                    Log.i("TAG",show);
                }
                else
                    Log.i("TAG","errorcode = "+response.code());
            }

            @Override
            public void onFailure(Call<List<Main_DataTable>> call, Throwable t) {
                Log.i("TAG","errorcode = ");
            }
        });
        main_rv = (RecyclerView)findViewById(R.id.main_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        main_rv.setLayoutManager(layoutManager);

        main_rv.setAdapter(adapter);

            public File createImageFile(){
                String image = "temp_"+String.valueOf(System.currentTimeMillis())+".jpg";
                File storageDir = new File(Environment.getExternalStorageDirectory(), image);
                return storageDir;
            }
        });*/
    }
}

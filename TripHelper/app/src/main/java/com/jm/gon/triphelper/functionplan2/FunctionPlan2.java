package com.jm.gon.triphelper.functionplan2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.jm.gon.triphelper.CustomClickListener;
import com.jm.gon.triphelper.DataModel;
import com.jm.gon.triphelper.HttpConnectControl;
import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.functionplan3.FunctionPlan3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionPlan2 extends AppCompatActivity implements CustomClickListener {
    private boolean[] dfs_visit;

    private int userInputSpotCount;

    private RecyclerView rv_FunctionPlan2_timeline;
    private ArrayList<String> userInputSpotList;

    private int date;
    private String city;
    private String theme;


    //입력한 정보에 따라 계획이 나오는 클래스입니다. httpconnectcontrol라는 기능이 모여있는 클래스에서 대부분의 기능을 수행합니다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_function_plan2);
        Intent intent = getIntent();
        date = intent.getIntExtra("date", -1);
        theme = intent.getStringExtra("theme");
        city = intent.getStringExtra("city");
        userInputSpotList = (ArrayList<String>) intent.getSerializableExtra("spot");
        userInputSpotCount = userInputSpotList.size();
        dfs_visit = new boolean[userInputSpotCount];
        Arrays.fill(dfs_visit, Boolean.FALSE);

        rv_FunctionPlan2_timeline = (RecyclerView) findViewById(R.id.rv_FunctionPlan2_timeline);
        rv_FunctionPlan2_timeline.setLayoutManager(new LinearLayoutManager(this));
        rv_FunctionPlan2_timeline.setHasFixedSize(true);

        FunctionPlan2Adapter functionPlan2Adapter = new FunctionPlan2Adapter(this, this);
        HttpConnectControl control = new HttpConnectControl();
        control.startAsync(functionPlan2Adapter, date, city, control.KEYWORD, userInputSpotList);
        rv_FunctionPlan2_timeline.setAdapter(functionPlan2Adapter);
    }

    @Override
    public void onClick(DataModel dataModel) {
        Intent intent = new Intent(this, FunctionPlan3.class);
        intent.putExtra("model", dataModel);
        startActivity(intent);
    }

    @Override
    public void onClick(String url) {

    }
}


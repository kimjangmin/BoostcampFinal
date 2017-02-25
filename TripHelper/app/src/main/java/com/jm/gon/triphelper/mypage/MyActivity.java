package com.jm.gon.triphelper.mypage;

import android.app.Fragment;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jm.gon.triphelper.FullImageActivity;
import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.functionplan2.TimeLineModel;
import com.jm.gon.triphelper.functionplan3.FunctionPlan3PageAdapter;

import java.io.File;
import java.util.ArrayList;

import quatja.com.vorolay.VoronoiView;

public class MyActivity extends AppCompatActivity {
    private TabLayout tl_MyActivity;
    private ViewPager vp_MyActivity;


    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my);

        tl_MyActivity = (TabLayout) findViewById(R.id.tl_MyActivity);
        tl_MyActivity.addTab(tl_MyActivity.newTab().setText("Recent"));
        tl_MyActivity.addTab(tl_MyActivity.newTab().setText("History"));
        tl_MyActivity.setTabGravity(TabLayout.GRAVITY_FILL);

        vp_MyActivity = (ViewPager)findViewById(R.id.vp_MyActivity);

        MyActivityPageAdapter mypagerAdapter = new MyActivityPageAdapter(getSupportFragmentManager(), tl_MyActivity.getTabCount());
        Log.i("GAG","FunctionPlan3 Page Init tabLayoutcount = "+tl_MyActivity.getTabCount());
        vp_MyActivity.setAdapter(mypagerAdapter);
        vp_MyActivity.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_MyActivity));

        // Set TabSelectedListener
        tl_MyActivity.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_MyActivity.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

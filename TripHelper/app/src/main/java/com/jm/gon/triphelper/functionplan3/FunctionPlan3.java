package com.jm.gon.triphelper.functionplan3;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.functionplan2.TimeLineModel;

public class FunctionPlan3 extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_plan3);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("주변 관광지"));
        tabLayout.addTab(tabLayout.newTab().setText("먹거리"));
        tabLayout.addTab(tabLayout.newTab().setText("숙소"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Intent intent = getIntent();
        TimeLineModel timeLineModel = intent.getParcelableExtra("model");
        Log.i("GAG","FucntionPlan3 start");
        Log.i("TAG","xxxxxxxxxxxxxxxxxxxxxxxxxx");
        Log.i("TAG","timeLineMode x = "+timeLineModel.getMapx());
        Log.i("TAG","xxxxxxxxxxxxxxxxxxxxxxxxxx");


        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        // Creating TabPagerAdapter adapter
        FunctionPlan3PageAdapter pagerAdapter = new FunctionPlan3PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), timeLineModel);
        Log.i("GAG","FunctionPlan3 Page Init tabLayoutcount = "+tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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

package com.jm.gon.triphelper.functionplan3;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.DataModel;

public class FunctionPlan3 extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //안내되어진 정보들중에 선택한 정보에 따라 주변 볼거리 먹거리 숙소를 제공해줍니다.
    //탭레이아웃과 프래그먼트를 이용하여 화면을 구성하였으며 각기능들은 프래그먼트에 있습니다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_function_plan3);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("주변 관광지"));
        tabLayout.addTab(tabLayout.newTab().setText("먹거리"));
        tabLayout.addTab(tabLayout.newTab().setText("숙소"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Intent intent = getIntent();
        DataModel dataModel = intent.getParcelableExtra("model");

        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        // Creating TabPagerAdapter adapter
        FunctionPlan3PageAdapter pagerAdapter = new FunctionPlan3PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), dataModel);
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

package com.jm.gon.triphelper.mypage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.jm.gon.triphelper.functionplan2.TimeLineModel;
import com.jm.gon.triphelper.functionplan3.FunctionPlan3Fragment1;
import com.jm.gon.triphelper.functionplan3.FunctionPlan3Fragment2;
import com.jm.gon.triphelper.functionplan3.FunctionPlan3Fragment3;

/**
 * Created by 김장민 on 2017-02-24.
 */

public class MyActivityPageAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public MyActivityPageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyRecentFragment myRecentFragment = new MyRecentFragment();
                return myRecentFragment;
            case 1:
                MyHistoryFragment myHistoryFragment = new MyHistoryFragment();
                return myHistoryFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
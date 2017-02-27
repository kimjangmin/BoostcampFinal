package com.jm.gon.triphelper.mypage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
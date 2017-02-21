package com.jm.gon.triphelper.functionplan3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class FunctionPlan3PageAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public FunctionPlan3PageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                FunctionPlan3Fragment1 functionPlan3Fragment1 = new FunctionPlan3Fragment1();
                return functionPlan3Fragment1;
            case 1:
                FunctionPlan3Fragment2 functionPlan3Fragment2 = new FunctionPlan3Fragment2();
                return functionPlan3Fragment2;
            case 2:
                FunctionPlan3Fragment3 functionPlan3Fragment3 = new FunctionPlan3Fragment3();
                return functionPlan3Fragment3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
package com.jm.gon.triphelper.functionplan3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jm.gon.triphelper.R;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class FunctionPlan3Fragment1 extends Fragment {
    RecyclerView rv_fragment1;
    Fragment1Adapter fragment1Adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.function_plan3_frgment1, container, false);

        fragment1Adapter = new Fragment1Adapter();
        rv_fragment1 = (RecyclerView)view.findViewById(R.id.rv_fragment1);
        rv_fragment1.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv_fragment1.setLayoutManager(layoutManager);
        rv_fragment1.setAdapter(fragment1Adapter);
        return view;
    }
}

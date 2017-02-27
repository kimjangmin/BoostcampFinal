package com.jm.gon.triphelper.functionplan3;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jm.gon.triphelper.CustomClickListener;
import com.jm.gon.triphelper.DetailActivity;
import com.jm.gon.triphelper.HttpConnectControl;
import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.DataModel;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class FunctionPlan3Fragment2 extends Fragment implements CustomClickListener {
    RecyclerView rv_fragment2;
    Fragment2Adapter fragment2Adapter;
    private DataModel bd_dataModel;
    HttpConnectControl control;
    //먹거리 구성하는 프래그먼트입니다.
    //선택한 곳의 좌표를 기준으로 근방 3km까지 먹거리를 제공합니다.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.function_plan3_frgment2, container, false);

        rv_fragment2 = (RecyclerView)view.findViewById(R.id.rv_fragment2);

        bd_dataModel = getArguments().getParcelable("model");

        control = new HttpConnectControl();
        fragment2Adapter = new Fragment2Adapter(getContext(), this);
        control.startAsync(fragment2Adapter, control.LOCATIONBASE, new String[]{bd_dataModel.getMapx(), bd_dataModel.getMapy()});

        //여기서 동기화 문제가 일어나지 않을까...?
        rv_fragment2.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv_fragment2.setLayoutManager(layoutManager);
        rv_fragment2.setAdapter(fragment2Adapter);

        return view;

    }

    @Override
    public void onClick(DataModel dataModel) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("title", dataModel.getTitle());
        startActivity(intent);
    }

    @Override
    public void onClick(String url) {
    }
}

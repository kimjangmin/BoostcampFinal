package com.jm.gon.triphelper.mypage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jm.gon.triphelper.FullImageActivity;
import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.db.DbHelper;
import com.jm.gon.triphelper.db.DbTable;
import com.jm.gon.triphelper.functionplan2.TimeLineModel;
import com.jm.gon.triphelper.functionplan3.Fragment1Adapter;
import com.jm.gon.triphelper.functionplan3.FunctionPlan3Fragment1;

import java.io.File;
import java.util.ArrayList;

import quatja.com.vorolay.VoronoiView;

/**
 * Created by 김장민 on 2017-02-24.
 */

public class MyRecentFragment  extends Fragment {
    private RecyclerView rv_MyRecentFragment;
    private ArrayList<String> list;
    private SQLiteDatabase sqLiteDatabase;
    private DbHelper helper;
    private Cursor cursor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_recent_fragment, container, false);

        list = new ArrayList<>();
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        VoronoiView voronoiView = (VoronoiView)view.findViewById(R.id.voronoi);
        helper = new DbHelper(getContext());
        sqLiteDatabase = helper.getReadableDatabase();
        cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.PHOTOTABLENAME,
                null,
                null,
                null,
                null,
                null,
                DbTable.AutoCompleteTable._ID+" desc",
                "15");

        while(cursor.moveToNext()){
            View tempview = layoutInflater.inflate(R.layout.activity_my_recent_adapter, null, false);
            voronoiView.addView(tempview);

            ImageView imageView = (ImageView) tempview.findViewById(R.id.image);
            Glide.with(this).load(cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.PHOTOURL))).placeholder(R.drawable.logo).into(imageView);
        }

        voronoiView.setOnRegionClickListener(new VoronoiView.OnRegionClickListener() {
            @Override
            public void onClick(View view, int position) {
                cursor.moveToPosition(position);
                Intent intent = new Intent(getActivity(), FullImageActivity.class);
                intent.putExtra("url", cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.PHOTOURL)));
                startActivity(intent);
            }
        });

        return view;

    }
}

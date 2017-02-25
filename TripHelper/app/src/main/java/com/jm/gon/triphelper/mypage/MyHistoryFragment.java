package com.jm.gon.triphelper.mypage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.jm.gon.triphelper.CustomClickListener;
import com.jm.gon.triphelper.FullImageActivity;
import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.db.DbHelper;
import com.jm.gon.triphelper.db.DbTable;
import com.jm.gon.triphelper.functionplan2.TimeLineModel;

import java.io.File;
import java.util.ArrayList;

import quatja.com.vorolay.VoronoiView;

/**
 * Created by 김장민 on 2017-02-24.
 */

public class MyHistoryFragment extends Fragment implements CustomClickListener{
    private RecyclerView rv_MyHistoryFragment;
    private CallbackManager callbackManager;
    private SQLiteDatabase sqLiteDatabase;
    private DbHelper helper;
    private Cursor cursor;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_history_fragment, container, false);

        rv_MyHistoryFragment = (RecyclerView)view.findViewById(R.id.rv_MyHistoryFragment);
        helper = new DbHelper(getContext());
        sqLiteDatabase = helper.getReadableDatabase();
        cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.PHOTOTABLENAME,
                null,
                null,
                null,
                null,
                null,
                null);
        MyHistoryFramentAdpater adpater = new MyHistoryFramentAdpater(cursor, this, callbackManager);
        rv_MyHistoryFragment.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rv_MyHistoryFragment.addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
            }
        });
        rv_MyHistoryFragment.setAdapter(adpater);
        return view;
    }

    @Override
    public void onClick(TimeLineModel timeLineModel) {

    }

    @Override
    public void onClick(String url) {
        Bitmap image = BitmapFactory.decodeFile(url);

        ShareDialog shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        if (shareDialog.canShow(SharePhotoContent.class)) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            shareDialog.show(content);
        }
    }
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

package com.jm.gon.triphelper.mypage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.jm.gon.triphelper.CustomClickListener;
import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.db.DbTable;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import worldline.com.foldablelayout.FoldableLayout;

/**
 * Created by 김장민 on 2017-02-24.
 */

public class MyHistoryFramentAdpater extends RecyclerView.Adapter<MyHistoryFramentAdpater.Holder>{

    private CustomClickListener customClickListener;
    private Map<Integer, Boolean> isFoldStates = new HashMap<>();
    private CallbackManager callbackManager;
    Context context;
    Cursor cursor;
    public MyHistoryFramentAdpater(Cursor ctx, CustomClickListener customClickListener, CallbackManager callbackManager){
        this.customClickListener = customClickListener;
        cursor = ctx;
        this.callbackManager = callbackManager;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(new FoldableLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        cursor.moveToPosition(position);
//        Log.i("TAG",)
        Glide.with(holder.fl_MyHistoryFragmentAdapter.getContext()).load(cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.PHOTOURL))).into(holder.iv_MyHistoryFragmentAdapter_Cover);
        Glide.with(holder.fl_MyHistoryFragmentAdapter.getContext()).load(cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.PHOTOURL))).into(holder.iv_MyHistoryFragmentAdapter_Detail);
        holder.tv_MyHistoryFragmentAdapter_title.setText(cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.PHOTOCOMMENT)));
        Log.i("LOG","url = "+cursor.getColumnIndex(DbTable.AutoCompleteTable.PHOTOURL));
        Log.i("Log","comment = "+cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.PHOTOCOMMENT)));

        if(isFoldStates.containsKey(position)){
            if(isFoldStates.get(position) == Boolean.TRUE){
                if(!holder.fl_MyHistoryFragmentAdapter.isFolded()){
                    holder.fl_MyHistoryFragmentAdapter.foldWithoutAnimation();
                }
            }else if(isFoldStates.get(position) == Boolean.FALSE){
                if(holder.fl_MyHistoryFragmentAdapter.isFolded()){
                    holder.fl_MyHistoryFragmentAdapter.unfoldWithoutAnimation();
                }
            }
        } else{
            holder.fl_MyHistoryFragmentAdapter.foldWithoutAnimation();
        }
        holder.fl_MyHistoryFragmentAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.fl_MyHistoryFragmentAdapter.isFolded()){
                    holder.fl_MyHistoryFragmentAdapter.unfoldWithoutAnimation();
                } else{
                    holder.fl_MyHistoryFragmentAdapter.foldWithAnimation();
                }
            }
        });
        holder.fl_MyHistoryFragmentAdapter.setFoldListener(new FoldableLayout.FoldListener() {
            @Override
            public void onUnFoldStart() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    holder.fl_MyHistoryFragmentAdapter.setElevation(5);
                }
            }

            @Override
            public void onUnFoldEnd() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    holder.fl_MyHistoryFragmentAdapter.setElevation(0);
                }
                isFoldStates.put(holder.getAdapterPosition(),false);
            }

            @Override
            public void onFoldStart() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    holder.fl_MyHistoryFragmentAdapter.setElevation(5);
                }

            }

            @Override
            public void onFoldEnd() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    holder.fl_MyHistoryFragmentAdapter.setElevation(0);
                }
                isFoldStates.put(holder.getAdapterPosition(),true);

            }
        });

//        holder.bt_MyHistoryFragmentAdapter_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bitmap image = BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.PHOTOURL)));
//
////                ShareDialog shareDialog = new ShareDialog();
//                callbackManager = CallbackManager.Factory.create();
////                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
////                    @Override
////                    public void onSuccess(Sharer.Result result) {
////
////                    }
////
////                    @Override
////                    public void onCancel() {
////
////                    }
////
////                    @Override
////                    public void onError(FacebookException error) {
////
////                    }
////                });
//
////                if (shareDialog.canShow(SharePhotoContent.class)) {
//                    SharePhoto photo = new SharePhoto.Builder()
//                            .setBitmap(image)
//                            .build();
//                    SharePhotoContent content = new SharePhotoContent.Builder()
//                            .addPhoto(photo)
//                            .build();
//                    ShareApi.share(content, null);
////                    shareDialog.show(content);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected FoldableLayout fl_MyHistoryFragmentAdapter;

        @BindView(R.id.iv_MyHistoryFragmentAdapter_Cover)
        protected ImageView iv_MyHistoryFragmentAdapter_Cover;
        @BindView(R.id.iv_MyHistoryFragmentAdapter_Detail)
        protected ImageView iv_MyHistoryFragmentAdapter_Detail;
        @BindView(R.id.tv_MyHistoryFragmentAdapter_title)
        protected TextView tv_MyHistoryFragmentAdapter_title;
        private Button bt_MyHistoryFragmentAdapter_share;
        public Holder(FoldableLayout foldableLayout) {
            super(foldableLayout);
//            iv_MyHistoryFragmentAdapter_Cover = (ImageView)foldableLayout.findViewById(R.id.iv_MyHistoryFragmentAdapter_Cover);
//            iv_MyHistoryFragmentAdapter_Detail = (ImageView)foldableLayout.findViewById(R.id.iv_MyHistoryFragmentAdapter_Detail);
//            tv_MyHistoryFragmentAdapter_title = (TextView)foldableLayout.findViewById(R.id.tv_MyHistoryFragmentAdapter_title);
//            bt_MyHistoryFragmentAdapter_share = (Button)foldableLayout.findViewById(R.id.bt_MyHistoryFragmentAdapter_share);
//            bt_MyHistoryFragmentAdapter_share.setOnClickListener((View.OnClickListener) customClickListener);
            fl_MyHistoryFragmentAdapter = foldableLayout;
            foldableLayout.setupViews(R.layout.activity_my_history_adapter_cover
            ,R.layout.activity_my_history_adapter_detail, R.dimen.card_cover_height, itemView.getContext());
            ButterKnife.bind(this, foldableLayout);
        }
        @Override
        public void onClick(View v) {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                cursor.moveToPosition(getAdapterPosition());
                customClickListener.onClick(cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.PHOTOURL)));
            }
        }
    }
}

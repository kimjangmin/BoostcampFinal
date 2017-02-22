package com.jm.gon.triphelper.functionplan3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jm.gon.triphelper.CustomClickListener;
import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.functionplan2.TimeLineModel;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by 김장민 on 2017-02-22.
 */

public class Fragment3Adapter extends RecyclerView.Adapter<Fragment3Adapter.Fragment3ViewHolder> {

    private List<TimeLineModel> timeLineModelList;
    private int clickposition;
    private CustomClickListener customClickListener;
    Context context;
    public Fragment3Adapter(ArrayList<TimeLineModel> list, Context ctx, CustomClickListener customClickListener){
        timeLineModelList = list;
        Log.i("TAG","Fragment Adapter constructure listcount = "+timeLineModelList.size());
        context = ctx;
        this.customClickListener = customClickListener;
    }
    @Override
    public Fragment3Adapter.Fragment3ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.frunction_plan3_fragment1_adapter, parent, false);
        return new Fragment3Adapter.Fragment3ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Fragment3Adapter.Fragment3ViewHolder holder, int position) {
        Log.i("TAG","onBindViewHolder count = "+position);
        clickposition = position;
        TimeLineModel timeLineModel = timeLineModelList.get(position);
        holder.title.setText(timeLineModel.getTitle());
        holder.addr.setText(timeLineModel.getAddr());
        holder.tel.setText(timeLineModel.getTel());
        Glide.with(context)
                .load(timeLineModel.getUrl())
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.iv);

    }

    @Override
    public int getItemCount() {
        Log.i("TAG","Adapter size = "+timeLineModelList.size());
        return timeLineModelList.size();
    }
    public void update(ArrayList<TimeLineModel> list){
        notifyDataSetChanged();
    }

    public class Fragment3ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView iv;
        TextView title;
        TextView addr;
        TextView tel;
        public Fragment3ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView)itemView.findViewById(R.id.iv_fragment1_image);
            title = (TextView)itemView.findViewById(R.id.tv_fragment1_title);
            addr = (TextView)itemView.findViewById(R.id.tv_fragment1_addr);
            tel = (TextView)itemView.findViewById(R.id.tv_fragment1_tel);
        }
        @Override
        public void onClick(View v) {
            if(getAdapterPosition() != RecyclerView.NO_POSITION){
                customClickListener.onClick(timeLineModelList.get(clickposition));
            }
        }
    }
}
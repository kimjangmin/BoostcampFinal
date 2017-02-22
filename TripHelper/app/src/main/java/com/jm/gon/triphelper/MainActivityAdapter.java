package com.jm.gon.triphelper;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.gon.triphelper.functionplan2.TimeLineAdapter;
import com.jm.gon.triphelper.functionplan2.TimeLineModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김장민 on 2017-02-23.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder>{
    List<TimeLineModel> modelList;
    public MainActivityAdapter(List<TimeLineModel> timeLineModels){
        modelList = timeLineModels;

    }
    @Override
    public MainActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.activity_main_adapter, null);
        return new MainActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainActivityViewHolder holder, int position) {
        holder.title.setText(modelList.get(position).getTel());
        holder.date.setText(modelList.get(position).getStartdate()+" ~ "+modelList.get(position).getEnddate());
        holder.addr.setText(modelList.get(position).getAddr());
    }

    @Override
    public int getItemCount() {
        Log.i("TAG","modelsize = "+modelList.size());
        return modelList.size();
    }

    public class MainActivityViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView title;
        TextView date;
        TextView addr;

        public MainActivityViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView)itemView.findViewById(R.id.iv_MainActivitAdapter);
            title = (TextView)itemView.findViewById(R.id.tv_MainActivityAdapter_title);
            date = (TextView)itemView.findViewById(R.id.tv_MainActivityAdapter_date);
            addr = (TextView)itemView.findViewById(R.id.tv_MainActivityAdapter_addr);

        }
    }
}

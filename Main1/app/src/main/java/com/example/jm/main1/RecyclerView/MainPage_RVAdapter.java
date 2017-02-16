package com.example.jm.main1.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jm.main1.Network.Main_DataTable;
import com.example.jm.main1.R;

import java.util.List;

/**
 * Created by jm on 2017-02-13.
 */

public class MainPage_RVAdapter extends RecyclerView.Adapter<MainPage_RVAdapter.Adaper>{

    List<Main_DataTable> main_dataTables;

    public MainPage_RVAdapter(List<Main_DataTable> dataTables){
        main_dataTables = dataTables;
    }

    public class Adaper extends RecyclerView.ViewHolder{
        TextView date_tv;
        TextView url_tv;
        public Adaper(View view){
            super(view);
            date_tv = (TextView)view.findViewById(R.id.tv_date);
            url_tv = (TextView)view.findViewById(R.id.tv_photourl);
        }
    }

    @Override
    public Adaper onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.mainpage_rvadapter, parent, false);
        return new Adaper(view);
    }

    @Override
    public void onBindViewHolder(Adaper holder, int position) {
        holder.date_tv.setText(main_dataTables.get(position).getDate());
        holder.url_tv.setText(main_dataTables.get(position).getPictureUrl());
    }

    @Override
    public int getItemCount() {
        Log.i("TAG",""+main_dataTables.size());
        return main_dataTables.size();
    }


}

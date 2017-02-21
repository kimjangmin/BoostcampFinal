package com.example.gon.triphelper.functionplan3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gon.triphelper.R;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class Fragment1Adapter extends RecyclerView.Adapter<Fragment1Adapter.Fragment1ViewHolder> {

    public Fragment1Adapter(){

    }
    @Override
    public Fragment1ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.frunction_plan3_fragment1_adapter, parent, false);
        return new Fragment1ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Fragment1ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Fragment1ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView title;
        TextView addr;
        TextView tel;
        public Fragment1ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView)itemView.findViewById(R.id.iv_fragment1_image);
            title = (TextView)itemView.findViewById(R.id.tv_fragment1_title);
            addr = (TextView)itemView.findViewById(R.id.tv_fragment1_addr);
            tel = (TextView)itemView.findViewById(R.id.tv_fragment1_tel);
        }
    }
}

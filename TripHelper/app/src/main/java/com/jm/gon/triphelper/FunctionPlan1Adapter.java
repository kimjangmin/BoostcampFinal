package com.jm.gon.triphelper;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 김장민 on 2017-02-28.
 */

public class FunctionPlan1Adapter extends RecyclerView.Adapter<FunctionPlan1Adapter.Holder>{
    ArrayList<String> arrayList;

    public FunctionPlan1Adapter(){
        arrayList = new ArrayList<>();

    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.activity_function_plan1_adapter, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tv.setText(arrayList.get(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        Log.i("TAG","arraylist size= "+arrayList.size());
        return arrayList.size();
    }
    public void update(ArrayList<String> list){
        arrayList.clear();
        arrayList = (ArrayList<String>) list.clone();
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv;
        public Holder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.tv_FunctionPlan1Adapter);
        }
    }
}

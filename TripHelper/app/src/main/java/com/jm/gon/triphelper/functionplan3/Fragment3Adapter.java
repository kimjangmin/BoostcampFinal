package com.jm.gon.triphelper.functionplan3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jm.gon.triphelper.CustomClickListener;
import com.jm.gon.triphelper.R;
import com.jm.gon.triphelper.DataModel;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by 김장민 on 2017-02-22.
 */

public class Fragment3Adapter extends RecyclerView.Adapter<Fragment3Adapter.Fragment3ViewHolder> {

    private List<DataModel> dataModelList;
    private CustomClickListener customClickListener;
    Context context;
    public Fragment3Adapter(Context ctx, CustomClickListener customClickListener){
        dataModelList = new ArrayList<>();
        Log.i("GAG","Fragment Adapter3 constructure listcount = "+ dataModelList.size());
        context = ctx;
        this.customClickListener = customClickListener;
    }
    @Override
    public Fragment3Adapter.Fragment3ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.frunction_plan3_fragment_adapter, parent, false);
        return new Fragment3Adapter.Fragment3ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Fragment3Adapter.Fragment3ViewHolder holder, int position) {
        DataModel dataModel = dataModelList.get(position);
        holder.title.setText(dataModel.getTitle());
        holder.addr.setText(dataModel.getAddr());
        holder.tel.setText(dataModel.getTel());
        Glide.with(context)
                .load(dataModel.getUrl())
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.noimage)
                .into(holder.iv);

    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }
    public void update(ArrayList<DataModel> list){
        dataModelList.clear();
        for( DataModel model : list){
            if(model.getContenttypeid().equals("32"))
                dataModelList.add(model);
        }
        notifyDataSetChanged();
    }

    public class Fragment3ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout ll;
        ImageView iv;
        TextView title;
        TextView addr;
        TextView tel;
        public Fragment3ViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout)itemView.findViewById(R.id.ll_fragment1_layout);
            iv = (ImageView)itemView.findViewById(R.id.iv_fragment1_image);
            title = (TextView)itemView.findViewById(R.id.tv_fragment1_title);
            addr = (TextView)itemView.findViewById(R.id.tv_fragment1_addr);
            tel = (TextView)itemView.findViewById(R.id.tv_fragment1_tel);
            ll.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(getAdapterPosition() != RecyclerView.NO_POSITION){
                customClickListener.onClick(dataModelList.get(getAdapterPosition()));
            }
        }
    }
}
package com.jm.gon.triphelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class FunctionSearchAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    ArrayList<String> arrayList;
    Context context;

    public FunctionSearchAdapter(Context ctx, ArrayList<String> list){
        arrayList = (ArrayList<String>) list.clone();
        context = ctx;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.function_search_adapter, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_FunctionSearchAdapter);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(arrayList.get(position).toString());
        return convertView;
    }
    public Filter getFilter(){
        return ((FunctionSearch)context).new KeywordFilter();
    }
    public class ViewHolder{
        public TextView tv;
    }
}

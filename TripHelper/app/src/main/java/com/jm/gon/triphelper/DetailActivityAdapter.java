package com.jm.gon.triphelper;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by 김장민 on 2017-02-24.
 */

public class DetailActivityAdapter extends PagerAdapter {

    private String[] imageUrl;
    private LayoutInflater inflater;
    private Context context;

    public DetailActivityAdapter(Context ctx, String[] strurl){
        context = ctx;
        imageUrl = strurl;
    }
    @Override
    public int getCount() {
        return imageUrl.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_detail_adapter, container, false);
        ImageView imageView = (ImageView)v.findViewById(R.id.iv_DetailActivityAdapter);
        TextView textView = (TextView)v.findViewById(R.id.tv_DetailActivityAdapter);
        textView.setText((position+1)+" / "+imageUrl.length);
        Glide.with(context).load(imageUrl[position]).into(imageView);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}

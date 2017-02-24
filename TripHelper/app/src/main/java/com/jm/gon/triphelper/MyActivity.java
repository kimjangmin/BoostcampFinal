package com.jm.gon.triphelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import quatja.com.vorolay.VoronoiView;

public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        LayoutInflater layoutInflater = getLayoutInflater();

        VoronoiView voronoiView = (VoronoiView) findViewById(R.id.voronoi);

        for (int i = 0; i < 15; i++) {
            View view = layoutInflater.inflate(R.layout.activity_my_adapter, null, false);
            voronoiView.addView(view);

            int resourceId = getResources().getIdentifier("image"+((i%10)+1), "drawable", this.getPackageName());

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setImageResource(resourceId);

            final TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText("image "+i);

        }

        voronoiView.setOnRegionClickListener(new VoronoiView.OnRegionClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.v("Region", "Region: " + position);
            }
        });

    }
}

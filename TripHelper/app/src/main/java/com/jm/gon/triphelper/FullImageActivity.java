package com.jm.gon.triphelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import static android.R.attr.duration;

public class FullImageActivity extends AppCompatActivity {

    private KenBurnsView iv_FullImageActivity;

    //mypage에서 앨범에 있는 자신의 사진을 볼수있는 용도입니다.
    //켄번이미지 효과를 적용했습니다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_image);
        iv_FullImageActivity = (KenBurnsView) findViewById(R.id.iv_FullImageActivity);

        Intent intent = getIntent();
        Glide.with(this).load(intent.getStringExtra("url")).into(iv_FullImageActivity);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(3000, new BounceInterpolator());
        iv_FullImageActivity.setTransitionGenerator(generator);
    }
}

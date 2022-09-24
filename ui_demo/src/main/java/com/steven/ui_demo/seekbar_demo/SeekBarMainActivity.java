package com.steven.ui_demo.seekbar_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.steven.ui_demo.R;

public class SeekBarMainActivity extends AppCompatActivity {
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar_main);
        ratingBar = (RatingBar) findViewById(R.id.my_ratingBar);
        // 给星星评分条添加回调事件
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(SeekBarMainActivity.this,"rating: "
                + String.valueOf(rating), Toast.LENGTH_LONG).show();
            }
        });
    }
}
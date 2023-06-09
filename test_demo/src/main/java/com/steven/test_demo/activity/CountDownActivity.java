package com.steven.test_demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.steven.test_demo.R;

public class CountDownActivity extends AppCompatActivity {
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        TextView view = findViewById(R.id.show_countdown);
        int time = 4000;
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished != 0)
                    view.setText("倒计时：" + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                view.setVisibility(View.INVISIBLE);
            }
        };
        timer.start();
    }
}
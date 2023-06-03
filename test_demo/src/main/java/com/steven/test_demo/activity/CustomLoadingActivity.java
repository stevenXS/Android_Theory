package com.steven.test_demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.steven.test_demo.R;
import com.steven.test_demo.custom_view.CustomLoadingView;

import java.util.Timer;
import java.util.TimerTask;

public class CustomLoadingActivity extends AppCompatActivity {
    private CustomLoadingView customLoading;
    private int time = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_loading);
        customLoading = findViewById(R.id.custom_loading);
        Button start = findViewById(R.id.bt_start);
        Button stop = findViewById(R.id.bt_stop);
        TextView show_countdown = findViewById(R.id.show_countdown);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customLoading.start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customLoading.stop();
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        show_countdown.setText("倒计时：" + time);
                        time--;
                        if (time < 0) {
                            timer.cancel();
                            show_countdown.setText("倒计时结束");
                        }
                    }
                });
            }
        },0,1000);
    }
}
package com.steven.test_demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.steven.test_demo.R;
import com.steven.test_demo.custom_view.CustomLoadingView;

public class CustomLoadingActivity extends AppCompatActivity {
    private CustomLoadingView customLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_loading);
        customLoading = findViewById(R.id.custom_loading);
        Button start = findViewById(R.id.bt_start);
        Button stop = findViewById(R.id.bt_stop);
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
    }
}
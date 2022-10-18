package com.steven.broadcast_receiver_demo.case_force_offline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.steven.broadcast_receiver_demo.R;

public class MyMainActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_main);
        Button forceOffline = findViewById(R.id.force_offline);
        forceOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.steven.FORCE_OFFLINE");
                intent.putExtra("a","b");
                sendBroadcast(intent);
            }
        });
    }
}

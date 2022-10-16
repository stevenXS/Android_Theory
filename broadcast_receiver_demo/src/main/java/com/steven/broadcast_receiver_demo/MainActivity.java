package com.steven.broadcast_receiver_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customBroadcastIntent();
    }

    public void customBroadcastIntent(){
        myReceiver = new MyReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.steven.CUSTOM_START");
//        Intent intent = new Intent();
//        intent.setAction("com.steven.CUSTOM_START");

//        registerReceiver(myReceiver, filter);
        registerReceiver(myReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//        sendBroadcast(intent);
    }
}
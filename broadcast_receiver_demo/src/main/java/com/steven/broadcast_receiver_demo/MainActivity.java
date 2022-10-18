package com.steven.broadcast_receiver_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private MyReceiver myReceiver;
    private static String INTENT_FILTER = "android.net.conn.CONNECTIVITY_CHANGE";
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_FILTER);
        registerReceiver(new NetworkMonitorBroadcastReceiver(), intentFilter);
        //        customBroadcastIntent();
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
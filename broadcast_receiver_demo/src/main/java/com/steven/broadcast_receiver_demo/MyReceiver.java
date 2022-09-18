package com.steven.broadcast_receiver_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        String action = intent.getAction();
        Log.d("MyReceiver# ",action);
        Toast.makeText(context, "检测到意图。", Toast.LENGTH_LONG).show();    }
}
package com.steven.notification_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyBroadCastReveiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 解析通知发送的数据
        Bundle extras = intent.getExtras();
        String name = extras.getString("name");
        Toast.makeText(context, "get msg from notify: "+name, Toast.LENGTH_SHORT).show();
        Log.d("Receiver","get msg from notify: "+name);
    }
}
package com.steven.broadcast_receiver_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 接受自定义广播
//        String action = intent.getAction();
//        Log.d("MyReceiver# ",action);
//        Toast.makeText(context, "检测到意图。", Toast.LENGTH_LONG).show();

        // 接受系统广播
        ConnectivityManager connectivityManager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if (network!=null && network.isAvailable()){
            Toast.makeText(context, "检测到网络变化" + network.getTypeName(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, "网络不可达", Toast.LENGTH_LONG).show();
        }
    }
}
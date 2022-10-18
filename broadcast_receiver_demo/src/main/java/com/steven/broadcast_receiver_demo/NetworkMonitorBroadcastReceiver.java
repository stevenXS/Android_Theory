package com.steven.broadcast_receiver_demo;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

public class NetworkMonitorBroadcastReceiver extends BroadcastReceiver {

//    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
            == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "network changed, intent: " + intent.getAction(),
                    Toast.LENGTH_SHORT).show();
        }

    }
}
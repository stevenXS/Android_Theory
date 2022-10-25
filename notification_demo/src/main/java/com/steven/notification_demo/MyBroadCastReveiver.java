package com.steven.notification_demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

public class MyBroadCastReveiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // NOTE：接收通知中setContentIntent(pendingIntent)传递的数据
        Bundle extras = intent.getExtras();
        String name = extras.getString("name");
        Toast.makeText(context, "get msg from notify: "+name, Toast.LENGTH_SHORT).show();

        // NOTE: 接收通知中addAction(action)传递的参数
        // 接收用户通过RemoteInput发送的数据
        Bundle resultsFromIntent = RemoteInput.getResultsFromIntent(intent);
        if (resultsFromIntent!=null){
            String value = resultsFromIntent.getString(NotificationActivity.KEY_REPLY);
            Log.d("Receiver", "get res from remoteInput value: "+ value);
        }
        // 处理完文本后，必须使用相同的 ID 和标记（如果使用）调用 NotificationManagerCompat.notify() 来更新通知。
        // 若要隐藏直接回复界面并向用户确认他们的回复已收到并得到正确处理，则必须完成该操作。
        Notification reply = new Notification.Builder(context, "10")
                .setSmallIcon(com.google.android.material.R.drawable.notification_template_icon_bg)
                .setContentText("reply from notify")
                .build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        // 如果设置同一个通知id则会复用之前的通知界面从而显示一条消息。
        managerCompat.notify(2, reply);
    }
}
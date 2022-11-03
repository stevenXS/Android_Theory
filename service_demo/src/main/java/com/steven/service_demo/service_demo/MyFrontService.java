package com.steven.service_demo.service_demo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.steven.service_demo.R;

/**
 * 基于通知的前台服务
 * IntentService:多线程的服务
 */
public class MyFrontService extends IntentService {
    private static final String TAG = "MyFrontService";

    public MyFrontService() {
        super("MyIntentService"); // 传入HandlerThread的name
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, Thread.currentThread().getName());
        createNotify();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public void createNotify(){
        // 创建通知管理器
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // android26+需要指定通知的渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channel_id = "10";
            String channel_name = "service";
            String channel_desc = "front service";
            // 通知优先级
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, channel_name, importance);
            channel.setDescription(channel_desc);

            // 设置通知的渠道
            notificationManager.createNotificationChannel(channel);

            // 配置通知
            Notification notification = new NotificationCompat.Builder(this, channel_id)
                    .setContentTitle("Notification Title")
                    .setContentText("content: hello world")
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_input_add))
                    .build();

            // 构建通知
            notificationManager.notify(1, notification);
        }
        Log.d(TAG, "create notification");
    }
}

package com.steven.notification_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends AppCompatActivity {
    private MyBroadCastReveiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // register  receiver
        receiver = new MyBroadCastReveiver();
        registerReceiver(receiver, new IntentFilter("notify_action"));

        Button button = findViewById(R.id.btn_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtainService(NotificationActivity.this);
            }
        });
    }

    /**
     * 通知的基本用法
     * @param context：上下文对象
     */
    public void obtainService(Context context){
        // 创建通知管理器
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // android26+需要指定通知的渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channel_id = "10";
            String channel_name = getString(R.string.channel_name);
            String channel_desc = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, channel_name, importance);
            channel.setDescription(channel_desc);
            // 设置渠道
            notificationManager.createNotificationChannel(channel);
            // 通知设置action通过PendingIntent调用广播
            Intent intent = new Intent(this, MyBroadCastReveiver.class);
            intent.setAction("notify_action"); // 自定义一条广播
            Bundle bundle = new Bundle();
            bundle.putString("name","steven");
            intent.putExtras(bundle);
            // Android31+需要加上标志位PendingIntent.FLAG_MUTABLE
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
            // 配置通知
            Notification builder = new NotificationCompat.Builder(context, channel_id)
                    .setContentTitle("Notification Title")
                    .setContentText("content: hello world")
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .addAction(R.drawable.ic_launcher_background,
                            getString(R.string.send_intent), pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                    .build();
            notificationManager.notify(100, builder);
        }
    }
}
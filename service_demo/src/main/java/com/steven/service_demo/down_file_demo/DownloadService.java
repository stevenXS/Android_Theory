package com.steven.service_demo.down_file_demo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.steven.service_demo.R;

import java.io.File;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";
    private static final int notify_id = 1;
    private String downloadUrl;
    private DownloadTask downloadTask;
    //处理回调
    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            // 任务下载成功后关闭前台服务通知，并创建一个下载成功的通知
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Success.", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            // 任务下载失败后关闭前台服务通知，并创建一个下载失败的通知
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Failed.", -1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Download Paused", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Download Canceled", Toast.LENGTH_LONG).show();
        }
    };

    private Notification getNotification(String title, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, DownloadActivity.class);
        /**
         * FLAG_IMMUTABLE：表示PendingIntent中的Intent不能被将Intent传递给PendingIntent.send()的其他应用程序修改。应用总是可以使用FLAG_UPDATE_CURRENT来修改它自己的PendingIntent
         * 在Android 12之前，默认情况下，不带此标志创建的PendingIntent是可变的。
         * 在Android 6 (API 23) 之前的Android版本上，PendingIntent总是可变的。
         *
         * FLAG_MUTABLE：表示PendingIntent中的Intent应允许应用程序通过合并PendingIntent.send()的Intent参数值来更新其内容。
         * 始终填写任何可变的PendingIntent的包装Intent的ComponentName。不这样做可能会导致安全漏洞！
         * 此标志是在Android 12中添加的。在Android 12之前，在没有FLAG_IMMUTABLE标志的情况下创建的任何PendingIntent都是隐式可变的。
         */
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setSmallIcon(android.R.drawable.presence_video_away)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_input_add))
                .setContentIntent(pendingIntent)
                .setChannelId("10")
                .setContentTitle(title);
        if (progress > 0){
            // 当progress>0显示下载进度
            builder.setContentText(progress + "%")
                    .setProgress(100, progress,true);
        }
        return builder.build();
    }

    private NotificationManager getNotificationManager() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // 26+需要指定通知渠道
            String channel_id = "1";
            String channel_name = "download";
            int channel_importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel(channel_id, channel_name, channel_importance);
        }
        notificationManager.createNotificationChannel(channel);
        return notificationManager;
    }

    /**
     * Binder
     */
    private DownloadBinder mBinder = new DownloadBinder();
    class DownloadBinder extends Binder{
        public void startDownload(String url){
            if(downloadUrl == null){
                downloadUrl = url;
                downloadTask = new DownloadTask(downloadListener);
                downloadTask.execute(downloadUrl);
                startForeground(notify_id, getNotification("Downloading...", 0));
                Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_LONG).show();
            }
        }

        public void pauseDownload(){
            if(downloadTask != null){
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload(){
            if(downloadTask != null){
                downloadTask.cancelDownload();
            }else {
                if (downloadUrl != null){
                    // 取消下载文件时关闭通知，并删除文件
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    Log.d(TAG,filePath + fileName);
                    File file = new File(filePath + fileName);
                    if (file.exists())
                        file.delete();
                    getNotificationManager().cancel(notify_id);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this,"Canceled Download", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (flags == BIND_AUTO_CREATE)
            Log.d(TAG,"call bindService for " + intent.getClass().getName());
        Log.d(TAG,"call startService for " + intent.getAction());
        return super.onStartCommand(intent, flags, startId);
    }
}

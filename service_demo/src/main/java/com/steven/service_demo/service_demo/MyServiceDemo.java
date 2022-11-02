package com.steven.service_demo.service_demo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyServiceDemo extends Service {
    private static final String TAG = "MyServiceDemo";
    public MyServiceDemo() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * 利用Binder通信
     */
    private DownLoadBinder downLoadBinder = new DownLoadBinder();

    class DownLoadBinder extends Binder{
        public void startDownload(){
            Log.d(TAG, "startDownload");
        }

        public void finishDownload(){
            Log.d(TAG, "finishDownload");
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return downLoadBinder;
    }
}
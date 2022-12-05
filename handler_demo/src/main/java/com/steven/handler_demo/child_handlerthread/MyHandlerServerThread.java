package com.steven.handler_demo.child_handlerthread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyHandlerServerThread extends Thread{
    public Handler getHandler() {
        return mHandler;
    }

    private Handler mHandler;
    private static final String TAG = "MyHandlerThread";
    public static final int MSG_DATA = 2;

    @Override
    public void run() {
        // 创建Looper对象
        Looper.prepare();
        // create handler
        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_DATA:
                        Bundle bundle = (Bundle)msg.obj;
                        Log.d(TAG, Thread.currentThread().getName()+","+ bundle.getString("data"));
                        break;
                    default:
                        break;
                }
            }
        };

        // monitor looper's message queue
        Looper.loop();
    }
}

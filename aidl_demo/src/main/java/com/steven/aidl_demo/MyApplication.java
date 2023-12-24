package com.steven.aidl_demo;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.looper_free.LooperMessageObserver;
import com.example.looper_free.LooperObserverUtil;
import com.steven.aidl_demo.optUtil.MainThreadOptUtil;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MainThreadOptUtil.TAG, "Application# onCreate");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(MainThreadOptUtil.TAG, "Application# attachBaseContext");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final MainThreadOptUtil mainThreadOptUtil = new MainThreadOptUtil();
                LooperObserverUtil.setObserver(new LooperMessageObserver() {
                    @Override
                    public Object messageDispatchStarting(Object token) {
                        return token;
                    }

                    @Override
                    public void messageDispatched(Object token, Message msg) {
                        mainThreadOptUtil.upgradeMessagePriority(mainThreadOptUtil.getMh(), mainThreadOptUtil.getMhHandlerMessageQueue());
                    }

                    @Override
                    public void dispatchingThrewException(Object token, Message msg, Exception exception) {

                    }
                });
               }
        }).start();
    }
}

package com.steven.handler_demo.refresh_image_byhandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

public class HandlerChildThread extends Thread {
    private Message msg;
    private Bundle bundle;
    private Handler mHandler;

    public HandlerChildThread(Handler Handler) {
        this.mHandler = Handler;
        new HandlerChildThread2(new ChildHandler(Looper.myLooper())).start(); // 开启子线程2
    }

    @Override
    public void run() {
//        Looper.prepare();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // 给主线程发送消息
                msg = new Message();
                msg.what = 0x123;
                bundle = new Bundle();
                bundle.putString("msg","hello, I'm your child thread.");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        },0,10000);
    }

    // 创建一个Handler来处理线程2的消息
    class ChildHandler extends Handler{
        public ChildHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0x00:
                    boolean res = Looper.myLooper() == Looper.getMainLooper();
                    String msg1 = msg.getData().getString("msg");
                    Log.d("ChildThread# ","收到其他线程的消息: "+msg1 + String.valueOf(res));
                    break;
            }
        }
    }
}

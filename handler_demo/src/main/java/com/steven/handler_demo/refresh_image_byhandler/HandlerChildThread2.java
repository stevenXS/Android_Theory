package com.steven.handler_demo.refresh_image_byhandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class HandlerChildThread2 extends Thread {
    private Message msg;
    private Bundle bundle;
    private Handler mHandler;

    public HandlerChildThread2(Handler Handler) {
        this.mHandler = Handler;

    }

    @Override
    public void run() {
//        Looper.prepare();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                boolean res = Looper.myLooper() == Looper.getMainLooper();
                Log.d("ChildTread-2#",String.valueOf(res));
                msg = new Message();
                msg.what = 0x00;
                bundle = new Bundle();
                bundle.putString("msg","hello, I'm your child thread---2.");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        },0,5000);
    }
}

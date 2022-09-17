package com.steven.handler_demo.refresh_image_byhandler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.steven.handler_demo.MainActivity;
import com.steven.handler_demo.R;

import java.util.Timer;
import java.util.TimerTask;

public class HandlerMainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Message msg;
    private Bundle bundle;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.img_view);

        // 主线程定时给子线程发送消息
        // 开启子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Looper.myLooper(): 表示的是当前线程维护的Looper对象；
                // Looper.getMainLooper()：表示的是整个App维护的Looper对象，仅有一个；
                mHandler = new HandlerImage(Looper.getMainLooper());

                // set timer that with 200ms period.
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        msg = new Message();
                        bundle = new Bundle();
                        msg.what = 0x123;
                        bundle.putInt("iv",123);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                },0,200);
            }
        }).start();


    }
}

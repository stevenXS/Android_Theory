package com.steven.service_demo.handler_demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.steven.service_demo.R;

public class HandlerActivityDemo extends AppCompatActivity {
    public static final int UPDATE = 1;
    private TextView show;
    private Handler mMyhandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE:
                    show.setText("get msg from handler");
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button send = findViewById(R.id.btn_update);
        show = findViewById(R.id.text_show);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        Looper.prepare(); //为子线程创建Looper
//                        Log.d("looper", Looper.myLooper().getThread().getName()+","
//                                    +Looper.getMainLooper().getThread().getName());
                        // 子线程发送消息；主线程处理消息并更新UI
                        Message message = new Message();
                        message.what = UPDATE;
                        mMyhandler.sendMessage(message);
                    }
                }).start();
            }
        });
    }
}

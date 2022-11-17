package com.steven.handler_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 通过当前Thread的ThreadLocal对象创建一个ThreadLocalMap,并在Map中添加Looper对象，
                // 保证了每个线程拥有自己的Looper对象。
                Looper.prepare();
                mHandler = new Handler(Looper.myLooper()){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        switch (msg.what){
                            case 123:
                                boolean res = Looper.myLooper() == Looper.getMainLooper();
                                Log.d("Handler", Thread.currentThread().getName()+" " + String.valueOf(res));
                                break;
                        }
                    }
                };
                // 1.loop()方法先从Thread对象取出对应的Looper；
                // 2.然后监听Looper对象自己的消息队列。
                Looper.loop();
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Handler", Thread.currentThread().getName());
                Message message = mHandler.obtainMessage();
                message.what = 123;
                mHandler.sendMessage(message);
            }
        }).start();
    }

}
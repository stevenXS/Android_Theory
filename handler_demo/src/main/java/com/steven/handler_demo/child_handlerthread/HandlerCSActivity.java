package com.steven.handler_demo.child_handlerthread;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.steven.handler_demo.R;

public class HandlerCSActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyHandlerServerThread serverThread = new MyHandlerServerThread();
        serverThread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MyHandlerClientThread clientThread = new MyHandlerClientThread(serverThread.getHandler());
        clientThread.start();
    }
}

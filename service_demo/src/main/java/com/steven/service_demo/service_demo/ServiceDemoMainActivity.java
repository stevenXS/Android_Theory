package com.steven.service_demo.service_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.steven.service_demo.R;

public class ServiceDemoMainActivity extends AppCompatActivity {

    private MyServiceDemo.DownLoadBinder downLoadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downLoadBinder = (MyServiceDemo.DownLoadBinder) service;
            downLoadBinder.startDownload();
            downLoadBinder.finishDownload();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("connection", name.getShortClassName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = findViewById(R.id.start_service);
        Button stop = findViewById(R.id.stop_service);
        Button bind = findViewById(R.id.bind_service);
        Button unbind = findViewById(R.id.unbind_service);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceDemoMainActivity.this, MyServiceDemo.class);
                startService(intent);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceDemoMainActivity.this, MyServiceDemo.class);
                stopService(intent);
            }
        });

        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceDemoMainActivity.this, MyServiceDemo.class);
                bindService(intent, connection, BIND_AUTO_CREATE);
            }
        });

        unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(connection);
            }
        });
    }
}
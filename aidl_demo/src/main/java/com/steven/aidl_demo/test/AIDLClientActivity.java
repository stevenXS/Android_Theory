package com.steven.aidl_demo.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.steven.aidl_demo.R;

public class AIDLClientActivity extends AppCompatActivity {
    public static final String TAG = "aidl# ";
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AIDLClientActivity.this, AIDLTestService.class);
                intent.setPackage("com.steven.aidl");
                Log.d(AIDLClientActivity.TAG, "bindService: start ");
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
                Log.d(AIDLClientActivity.TAG, "bindService: end ");
            }
        });
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IAIDLService iaidlService = IAIDLService.Stub.asInterface(service);
            try {
                Log.d(AIDLClientActivity.TAG, "onServiceConnected: register callback");
                iaidlService.psotMessage(new AIDLTestCallback());
                Log.d(AIDLClientActivity.TAG, "onServiceConnected: post msg");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(AIDLClientActivity.TAG, "onServiceDisconnected: " + name);
        }
    };
}

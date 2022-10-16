package com.steven.handler_demo.refresh_image_byhandler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 当前线程向子线程传递的Looper就不等价于主线程的Looper
        new HandlerChildThread(new MyHandler(Looper.myLooper())).start();
    }

    class MyHandler extends Handler{
        public MyHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0x123:
                    String value1 = msg.getData().getString("msg");
                    Toast.makeText(HandlerMainActivity.this,value1,Toast.LENGTH_SHORT).show();
//                    Log.d("主线程 msg#",value1);
                    break;
                case 0x00:
                    String value2 = msg.getData().getString("msg");
                    Log.d("主线程 msg#",value2);
                    break;
            }
        }
    }
}

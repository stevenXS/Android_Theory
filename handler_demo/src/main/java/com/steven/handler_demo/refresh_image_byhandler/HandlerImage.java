package com.steven.handler_demo.refresh_image_byhandler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.steven.handler_demo.R;

public class HandlerImage extends Handler{
    public HandlerImage(@NonNull Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            // 从Looper对象的MessageQueue中去取对应的Message对象
            case 0x123:
                int value = msg.getData().getInt("iv");
                Log.d("iv#",String.valueOf(value));
                break;
        }
    }
}

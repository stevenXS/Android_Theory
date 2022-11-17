package com.steven.handler_demo.child_handlerthread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyHandlerClientThread extends Thread{
    private Handler handler;

    public MyHandlerClientThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        // 向ServerThread的Handler发送消息
        if (handler != null){
            Message message = handler.obtainMessage();
            message.what = MyHandlerServerThread.MSG_DATA;
            Bundle bundle = new Bundle();
            bundle.putString("data","hello!");
            message.obj = bundle;
            handler.sendMessage(message);
        }else {
            throw new RuntimeException("handler is null");
        }
    }
}

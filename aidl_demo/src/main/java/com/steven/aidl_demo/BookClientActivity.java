package com.steven.aidl_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class BookClientActivity extends AppCompatActivity {
    private static final String TAG = "BookClientActivity";
    private IBookManager mRemoteManager;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, BookManagerService.class);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        });
    }


    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

    private ServiceConnection connection = new ServiceConnection() {
        // 当Service调用onBind()后会回调onServiceConnected()方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager manager = IBookManager.Stub.asInterface(service); // 同一个进程则返回Stub本身，跨进程则返回Stub.Proxy
            mRemoteManager = manager; // 这里调用服务端Service的实现类

            try {
//                List<Book> bookList = manager.getBookList();
//                for (Book book : bookList) {
//                    Log.d(TAG, book.getName());
//                }
                Log.d(TAG, "onServiceConnected");
                manager.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteManager = null;
            Log.d(TAG,"service offline.");
        }
    };

    /**
     * 实现IOnBookGetListener接口，服务端回调时的实现走以下逻辑
     */
    private IOnBookGetListener listener = new IOnBookGetListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            // 利用handler处理消息
            mHandler.obtainMessage(123, book).sendToTarget();
        }
    };

    /**
     * handler处理消息
     */
    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 123:
                    Book book = (Book) msg.obj;
                    Log.d(TAG,"get new Book from service id# " + book.getName() + ", " + Thread.currentThread().getName());
                    break;
                default:
                    break;
            }
        }
    };
}
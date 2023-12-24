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
import android.os.MessageQueue;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.steven.aidl_demo.optUtil.MainThreadOptUtil;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookClientActivity extends AppCompatActivity {
    private static final String TAG = "BookClientActivity# ";
    private IBookManager mRemoteManager;
    private Button btn;
    private CountDownLatch latch = new CountDownLatch(1);
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "msg is " + msg.obj + ", " + msg.getTarget());
            super.handleMessage(msg);
        }
    };
    private long start = 0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                service.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        long start = System.currentTimeMillis();
//                        if(bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
//                            try {
//                                latch.await();
//                                Log.d(TAG, Thread.currentThread().getName() +", priority: " +Thread.currentThread().getPriority() + " bindService success, countDown()");
//                                Log.d(TAG, Thread.currentThread().getName() + ", priority: " +Thread.currentThread().getPriority() + " callback success");
//                                Log.d(TAG, Thread.currentThread().getName() +", cost time: " + (System.currentTimeMillis() - start));
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        MessageQueue queue = Looper.getMainLooper().getQueue();
//
//                    }
//                });
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, BookManagerService.class);
        intent.setAction("steven");
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            Log.d(TAG, "main thread done");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                if(bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
                    try {
                        start = System.currentTimeMillis();
                        latch.await();
                        Log.d(TAG, Thread.currentThread().getName() + ", priority: " +Thread.currentThread().getPriority() + " callback success");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

    /**
     * 1. 创建ServiceConnection，然后Service回调onServiceConnected传递服务端的IBookManager实现类的引用；
     * 2. 用该引用注入客户端监听器
     * 3. 服务端后台轮训发现有监听者即刻回调对应方法；
     * 4. 客户端监听者在回调中消费服务端给的数据
     */
    private final ServiceConnection connection = new ServiceConnection() {
        // 当Service调用onBind()后会回调onServiceConnected()方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                Log.d(TAG, Thread.currentThread().getName() +",###### cost time: " + (System.currentTimeMillis() - start));
                Log.d(TAG, "onServiceConnected");
                IBookManager manager = IBookManager.Stub.asInterface(service); // 同一个进程则返回Stub本身，跨进程则返回Stub.Proxy
                mRemoteManager = manager; // 这里调用服务端Service的实现类
                List<Book> bookList = manager.getBookList();
                for (Book book : bookList) {
                    Log.d(TAG, book.getName());
                }
                manager.registerListener(listener);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Log.d(TAG, Thread.currentThread().getName() + ", priority: " +Thread.currentThread().getPriority() + " onServiceConnected, start callback.");
                latch.countDown();
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
    private final IOnBookGetListener listener = new IOnBookGetListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            // 利用handler处理消息
            mHandler.obtainMessage(123, book).sendToTarget();
        }
    };

    /**
     * handler处理消息
     */
    private final Handler mHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 123:
                    Book book = (Book) msg.obj;
//                    Log.d(TAG,"get new Book from service id# " + book.getName() + ", " + Thread.currentThread().getName());
                    break;
                default:
//                    Log.d(TAG,"service msg " + msg.toString());
                    break;
            }
        }
    };
}
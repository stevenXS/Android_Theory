package com.steven.test_demo;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.steven.Utils.CameraDemo;
import com.steven.Utils.thread.PriorityTask;
import com.steven.Utils.Result;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
//        ExecutorService service = Executors.newSingleThreadExecutor();
//        Future<String> future = service.submit(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return getMsg();
//            }
//        });
//        try {
//            Log.d("msg", future.get());
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.d("msg", getMsg());
        execute();
    }

    public String getMsg() {
        CameraDemo cameraDemo = new CameraDemo();
        return cameraDemo.start(new Result() {
            @Override
            public String onResult(String msg) {
                return msg;
            }
        });
    }

    @Test
    public void execute() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 20, 5L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
        for (int i = 0; i < 10; i++) {
            PriorityTask priorityTask = new PriorityTask("task" + i, i);
            executor.execute(priorityTask);
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 10; i < 20; i++) {
            PriorityTask priorityTask = new PriorityTask("task" + i, i);
            executor.execute(priorityTask);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
    @Test
    public void testFuture() {
        Future<String> future = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                OaidInit();
                return "aaa";
            }
        });
        long start = System.currentTimeMillis();
        try {
            future.get(500, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.d("aaa", "cost time " + (System.currentTimeMillis() - start));
        }
    }

    CountDownLatch latch = new CountDownLatch(1);
    public String getOaid() {
        long l = SystemClock.elapsedRealtime();
        if (OaidInit()) {
            try {
                latch.await();
                Log.d("aaa", "get init msg");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Log.d("aaa", "getOaid cost time Realtime" + (SystemClock.elapsedRealtime() - l));
            }
        }
        return "aaa";
    }

    private boolean OaidInit() {
        Handler handler = new Handler();
        long l = SystemClock.elapsedRealtime();
        try {
            Log.d("aaa", "oaid init");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("aaa", "post msg");
                }
            }, 600);
        } finally {
            Log.d("aaa", "getOaid cost time Realtime" + (SystemClock.elapsedRealtime() - l));
        }
        return true;
    }

}
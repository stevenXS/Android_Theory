package com.steven.Utils;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    public static void testFuture() {
        ExecutorService service = Executors.newFixedThreadPool(2);
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
            Log.e("aaa", "e " + e.getMessage());
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

    private static boolean OaidInit() {
        long l = SystemClock.elapsedRealtime();
        try {
            Log.d("aaa", "oaid init");
            TimeUnit.SECONDS.sleep(1);
            Log.d("aaa", "oaid done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Log.d("aaa", "getOaid cost time Realtime" + (SystemClock.elapsedRealtime() - l));
        }
        return true;
    }
}

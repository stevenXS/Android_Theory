package com.steven.performance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.steven.test_demo.MainActivity;
import com.steven.test_demo.activity.SearchActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends MainActivity {
    private Handler handler = new Handler(Looper.getMainLooper());
    private static final String TAG = "Splash# ";
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 主线程发送一个延迟消息，模拟耗时任务
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Log.e(TAG, "任务处理3000ms");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Intent intent = new Intent(SplashActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "SplashActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "SplashActivity onStop");
    }
}

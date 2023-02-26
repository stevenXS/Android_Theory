package com.steven.test_demo;

import android.app.Application;
import android.os.Process;
import android.util.Log;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        int pid = Process.myPid();
        String processName = getProcessName();
        Log.d("MyApp", processName + ", " + pid);

    }


}

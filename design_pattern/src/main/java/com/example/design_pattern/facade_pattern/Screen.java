package com.example.design_pattern.facade_pattern;

import android.util.Log;

public class Screen {
    public static Screen getInstance() {
        return new Screen();
    }

    public void on() {
        Log.d("aaa#", "打开屏幕");
    }

    public void off() {
        Log.d("aaa#", "关闭屏幕");
    }
}

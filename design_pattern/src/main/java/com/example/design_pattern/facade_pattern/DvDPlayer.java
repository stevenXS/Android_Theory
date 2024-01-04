package com.example.design_pattern.facade_pattern;

import android.util.Log;

public class DvDPlayer {
    public static DvDPlayer getInstance() {
        return new DvDPlayer();
    }

    public void on() {
        Log.d("aaa#", "打开遥控器");
    }

    public void off() {
        Log.d("aaa#", "关闭遥控器");
    }
}

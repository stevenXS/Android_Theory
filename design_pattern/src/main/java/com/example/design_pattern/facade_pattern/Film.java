package com.example.design_pattern.facade_pattern;

import android.util.Log;

public class Film {
    public static Film getInstance() {
        return new Film();
    }

    public void on() {
        Log.d("aaa#", "打开电影");
    }

    public void off() {
        Log.d("aaa#", "关闭电影");
    }
}

package com.steven.Utils;

import android.util.Log;
import android.util.Size;

public class CameraDemo {
    private Size size = null;

    public void setUpSize() {
        Log.d("a", String.valueOf(size.getHeight()));
    }

    public void initSize() {
        size = new Size(20,30);
    }

    public String start(Result result) {
        return result.onResult("test123");
    }
}

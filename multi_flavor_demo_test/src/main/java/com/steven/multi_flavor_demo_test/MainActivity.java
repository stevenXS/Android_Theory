package com.steven.multi_flavor_demo_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private success s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        s = new success() {
            @Override
            public void ok() {
                Log.d("test","aaa");
            }
        };
        addListener(s);
    }


    interface success{
        void ok();
    }

    public void addListener(success listener){
        listener.ok();
    }
}
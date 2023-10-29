package com.example.design_pattern.mvp.view.activity;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
}

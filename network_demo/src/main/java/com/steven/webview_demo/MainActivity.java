package com.steven.webview_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * WebView基本用法：
 * 需要权限：android.permission.INTERNET
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView web_view = findViewById(R.id.web_view);
        web_view.getSettings().setJavaScriptEnabled(true);
        // 添加WebView的客户端：目的是在跳转多个网页时仍然显示在WebView视图中；
        // 而不是显示到系统浏览器。
        web_view.setWebViewClient(new WebViewClient());
        // 加载URL
        web_view.loadUrl("https://www.baidu.com");
    }
}
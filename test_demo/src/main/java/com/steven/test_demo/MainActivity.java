package com.steven.test_demo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton("webview_activity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用JS对应的函数
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        addButton("remoteActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemoteAMainActivity.class);
                startActivity(intent);
            }
        });

        addButton("remoteBctivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemoteBMainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addButton(String btnName, View.OnClickListener listener){
        LinearLayout rootView = (LinearLayout) findViewById(R.id.ll_root);
        Button button = new Button(this);
        button.setText(btnName);
        button.setTag(btnName);

        button.setOnClickListener(listener);

        if (rootView != null){
            rootView.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
package com.steven.test_demo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @SuppressLint({"MissingInflatedId", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        // 设置与js交互的权限
        settings.setJavaScriptEnabled(true);
        // 允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 载入JS代码, 固定格式
        webView.loadUrl("file:///android_asset/javascript.html");
//        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
//        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
//        // 通过设置WebChromeClient对象处理JavaScript的对话框
//        // 设置响应js 的Alert()函数
//        // ps: JS代码调用一定要在 onPageFinished() 回调之后才能调用，否则不会调用
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                AlertDialog.Builder b = new AlertDialog.Builder(WebViewActivity.this);
//                b.setTitle("Alert");
//                b.setMessage(message);
//                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.confirm();
//                    }
//                });
//                b.setCancelable(false);
//                b.create().show();
//                return true;
//            }
//        });

        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("JSToJAVA", value);
                    }
                });
            }
        });
    }
}
package com.steven.webview_demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpClientActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp3_main);
        Button send_req = findViewById(R.id.send_request);
        tv  = findViewById(R.id.response_text);
        send_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.send_request){
                    sendRequestByOkHttp();
                }
            }
        });
    }

    private void sendRequestByOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://www.baidu.com")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String res = response.body().string();
                    parseXMLWithPull(res);
                    showRes(res);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析XML格式的数据
     * @param res: 来自body的数据
     */
    private void parseXMLWithPull(String res) throws XmlPullParserException, IOException {
        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = parserFactory.newPullParser();
        parser.setInput(new StringReader(res));
        int eventType = parser.getEventType();
        String id = "";
        String name = "";
        String version = "";
        while (eventType != XmlPullParser.END_DOCUMENT){
            String nodeName = parser.getName();
            switch (eventType){
                // 开始解析某个节点
                case XmlPullParser.START_TAG:
                    if ("id".equals(nodeName)){
                        id = parser.nextText();
                    }
                    break;
                case  XmlPullParser.END_TAG:
                    if ("app".equals(nodeName)){
                        Log.d("OkhttpActivity", "id is " + id);
                        Log.d("OkhttpActivity", "name is " + name);
                        Log.d("OkhttpActivity", "version is " + version);
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    private void showRes(String res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(res);
            }
        });
    }
}

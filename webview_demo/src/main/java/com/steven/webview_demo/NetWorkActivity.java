package com.steven.webview_demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;


import javax.net.ssl.HttpsURLConnection;

public class NetWorkActivity extends AppCompatActivity {
    TextView response;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_main);
        response = findViewById(R.id.response_text);
        Button send = findViewById(R.id.send_request);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.send_request){
                    sendRequestByHttpURLConn();
                }
            }
        });
    }

    private void sendRequestByHttpURLConn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    // 绑定IP+PORT(域名)
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpsURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
//                    // 向服务器写入数据
//                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
//                    outputStream.writeBytes("username=admin&password=123456");
                    // 读取socket中返回的数据
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null){
                        sb.append(line);
                    }
                    showResponse(sb.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection !=null){
                        connection.disconnect();
                    }
                }
            }
        },"httpURL").start();
    }

    private void showResponse(String res) {
        // 在主线程中显示结果
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                response.setText(res);
            }
        });
    }
}

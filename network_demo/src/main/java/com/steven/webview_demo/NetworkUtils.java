package com.steven.webview_demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络连接的工具类
 *  使用多线程+回调接口（异步非阻塞）来解决线程没有返回值的问题。
 */
public class NetworkUtils {

    /**
     * 回调接口
     */
    public interface HttpCallback{
        String onFinsh(String response);
        String onError(Exception e);
    }

    /**
     * 原生Http实现请求
     */
    public static void sendRequestByHttpURLConn(String urlString, HttpCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    // 绑定IP+PORT(域名)
                    URL url = new URL(urlString);
                    connection = (HttpsURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    // 读取socket中返回的数据
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null){
                        sb.append(line);
                    }
                    // 异步（多线程）+ 非阻塞（有返回结果）的体现
                    if (callback != null){
                        callback.onFinsh(sb.toString());
                    }
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


    /**
     * okHttp实现请求
     */
    public static void sendRequestByOkHttp(String address, okhttp3.Callback callback) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(new URL(address))
                .build();
       client.newCall(request).enqueue(callback);
    }
}

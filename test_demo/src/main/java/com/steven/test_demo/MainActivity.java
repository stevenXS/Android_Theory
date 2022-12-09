package com.steven.test_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Network;
import android.os.Bundle;
import android.util.Log;

import com.steven.test_demo.ipv6_adress.Test;

import java.io.UnsupportedEncodingException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    private Object object = new Object();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object){
                    try {
                        Log.d("aaa","aaaaaaaaa");
                        object.wait(5000);
                        Log.d("aaa","ccccccccc");

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        try {
            Thread.sleep(200);
        }catch (Exception e){
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object){
                    try {
                        Log.d("bbb","bbb");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

}
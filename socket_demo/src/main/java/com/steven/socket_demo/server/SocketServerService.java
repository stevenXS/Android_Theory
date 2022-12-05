package com.steven.socket_demo.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerService extends Service {
    private boolean isStop = false;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TCPServer()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class TCPServer implements Runnable{

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                // 绑定8080端口
                serverSocket = new ServerSocket(8081);
            }catch (IOException e){
                e.printStackTrace();
            }
            // 轮询接受客户端消息
            while (!isStop){
                try {
                    Log.d("Server", "start listening");
                    Socket client = serverSocket.accept();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void responseClient(Socket client) throws IOException {
        // receive message from client
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // send message to client
        PrintWriter  printWriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        printWriter.println("hello, i'm server!");
        while (!isStop){
            String s = reader.readLine();
            Log.d("room", "get message from client: "+s);
            // 从服务端回传消息给客户端
            printWriter.println(s);
        }
        printWriter.close();
        reader.close();
    }

    @Override
    public void onDestroy() {
        isStop = true;
        super.onDestroy();
    }
}
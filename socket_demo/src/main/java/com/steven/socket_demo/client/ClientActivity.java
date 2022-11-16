package com.steven.socket_demo.client;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.steven.socket_demo.R;
import com.steven.socket_demo.server.SocketServerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientActivity extends AppCompatActivity {
    private Socket mClientSocket;
    private Button btn_send;
    private PrintWriter pw;
    private TextView tv_message;
    private EditText et_receive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Intent intent = new Intent(this, SocketServerService.class);
        startService(intent);
        new Thread(){
            @Override
            public void run() {
                connectServer();
            }
        }.start();
    }

    private void connectServer() {
        Socket socket = null;
        while (socket==null){
            try {
                // 这个错误是因为模拟器默认把127.0.0.1和localhost当做本身，那么我们可以在模拟器上用10.0.2.2代替127.0.0.1和localhost，
                // 另外如果是在局域网环境可以用 192.168.0.x或者192.168.1.x(根据具体配置)连接本机,这样也不会报错
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.0.113",8081));

                // send msg to server
                pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!isFinishing()){
                // activity is alive.
                String s = reader.readLine();
                if (s!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // update textview on ui Thread.
                            tv_message.setText(tv_message.getText() + "\n" +"服务端:"+s);
                        }
                    });
                }
            }
            pw.close();
            reader.close();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initView() {
        et_receive = findViewById(R.id.et_receive);
        btn_send= (Button) findViewById(R.id.btn_send);
        tv_message= (TextView) this.findViewById(R.id.tv_message);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = et_receive.getText().toString();
                //向服务器发送信息
                if(!TextUtils.isEmpty(msg) && null != pw) {
                    pw.println(msg);
                    tv_message.setText(tv_message.getText() + "\n" + "客户端：" + msg);
                    et_receive.setText("");
                }
            }
        });
    }
}

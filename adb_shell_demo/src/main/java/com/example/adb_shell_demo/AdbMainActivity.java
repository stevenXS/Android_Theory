package com.example.adb_shell_demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class AdbMainActivity extends AppCompatActivity {
    private static final String TAG = "shell# ";

    private EditText edit;
    private Button btn;
    private TextView textView;
    private String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adb_main);
        edit = (EditText) findViewById(R.id.edit_shell);
        btn = (Button) findViewById(R.id.excute_shell);
        textView = (TextView) findViewById(R.id.show_res);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = executeShell(edit.getText().toString());
                textView.setText(res);
            }
        });
    }

    private String executeShell(String shell) {
        DataOutputStream dos = null;
        DataInputStream dis = null;
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(shell)) {
            Toast.makeText(AdbMainActivity.this, "execute shell", Toast.LENGTH_SHORT).show();
            String prePrefix = shell.substring(0, 9);
            if (!"adb shell".equals(prePrefix)) {
                try {
                    throw new IllegalAccessException("shell error, prefix not 'adb shell'.");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            try {
                shell = shell.replace("adb shell", "");
                final Process process = Runtime.getRuntime().exec(shell);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

//                dos = new DataOutputStream(process.getOutputStream());
//                dos.writeBytes(shell + "\n"); // 2、向进程内写入shell指令，cmd为要执行的shell命令字符串
//                dos.flush();
//                dos.writeBytes("exit\n");
//                dos.flush();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                process.waitFor();
                Log.d(TAG, "res is " + sb);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sb.toString();
    }
}
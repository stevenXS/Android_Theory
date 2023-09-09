package com.steven.extertal_storage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.steven.Utils.PermissionUtils;
import com.steven.test_demo.R;

public class ExternalStorageActivity extends AppCompatActivity implements View.OnClickListener {
    private String file_path;
    private String file_name = "user.txt";
    private Button btnWrite;
    private Button btnRead;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);
        file_path = getBaseContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/mobile_report"; // Android Q以上采用这种方式读取数据
        btnWrite = findViewById(R.id.write);
        btnRead = findViewById(R.id.read);
        btnWrite.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        PermissionUtils.checkStoragePermission(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast(this, "授权成功");
                } else {
                    PermissionUtils.checkStoragePermission(this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                    PermissionUtils.checkStoragePermission(this);
                } else {
                    toast(this, "已授权" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                String data = "text";
                ExternalStorageController.saveData(file_path, file_name, data);
                break;
            case R.id.read:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    PermissionUtils.checkStoragePermission(this);
                } else {
                    toast(this, "已授权" + Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                String result = ExternalStorageController.readData(file_path, file_name);
                toast(this, "读取文件数据 " + result);

        }
    }

    private void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

package com.steven.content_provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.steven.Utils.PermissionUtils;
import com.steven.test_demo.R;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.URI;

public class MyProviderActivity extends AppCompatActivity {
    private ContentResolver contentResolver = null;
    private Cursor cursor = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_provider);
        PermissionUtils.checkStoragePermission(this);

        Button insert = findViewById(R.id.insert);
        Button query = findViewById(R.id.query);
        contentResolver = getContentResolver();
        insert.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", "steven");
                contentValues.put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/hello");// 设置存储路径
                contentValues.put(MediaStore.Downloads.DISPLAY_NAME, "hello.txt"); // 设置文件名称
                contentValues.put(MediaStore.Downloads.TITLE, "hello"); // 设置文件标题, 一般是删除后缀, 可以不设置
                Uri uri = MediaStore.Downloads.getContentUri("external");
                try {
                    OutputStream outputStream = contentResolver.openOutputStream(uri);
                    BufferedOutputStream bos = new BufferedOutputStream(outputStream);
                    bos.write("test msg".getBytes());
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                contentResolver.insert(uri, contentValues);
            }
        });
        query.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                cursor = contentResolver.query(MediaStore.Downloads.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (cursor.moveToFirst()) {
                    String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    Toast.makeText(MyProviderActivity.this, "查询结果" + data, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

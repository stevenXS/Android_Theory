package com.steven.pick_photo_album;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.steven.camera.ConfirmationDialog;
import com.steven.test_demo.R;

/**
 * 打开手机系统相册，选择图片并显示到ImageView中
 */
public class PickPhotoActivity extends AppCompatActivity implements View.OnClickListener{
    private final int REQUEST_OPEN_ALBUM = 0x01;
    private final int REQUEST_READ_EXTERNAL_STORAGE = 0x02;
    private ImageView showImg;
    private Button openAlbum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }
        openAlbum = findViewById(R.id.open_album);
        showImg = findViewById(R.id.show_img);
        openAlbum.setOnClickListener(this);
        showImg.setOnClickListener(this);
    }

    /**
     * 处理回传结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_ALBUM) {
            assert data != null;
            Uri uri = data.getData();
            if (uri != null) {
                String realImagePath = UriUtils.getRealPathFromUri(this, uri);
                if (!TextUtils.isEmpty(realImagePath)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(realImagePath);
                    if (bitmap != null) {
                        openAlbum.setVisibility(View.INVISIBLE);
                        showImg.setImageBitmap(bitmap);
                        showImg.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_img:
                showImg.invalidate();
                showImg.setVisibility(View.INVISIBLE);
                openAlbum.setVisibility(View.VISIBLE);
                break;
            case R.id.open_album:
                openAlbum();
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 19) {
            // Intent.ACTION_GET_CONTENT:
            //  支持选择图片，视频。范围选择,
            //  注意必须申请READ_EXTERNAL_STORAGE权限，否则无法正确解析文件路径
            // Intent.ACTION_PICK:支持图片,联系人。点对点
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_OPEN_ALBUM);
    }

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
    }
}

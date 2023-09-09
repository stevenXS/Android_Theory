package com.steven.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    public static final int REQ_EXTERNAL_STORAGE = 0x01;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    };
    private static List<String> permissionList = new ArrayList<>();


    public static void checkStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            permissionList.clear();
            for (String permission : PERMISSION_STORAGE) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            if (permissionList.size() > 0 ) {
                ActivityCompat.requestPermissions((Activity) context, PERMISSION_STORAGE, REQ_EXTERNAL_STORAGE);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 30) {
                if (!Environment.isExternalStorageManager()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("提示")//设置标题
                            .setMessage("请开启文件访问权限，否则无法正常使用本应用！")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    context.startActivity(intent);
                                }
                            }).create();
                    dialog.show();
                }
            }
        }
    }
}

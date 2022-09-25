package com.steven.ui_demo.dialog_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.steven.ui_demo.R;

public class AlertDialogMainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLayoutDialog();
    }

    private void showLayoutDialog() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_alertdialog_main,null);
        TextView dialogText = (TextView) dialogView.findViewById(R.id.dialog_text);
        Button dialogBtnConfirm = (Button) dialogView.findViewById(R.id.dialog_btn_confirm);
        Button dialogBtnCancel = (Button) dialogView.findViewById(R.id.dialog_btn_cancel);
        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(this);
        layoutDialog.setTitle("自定义对话框");
        layoutDialog.setIcon(R.mipmap.ic_launcher_round);
        layoutDialog.setView(dialogView);

        //设置组件
        dialogText.setText("我是自定义layout的弹窗！！");
        AlertDialog dialog = layoutDialog.create();

        // 空间类自带的事件监听方法
//        layoutDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(AlertDialogMainActivity.this,"确定！",Toast.LENGTH_SHORT).show();
//            }
//        });

        // 自定义View中按钮监听事件
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AlertDialogMainActivity.this,"dismiss！",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // 确认事件
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AlertDialogMainActivity.this,"ok！",Toast.LENGTH_SHORT).show();
            }
        });

        // 取消事件
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AlertDialogMainActivity.this,"取消",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // 创建对话框实例
        dialog.show();
    }
}

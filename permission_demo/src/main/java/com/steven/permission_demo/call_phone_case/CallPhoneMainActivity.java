package com.steven.permission_demo.call_phone_case;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.steven.permission_demo.R;

/**
 * 申请电话权限，成功后跳转到打电话页面
 */
public class CallPhoneMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_call = findViewById(R.id.btn_call_phone);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CallPhoneMainActivity.this, Manifest.permission.CALL_PHONE)
                 != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            CallPhoneMainActivity.this, new String[]{Manifest.permission.CALL_PHONE},123);
                }
            }
        });
    }

    public void call(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }

    /**
     * 重写权限方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 如果权限获取成功则回调该方法
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(CallPhoneMainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
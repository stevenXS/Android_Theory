package com.steven.test_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.steven.annotaion.AnnotationActivity;
import com.steven.camera.CameraActivity;
import com.steven.content_provider.MyProviderActivity;
import com.steven.event_bus.FirstActivity;
import com.steven.extertal_storage.ExternalStorageActivity;
import com.steven.performance.SplashActivity;
import com.steven.pick_photo_album.PickPhotoActivity;
import com.steven.test_demo.activity.CountDownActivity;
import com.steven.test_demo.activity.CustomLoadingActivity;
import com.steven.test_demo.activity.RemoteAMainActivity;
import com.steven.test_demo.activity.RemoteBMainActivity;
import com.steven.test_demo.activity.WebViewActivity;
import com.steven.test_demo.activity.SearchActivity;
import com.steven.test_demo.custom_view.CustomFloatActionButton;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton("webview_activity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用JS对应的函数
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        addButton("remoteActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemoteAMainActivity.class);
                startActivity(intent);
            }
        });

        addButton("remoteActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemoteBMainActivity.class);
                startActivity(intent);
            }
        });

        addButton("customLoadingActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomLoadingActivity.class);
                startActivity(intent);
            }
        });

        addButton("CountDownActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CountDownActivity.class);
                startActivity(intent);
            }
        });
        addView("FloatButton", new CustomFloatActionButton.FloatButtonListener(this, "测试"),
                new CustomFloatActionButton(this));
        addButton("SearchActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        addButton("Camera2Activity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        addButton("PickPhotoActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PickPhotoActivity.class);
                startActivity(intent);
            }
        });
        addButton("ExternalStorageActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExternalStorageActivity.class);
                startActivity(intent);
            }
        });
        addButton("MyProviderActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyProviderActivity.class);
                startActivity(intent);
            }
        });
        addButton("EventBus", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });
        addButton("Annotation", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnnotationActivity.class);
                startActivity(intent);
            }
        });

        addButton("SplashActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addButton(String btnName, View.OnClickListener listener){
        LinearLayout rootView = (LinearLayout) findViewById(R.id.ll_root);
        Button button = new Button(this);
        button.setText(btnName);
        button.setTag(btnName);
        if (listener != null) {
            button.setOnClickListener(listener);
        }
        if (rootView != null){
            rootView.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public void addView(String title, View.OnClickListener listener, View view) {
        LinearLayout rootView = findViewById(R.id.ll_root);
        rootView.setOrientation(LinearLayout.VERTICAL);
        if (view == null) {
            view = new TextView(this);
            rootView.addView(view);
        }
        if (listener != null) {
            view.setOnClickListener(listener);
        }
        // 获取当前屏幕大小
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth();

        // 当前布局中，view的起始位置, 偏移量。当前view相当于该view的布局的偏移量
        // 布局的左上角为原点
        view.setX((float) (width * 0.4));
        view.setY(20); // 相比原点，Y方向下移动N像素

        // LinearLayout.LayoutParams决定view的布局大小
        rootView.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }
}
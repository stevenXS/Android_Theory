package com.example.design_pattern;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.design_pattern.composite.College;
import com.example.design_pattern.composite.Component;
import com.example.design_pattern.composite.Department;
import com.example.design_pattern.composite.University;
import com.example.design_pattern.facade_pattern.FacedePatternManager;
import com.example.design_pattern.mvp.presenter.impl.MVPActivity;
import com.example.design_pattern.mvp.view.activity.BaseActivity;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        addButton("MVP", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Component university = new University("清华大学");
                Component college1 = new College("电子信息学院");
                Component college2 = new College("计算机学院");
                college1.add(new Department("计算机科学", "计算机科学"));
                college1.add(new Department("软件工程", "软件工程"));
                college2.add(new Department("通信专业", "通信专业"));
                college2.add(new Department("电路", "电路"));

                university.add(college1);
                university.add(college2);
                university.print();

                FacedePatternManager patternManager = new FacedePatternManager();
                patternManager.ready();
                patternManager.play();
                patternManager.end();
                // 调用JS对应的函数
                Intent intent = new Intent(MainActivity.this, MVPActivity.class);
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

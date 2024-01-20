package com.steven.annotaion;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.annotation.MyBindView;
import com.steven.test_demo.R;

public class AnnotationActivity2 extends AppCompatActivity {
    @MyBindView(R.id.text)
    public TextView textView2;

    @MyBindView(R.id.btn)
    public Button btn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        // 编译期注解解释器生成代码，构造触发生成代码的逻辑
        MyAnnotation.bind(this);
        textView2.setText("自定义注解绑定TextView");
        btn2.setText("自定义注解绑定btn");
    }
}

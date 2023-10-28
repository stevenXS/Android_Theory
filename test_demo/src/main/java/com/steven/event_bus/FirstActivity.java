package com.steven.event_bus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.steven.test_demo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FirstActivity extends AppCompatActivity {
    private TextView tv_message;
    private Button bt_message;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        tv_message = findViewById(R.id.text);
        bt_message = findViewById(R.id.btn);
        tv_message.setText("FirstActivity");
        bt_message.setText("跳转到SecondActivity");
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, SecondActivity.class));
            }
        });
        // register event
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregister event
        EventBus.getDefault().unregister(this);
    }

    // 事件的处理会在UI线程中执行，用TextView来展示收到的事件消息：
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMooEvent(MessageEvent event) {
        tv_message.setText(event.getMessage());
    }
}

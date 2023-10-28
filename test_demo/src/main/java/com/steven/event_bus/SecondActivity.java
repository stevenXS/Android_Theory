package com.steven.event_bus;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.steven.test_demo.R;

import org.greenrobot.eventbus.EventBus;

public class SecondActivity extends AppCompatActivity {
    private Button bt_message;
    private TextView tv_message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        tv_message=(TextView)this.findViewById(R.id.text);
        tv_message.setText("SecondActivity");
        bt_message=(Button)this.findViewById(R.id.btn);
        bt_message.setText("发送事件");
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("来自SecondActivity发布者的消息"));
                finish();
            }
        });
    }
}

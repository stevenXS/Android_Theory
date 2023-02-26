package com.steven.test_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton("remoteActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemoteAMainActivity.class);
                startActivity(intent);
            }
        });

        addButton("remoteBctivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemoteBMainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addButton(String btnName, View.OnClickListener listener){
        LinearLayout rootView = (LinearLayout) findViewById(R.id.ll_root);
        Button button = new Button(this);
        button.setText(btnName);
        button.setTag(btnName);

        button.setOnClickListener(listener);

        if (rootView != null){
            rootView.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
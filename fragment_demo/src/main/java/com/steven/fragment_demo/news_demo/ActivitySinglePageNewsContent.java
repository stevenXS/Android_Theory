package com.steven.fragment_demo.news_demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.steven.fragment_demo.R;

public class ActivitySinglePageNewsContent extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_page_news_content);
        FragmetNewsContent fragment = (FragmetNewsContent) getSupportFragmentManager().findFragmentById(R.id.single_page_news_content_fragment);
        fragment.refresh(getIntent().getStringExtra("news_title"), getIntent().getStringExtra("news_content"));
        Log.d(" ActivitySingle#","onCreate");
    }

    // 根据当前上下文跳转到当前activity
    public static void actionStart(Context context, String title, String content){
        Intent intent = new Intent(context, ActivitySinglePageNewsContent.class);
        intent.putExtra("news_title",title);
        intent.putExtra("news_content",content);
        context.startActivity(intent);
    }
}

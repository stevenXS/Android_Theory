package com.steven.fragment_demo.news_demo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.steven.fragment_demo.R;

/*
如果是手机，则单个fragment显示标题，每个标题点击点击后显示内容；
如果是平板，则只显示内容不显示标题；
 */
public class FragmetNewsContent extends Fragment {
    private View view;
    private TextView tv_title;
    private TextView tv_content;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 绑定布局资源
        Log.d("FragmetNewsContent","create");
        view = inflater.inflate(R.layout.fragment_news_content, container, false);
        return view;
    }

    public void refresh(String title, String content){
        View tc_layout = view.findViewById(R.id.title_content_layout);
        tc_layout.setVisibility(View.VISIBLE); // 设置可见
        tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
        tv_title.setText(title);
        tv_content.setText(content);

    }
}



















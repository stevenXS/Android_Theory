package com.steven.test_demo.activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.steven.test_demo.R;
import com.steven.test_demo.customSearchView.callback.IReturnCallback;
import com.steven.test_demo.customSearchView.callback.ISearchCallback;
import com.steven.test_demo.customSearchView.view.CustomSearchView;

public class SearchActivity extends AppCompatActivity {
    private CustomSearchView customSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        customSearchView = (CustomSearchView) findViewById(R.id.custom_search_view);
        if (customSearchView != null) {
            customSearchView.setOnReturnClickListener(new IReturnCallback() {
                @Override
                public void onReturn() {
                    Log.d("searchView", "点击返回");
                }
            });
            customSearchView.setOnSearchClickListener(new ISearchCallback() {
                @Override
                public void onSearch(String string) {
                    Log.d("searchView", "点击搜索，内容 = " + string);
                }
            });
        }
    }
}

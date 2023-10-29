package com.example.design_pattern.mvp.presenter;

import com.example.design_pattern.mvp.model.bean.ArticleInfo;

public interface OnArticleListener {
    void onSuccess(ArticleInfo articleInfo);
    void onStart();
    void onFailed();
    void onFinish();
}

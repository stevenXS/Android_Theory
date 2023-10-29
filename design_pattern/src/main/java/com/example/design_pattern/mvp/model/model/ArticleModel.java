package com.example.design_pattern.mvp.model.model;

import com.example.design_pattern.mvp.presenter.OnArticleListener;

public interface ArticleModel {
    void getArticle(OnArticleListener onArticleListener);
}

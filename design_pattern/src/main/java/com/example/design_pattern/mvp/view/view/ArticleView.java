package com.example.design_pattern.mvp.view.view;

import com.example.design_pattern.mvp.model.bean.ArticleInfo;

public interface ArticleView {
    void setArticleInfo(ArticleInfo articleInfo);
    void showLoading();
    void hideLoading();
    void showError();
}

package com.example.design_pattern.mvp.presenter.impl;

import com.example.design_pattern.mvp.model.bean.ArticleInfo;
import com.example.design_pattern.mvp.model.impl.ArticleModelImpl;
import com.example.design_pattern.mvp.model.model.ArticleModel;
import com.example.design_pattern.mvp.presenter.ArticlePresenter;
import com.example.design_pattern.mvp.presenter.OnArticleListener;
import com.example.design_pattern.mvp.view.view.ArticleView;

/**
 * 桥接View和Model层，两者不直接交互，基于Presenter交互。
 */
public class ArticlePresenterImpl implements ArticlePresenter, OnArticleListener {
    private ArticleModel articleModel; // Model层
    private ArticleView articleView; // View层

    public ArticlePresenterImpl(ArticleView view) {
        this.articleModel = new ArticleModelImpl();
        this.articleView = view;
    }

    @Override
    public void getArticle() {
        // Model注入监听实例；
        // Presenter层实现了监听实例；
        // 回调下面的方法给视图层，视图层根据业务逻辑定义回调接口
        articleModel.getArticle(this);
    }

    @Override
    public void onSuccess(ArticleInfo articleInfo) {
        articleView.setArticleInfo(articleInfo);
    }

    @Override
    public void onStart() {
        articleView.showLoading();
    }

    @Override
    public void onFailed() {
        articleView.showError();
    }

    @Override
    public void onFinish() {
        articleView.hideLoading();
    }
}

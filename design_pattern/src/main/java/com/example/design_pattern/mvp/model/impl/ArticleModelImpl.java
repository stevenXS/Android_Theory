package com.example.design_pattern.mvp.model.impl;

import com.example.design_pattern.mvp.model.bean.ArticleInfo;
import com.example.design_pattern.mvp.model.model.ArticleModel;
import com.example.design_pattern.mvp.presenter.OnArticleListener;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;

public class ArticleModelImpl implements ArticleModel {
    @Override
    public void getArticle(OnArticleListener onArticleListener) {
        HttpRequest.post("http://api.1-blog.com/biz/bizserver/article/list.do", new BaseHttpRequestCallback<ArticleInfo>(){
            @Override
            public void onStart() {
                super.onStart();
                onArticleListener.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onArticleListener.onFinish();
            }

            @Override
            protected void onSuccess(ArticleInfo articleInfo) {
                super.onStart();
                onArticleListener.onSuccess(articleInfo);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                onArticleListener.onFailed();
            }
        });
    }
}

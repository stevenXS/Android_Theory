package com.steven.android_theory.MVP;

import android.content.Context;

interface ICidianPresenter{
   void inputToModel(String s, Context context);
}

public class CidianPresenter implements ICidianPresenter, onResultListener{
    // 添加View&Model层接口
    private IFanyiView fanyiView;
    private IFanyiModel fanyiModel;

    public CidianPresenter(IFanyiView view){
        fanyiView = view;
        fanyiModel = new FanyiModel();
    }

    @Override
    public void inputToModel(String s, Context context) {
        fanyiModel.handleData(s, context, this);
    }

    @Override
    public void onSuccess(String s) {
        // 将Model层的处理结果回调给View层
        fanyiView.setInfo(s);
    }

    @Override
    public void onError() {
        fanyiView.setError();
    }
}

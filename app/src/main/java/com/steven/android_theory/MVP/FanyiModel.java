package com.steven.android_theory.MVP;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

interface IFanyiModel{
    void handleData(String input, Context context, final onResultListener listener);
}

public class FanyiModel implements IFanyiModel{
    @Override
    public void handleData(String input, Context context, onResultListener listener) {
        if (context != null && !TextUtils.isEmpty(input)){
            if (input.equals("Hello")){
                listener.onSuccess("你好");
            }else {
                listener.onError();
            }
        }
    }

    // refine javaBean
//    class FanyiResult{
//        String res;
//    }
}

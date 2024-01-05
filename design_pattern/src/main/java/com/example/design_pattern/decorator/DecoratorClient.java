package com.example.design_pattern.decorator;

import android.util.Log;

public class DecoratorClient {
    public static void execute() {
        Component america = new America();
        // 一份美式
        Log.d("aaa", " cost " + america.cost() + " desc " + america.getDesc());

        // 一份美式 + 一份牛奶
        america = new ChocolateDec(america);
        Log.d("aaa", " cost " + america.cost() + " desc " + america.getDesc());
        // 一份美式 + 两份牛奶
        america = new ChocolateDec(america);
        Log.d("aaa", " cost " + america.cost() + " desc " + america.getDesc());
}
}

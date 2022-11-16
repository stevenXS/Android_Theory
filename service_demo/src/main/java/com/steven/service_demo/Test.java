package com.steven.service_demo;

import java.lang.ref.WeakReference;

public class Test {
    public static void main(String[] args) {
        String str = new String("aaaa");
        System.out.println(str);
        WeakReference<String> reference = new WeakReference<>(str);
        String s = reference.get();
        if (s != null)
            System.out.printf(s);
    }
}

package com.steven.async_demo;

public class MySyncTask {
    public static void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("点外卖");
            }
        }).start();

        try {
            Thread.sleep(2000);
            System.out.println("外卖到了");
            System.out.println("吃外卖");
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("工作");
    }
}

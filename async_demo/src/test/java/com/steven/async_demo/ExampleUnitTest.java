package com.steven.async_demo;

import org.junit.Test;

import static org.junit.Assert.*;

import com.steven.async_demo.async.MyAsyncTask;
import com.steven.async_demo.async.MyFutureTask;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        MyFutureTask futureTask = new MyFutureTask();

        new Thread(new Runnable() {
            @Override
            public void run() {
                MyAsyncTask asyncTask = new MyAsyncTask();
                futureTask.setData(asyncTask);
                System.out.println("点外卖");
            }
        }).start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("工作");
        System.out.println(futureTask.getResult());
    }
}
package com.steven.service_demo;

import org.junit.Test;

import static org.junit.Assert.*;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        List<WeakReference<String>>
                sListenerRef = Collections.synchronizedList(new ArrayList<WeakReference<String>>());
            String str = new String("aaaa");
            System.out.println(str);
            sListenerRef.add(new WeakReference<>(str));
            try {
                Thread.sleep(3000);
            }catch (Exception e){

            }
        WeakReference<String> stringWeakReference = sListenerRef.get(0);
        String s = (String) stringWeakReference.get();
            if (s != null)
                System.out.printf(s);

    }
}
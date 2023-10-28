package com.steven.annotaion;

import android.app.Activity;

import com.example.annotation.MyBindView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MyAnnotation {
    public static void bind(Activity activity) {
        String name = activity.getClass().getName();
        ClassLoader classLoader = activity.getClass().getClassLoader();
        try {
            Class<?> aClass = classLoader.loadClass(name + MyBindView.SUFFIX);
            Constructor<?> constructor = aClass.getConstructor(activity.getClass());
            constructor.newInstance(activity);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}

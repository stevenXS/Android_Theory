package com.steven.annotataion;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS) //生命周期是编译期，存活于.class文件，当jvm加载class时小时
@Target(ElementType.FIELD) // 目标类型为类变量
public @interface BindView {
    /**
     *
     * @return 控件变量的resourceID
     */
    int value();
}

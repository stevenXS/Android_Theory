package com.steven.kotlin_demo.myLazy

import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.steven.kotlin_demo.CommonActivity

/**
 * 测试自定义lazy函数
 */
class MyLazyMainActivity(): CommonActivity(){
    val p by later{
        Log.d("MyLazyMainActivity", "test my later")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addView("my lazy", "", Button(this)){
            p.dec() //触发懒加载执行里面的日志，表明自定义的lazy函数成功被执行【仅执行一次】
        }
    }
}
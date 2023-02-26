package com.steven.kotlin_demo.highLevelFunction

import android.content.Context
import android.content.SharedPreferences

/**
 * 基于高阶函数简化写法
 */
class HighFunctionMain {
    /**
     * 通过扩展方式定义一个高阶函数
     */
    fun SharedPreferences.open(myBlock: SharedPreferences.Editor.() -> Unit){
        val editor = edit()
        editor.myBlock()
        editor.apply()
    }

    fun getSP(context: Context){
        // open高阶函数完成了sp的提交
        context.getSharedPreferences("test", Context.MODE_PRIVATE).open {
            putString("name","steven")
            putString("age","12")
        }
    }
}
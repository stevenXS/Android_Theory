package com.steven.kotlin_demo.myLazy

import kotlin.reflect.KProperty

/**
 * 定义委托函数
 */
class Later<T>(val block: () -> T){
    var value: Any? = null
    operator fun getValue(any: Any?, prop: KProperty<*>): T{
        if(value == null){
            value = block()
        }
        return value as T
    }
}

/**
 * 定义顶层方法
 */
fun<T> later(block: () -> T) = Later(block)
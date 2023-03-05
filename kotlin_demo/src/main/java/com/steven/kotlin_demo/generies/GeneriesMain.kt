package com.steven.kotlin_demo.generies

/**
 * 泛型类使用
 */
class GeneriesMain {
    // 创建一个扩展函数build(),接受参数类型为StringBuilder的高阶函数
    fun StringBuilder.build(block: StringBuilder.() -> Unit): StringBuilder{
        block()
        return this
    }

    // 基于泛型实现上述逻辑
    fun <T> T.build(block: T.() -> Unit): T{
        block()
        return this
    }
}
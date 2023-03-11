package com.steven.kotlin_demo.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.steven.kotlin_demo.bean.CellPhone
import com.steven.kotlin_demo.construtor.Student
import kotlinx.coroutines.*
import kotlin.math.max

class CoroutinesActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 主、次构造函数
        val student1 = Student() // 调用第二个次构造函数
        val student2 = Student("steven",12) // 调用第一个次构造函数
        val student3 = Student("steven",12,"ss",13) // 调用主构造函数

        // data class
        val cellPhone1 = CellPhone(1233, "huawei")
        val cellPhone2 = CellPhone(1233, "huawei")
        println(cellPhone1)
        println(cellPhone2 == cellPhone1)

        // 集合
        onSet()

        /**
         * 协程
         * runBlocking:
         *   每个launch可以视作一个协程块
         *   runBlocking能保证每个协程都被执行
         */
        runBlocking {
            launch {
                println("it's coroutines 1")
                delay(1000)
            }
            launch {
                println("it's coroutines 2")
            }
        }
        /**
         * Global.launch:
         *   顶层协程，application退出后其launch块代码无法被执行，该协程一般用于测试使用
         */
        GlobalScope.launch{
            delay(3000)
            println("it's global scope")
            printNum()
        }

        runBlocking {
            /**
             * coroutineScope：
             *  # 会继承外部协程作用域（会阻塞外部协程）并创建协程，相当于Java.join()
             *  # 其代码块中的协程会并发执行；并优先执行非协程的代码
             * 上述代码执行完毕后，runBlocking()中的协程是并发执行的
             */
            coroutineScope {
                println("it's launch1")
                println("it's launch2")
                launch {
                    delay(1000)
                    println("it's launch3")
                }
                launch {
                    delay(200)
                    println("it's launch4")
                }
                launch {
                    for(i in 0..10){
                        println(i)
                        delay(30)
                    }
                    println("it's launch5")
                }
            }
            /**
             * 以下运行是并发操作
             */
            launch {
                delay(2000)
                println("it's launch6")
            }
            launch {
                delay(1000)
                println("it's launch7")
            }
            launch {
                delay(500)
                println("it's launch8")
            }
            println("it's coroutineScope")
        }

        /**
         * Async：获取协程的返回结果
         */
        runBlocking {
            val res = async {
                5 + 5
            }.await()
            println(res)
        }
    }

    fun onSet(){
        val numb = listOf<Int>(1,4,5)
        val maxNum = numb.maxOf({n: Int -> n})
        println("maxNUm" + maxNum)

        val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
        val maxLen = fruits.maxByOrNull { fruits: String -> fruits.length }
        println("fruits " + maxLen)

        var map = fruits.map { it.uppercase() }
        map.forEach({ it -> println(it) })

        // 过滤多个条件
        fruits
                .filter { it.startsWith("a") }
                .sortedBy { it }
                .map { it.uppercase() }
                .forEach { println(it) }
    }

    fun largeNum(p1: Int, p2: Int): Int = max(p1, p2)

    /**
     * 创建可挂起的函数【协程函数】
     */
    suspend fun printNum(){
        println("it's custom coroutines method")
    }
}
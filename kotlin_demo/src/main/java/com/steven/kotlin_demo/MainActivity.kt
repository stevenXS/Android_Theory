package com.steven.kotlin_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.R
import com.steven.kotlin_demo.construtor.Student
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_action)
        val student1 = Student() // 调用第二个次构造函数
        val student2 = Student("steven",12) // 调用第一个次构造函数
        val student3 = Student("steven",12,"ss",13) // 调用主构造函数

//        getRes()
    }

    fun getRes(){
        val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
        fruits
            .filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.uppercase() }
            .forEach { println(it) }
    }
    fun largeNum(p1: Int, p2: Int): Int = max(p1, p2)
}
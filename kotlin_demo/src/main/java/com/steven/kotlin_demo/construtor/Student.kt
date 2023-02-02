package com.steven.kotlin_demo.construtor

import com.steven.kotlin_demo.construtor.Person

/**
 * 括号与构造函数相关；
 * 次构造函数相当于去重载主构造函数。
 */
class Student(val son:String , val sage: Int, name:String, age:Int): Person(name, age) {
    // 主构造函数的逻辑可以封装在init结构体中
    // kotlin有且仅有一个主构造函数
    init {
        println("son is" + son)
        println("sage is" + sage)
    }

    // kotlin可以有多个次构造函数
    // 所有次构造函数必须调用或间接调用主构造函数，下面用this关键字调用了其主构造函数。
    constructor(x: String, y: Int) : this("",0, x,y){

    }

    // 下面的第一个函数体不传入任何参数，此时this关键字[当前类的次构造方法]调用了第一个constructor()
    constructor():this("",0){}
}
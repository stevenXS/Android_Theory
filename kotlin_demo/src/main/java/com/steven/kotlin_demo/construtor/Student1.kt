package com.steven.kotlin_demo.construtor

// 如果子类没有显式定义主构造函数且定义了次构造函数，那么可以省略()
class Student1 : Person1 {
    constructor(name: String, age : Int) : super(name, age){}
}
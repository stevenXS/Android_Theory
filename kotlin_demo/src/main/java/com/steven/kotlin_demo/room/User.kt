package com.steven.kotlin_demo.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(var firstName: String, val lastName: String, var age:Int){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

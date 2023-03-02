package com.steven.kotlin_demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.steven.kotlin_demo.commonActivity.CommonActivity
import com.steven.kotlin_demo.coroutines.CoroutinesActivity
import com.steven.kotlin_demo.sqllite.SQLLiteActivity

class MainActivity : CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 非泛型方法
        addBtn("测试按钮", ""){
            Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show()
        }

        addBtn("协程", ""){
            startActivity(Intent(this, CoroutinesActivity::class.java))
        }

        addBtn("SQLite", ""){
            startActivity(Intent(this, SQLLiteActivity::class.java))
        }

        // 泛型方法
        addView("Text", "", TextView(this)){
            startActivity(Intent(this, SQLLiteActivity::class.java))
        }

        addView("Switch", "", Switch(this)){
            startActivity(Intent(this, SQLLiteActivity::class.java))
        }

        addView("Button", "", Button(this)){
            startActivity(Intent(this, SQLLiteActivity::class.java))
        }
    }
}
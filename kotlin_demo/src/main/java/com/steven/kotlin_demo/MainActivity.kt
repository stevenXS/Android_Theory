package com.steven.kotlin_demo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.steven.kotlin_demo.commonActivity.CommonActivity
import com.steven.kotlin_demo.coroutines.CoroutinesActivity
import com.steven.kotlin_demo.sqllite.SQLLiteActivity

class MainActivity : CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addBtn("测试按钮", ""){
            Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show()
        }

        addBtn("协程", ""){
            startActivity(Intent(this, CoroutinesActivity::class.java))
        }

        addBtn("SQLite", ""){
            startActivity(Intent(this, SQLLiteActivity::class.java))
        }
    }
}
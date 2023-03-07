package com.steven.kotlin_demo.sqllite

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.steven.kotlin_demo.CommonActivity

class SQLLiteActivity: CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addView("创建数据库","null", Button(this)) {
            Toast.makeText(this, "test", Toast.LENGTH_LONG).show()
        }
    }
}
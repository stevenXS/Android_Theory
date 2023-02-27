package com.steven.kotlin_demo.sqllite

import android.os.Bundle
import android.widget.Toast
import com.steven.kotlin_demo.commonActivity.CommonActivity

class SQLLiteActivity: CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addBtn("创建数据库","null"
        ) { Toast.makeText(this, "test", Toast.LENGTH_LONG).show() }

    }
}
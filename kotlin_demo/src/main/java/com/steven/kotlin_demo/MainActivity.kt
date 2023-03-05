package com.steven.kotlin_demo

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.steven.kotlin_demo.alertDialog.MyDialogActivity
import com.steven.kotlin_demo.commonActivity.CommonActivity
import com.steven.kotlin_demo.coroutines.CoroutinesActivity
import com.steven.kotlin_demo.myLazy.MyLazyMainActivity
import com.steven.kotlin_demo.myLazy.later
import com.steven.kotlin_demo.sqllite.SQLLiteActivity

class MainActivity : CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 非泛型方法
        addView("测试按钮", "", Button(this)){
            Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show()
        }

        addView("协程", "", Button(this)){
            startActivity(Intent(this, CoroutinesActivity::class.java))
        }

        addView("SQLite", "", Button(this)){
            startActivity(Intent(this, SQLLiteActivity::class.java))
        }

        // 泛型方法
        addView("Text", "", TextView(this)){
            startActivity(Intent(this, SQLLiteActivity::class.java))
        }

        addView("Switch", "", Switch(this)){
            startActivity(Intent(this, SQLLiteActivity::class.java))
        }

        addView("输入APPID", "", Button(this)){
            val et = EditText(this)
            AlertDialog.Builder(this)
                    .setView(et)
                    .setPositiveButton("true", object: DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            Toast.makeText(this@MainActivity, et.text.toString(), Toast.LENGTH_LONG).show()
                        }
                    })
                    .show()
        }

        addView("MyLazy Fun Test", "", Button(this)){
            startActivity(Intent(this, MyLazyMainActivity::class.java))
        }

        addView("Dialog", "", Button(this)){
            startActivity(Intent(this, MyDialogActivity::class.java))
        }
    }
}
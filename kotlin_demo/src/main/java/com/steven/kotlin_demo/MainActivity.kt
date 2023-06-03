package com.steven.kotlin_demo

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.steven.kotlin_demo.alertDialog.MyDialogActivity
import com.steven.kotlin_demo.cameraDemo.CameraActivity
import com.steven.kotlin_demo.content_provider.MyContentProviderActivity
import com.steven.kotlin_demo.coroutines.CoroutinesActivity
import com.steven.kotlin_demo.jetPack.lifeCycle.MyObserver
import com.steven.kotlin_demo.jetPack.viewModel.ViewModelMainActivity
import com.steven.kotlin_demo.myLazy.MyLazyMainActivity
import com.steven.kotlin_demo.room.RoomMainActivity
import com.steven.kotlin_demo.service.ServiceMainActivity
import com.steven.kotlin_demo.sqllite.SQLLiteActivity
import com.steven.kotlin_demo.view.MaterialMainActivity
import com.steven.kotlin_demo.webView.WebViewMainActivity
import com.steven.kotlin_demo.workManager.WorkManagerMainActivity
import java.util.Objects

class MainActivity : CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(MyObserver(lifecycle))
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
        addView("text", "", TextView(this)){
            startActivity(Intent(this, SQLLiteActivity::class.java))
        }

        addView("switch", "", Switch(this)){
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
        addView("myLazy_fun_test", "", Button(this)){
            startActivity(Intent(this, MyLazyMainActivity::class.java))
        }
        addView("dialog", "", Button(this)){
            startActivity(Intent(this, MyDialogActivity::class.java))
        }
        addView("takePhoto", "", Button(this)){
            startActivity(Intent(this, CameraActivity::class.java))
        }
        addView("content_provider", "", Button(this)){
            startActivity(Intent(this, MyContentProviderActivity::class.java))
        }
        addView("service", "", Button(this)){
            startActivity(Intent(this, ServiceMainActivity::class.java))
        }
        addView("web_view", "", Button(this)){
            startActivity(Intent(this, WebViewMainActivity::class.java))
        }
        addView("material_view", "", Button(this)){
            startActivity(Intent(this, MaterialMainActivity::class.java))
        }
        addView("jetPack", "", Button(this)){
            startActivity(Intent(this, ViewModelMainActivity::class.java))
        }
        addView("room", "", Button(this)){
            startActivity(Intent(this, RoomMainActivity::class.java))
        }
        addView("work_manger", "", Button(this)){
            startActivity(Intent(this, WorkManagerMainActivity::class.java))
        }
        addView("test", "", Button(this)){
            startActivity(Intent(this, TestMainActivity::class.java))
        }
    }
}
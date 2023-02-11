package com.steven.jetpack_demo

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.steven.jetpack_demo.lifecycles.MyObserver
import com.steven.jetpack_demo.viewModel.MainViewModel
import com.steven.jetpack_demo.viewModel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var sp: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 添加生命周期观察者
        lifecycle.addObserver(MyObserver())

        // 创建ViewModel实例的方式
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count",0)

        // 基于ViewModelFactory的方式像ViewHolder传递参数
        viewModel = ViewModelProvider(this, ViewModelFactory(countReserved))
            .get(MainViewModel::class.java)

        var plusOneBtn = findViewById<Button>(R.id.plusOneBtn)
        var clearBtn = findViewById<Button>(R.id.clearBtn)
        var infoText = findViewById<TextView>(R.id.infoText)
        plusOneBtn.setOnClickListener {
            viewModel.plusOne()
        }
        clearBtn.setOnClickListener {
            viewModel.clear()
        }

        viewModel.counter.observe(this, Observer {count ->
            infoText.text = count.toString()
        })
    }

    override fun onPause() {
        super.onPause()
        var edit = sp.edit()
        edit.putInt("count", viewModel.counter.value?:0).apply()
        // 上一句使用let函数重写
//        viewModel.counter.value?.let { edit.putInt("count", it).apply() }
    }
}
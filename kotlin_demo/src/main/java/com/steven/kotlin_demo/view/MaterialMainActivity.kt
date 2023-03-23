package com.steven.kotlin_demo.view

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.view.GravityCompat
import com.steven.kotlin_demo.CommonActivity
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_meterial_main.*

class MaterialMainActivity : CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meterial_main)
//        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        }
        setSystemBar(this)
    }

    fun setSystemBar(activity: Activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 增加绘制系统bar的背景图标志（5.0以上使用该标志位）
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS )
            // 同时清除透明状态栏标志（4.4）才使用该标志位
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 设置顶部状态栏颜色
            activity.window.statusBarColor = Color.TRANSPARENT
            // 设置底部导航栏颜色
            activity.window.navigationBarColor = Color.TRANSPARENT
            // View铺满至顶部状态栏
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            // View铺满至底部导航栏, 隐藏底部导航栏
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)

        }
        return true
    }
}
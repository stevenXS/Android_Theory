package com.steven.kotlin_demo.view

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.steven.kotlin_demo.CommonActivity
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_meterial_main.*

class MaterialMainActivity : CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meterial_main)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)

        }
        return true
    }
}
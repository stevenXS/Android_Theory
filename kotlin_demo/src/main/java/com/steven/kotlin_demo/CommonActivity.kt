package com.steven.kotlin_demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.root_layout.*

open class CommonActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_layout)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun <T: View>addView(name: String, desc: String, view: T, onClick: ()->Unit ): T {
        if (view is Button){
            view.text = name
            view.textSize = 20F
            view.isAllCaps = false
        }else if(view is Switch){
            view.text = name
            view.textSize = 20F
            view.isAllCaps = false
        }else if(view is TextView){
            view.text = name
            view.textSize = 20F
            view.isAllCaps = false
        }else if (view is EditText){
            Toast.makeText(this, view.text.toString(), Toast.LENGTH_LONG).show()
        }
        view.setOnClickListener {
            onClick.invoke()
        }
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        ll_root!!.addView(view, lp)

        if (!TextUtils.isEmpty(desc)){
            val descTv = TextView(this)
            descTv.text = desc
            descTv.gravity = Gravity.CENTER
            ll_root!!.addView(descTv, lp)
            ll_root!!.addView(View(this), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,12))
        }

        return view;
    }

    fun Activity.toast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
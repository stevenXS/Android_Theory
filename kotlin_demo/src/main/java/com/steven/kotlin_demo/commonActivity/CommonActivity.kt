package com.steven.kotlin_demo.commonActivity

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.root_layout.*

open class CommonActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_layout)
    }

    fun <T: View>addView(name: String, desc: String, view: T, onClick: ()->Unit ): T {
        if (view is Button){
            view.text = name
            view.textSize = 18F
        }else if(view is Switch){
            view.text = name
            view.textSize = 18F
        }else if(view is TextView){
            view.text = name
            view.textSize = 18F
        }
        view.setOnClickListener {
            onClick.invoke()
        }
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        ll_root!!.addView(view, lp)
        ll_root!!.addView(View(this), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,6))
        if (!TextUtils.isEmpty(desc)){
            val descTv = TextView(this)
            descTv.text = desc
            descTv.gravity = Gravity.CENTER
            ll_root!!.addView(descTv, lp)
            ll_root!!.addView(View(this), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,6))
        }
        return view;
    }

}
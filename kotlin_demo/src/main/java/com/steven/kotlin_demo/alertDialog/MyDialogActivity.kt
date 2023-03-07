package com.steven.kotlin_demo.alertDialog

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.steven.kotlin_demo.CommonActivity

class MyDialogActivity(): CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addView("Dialog","", Button(this)){
            showDialog()
        }
    }

    fun showDialog(){
        val builder = AlertDialog.Builder(this)
        val value = arrayOf("China", "America", "Japanese")
        // 列表对话框
//        builder.setItems(value){dialog, which ->
//            toast("选择了 + ${value[which]}")
//        }
        // 单选对话框
//        builder.setSingleChoiceItems(value, 0){dialog, which ->
//            toast("选择了 + ${value[which]}")
//        }
        // 复选对话框
        builder.setMultiChoiceItems(value, null) { dialog, which, isChecked ->
            if (isChecked){
                toast("选择了 + ${value[which]}")
            }
        }

        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    fun toast(desc: String){
        Toast.makeText(this@MyDialogActivity, desc, Toast.LENGTH_SHORT).show()
    }
}
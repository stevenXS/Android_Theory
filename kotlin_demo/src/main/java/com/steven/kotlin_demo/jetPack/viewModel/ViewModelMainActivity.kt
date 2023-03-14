package com.steven.kotlin_demo.jetPack.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.steven.kotlin_demo.CommonActivity
import com.steven.kotlin_demo.R
import com.steven.kotlin_demo.jetPack.lifeCycle.MyObserver
import kotlinx.android.synthetic.main.activity_view_model_main.*

class ViewModelMainActivity : CommonActivity() {
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model_main)
        lifecycle.addObserver(MyObserver(lifecycle))
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        plusOneBtn.setOnClickListener {
            viewModel.counter++
            refreshCounter()
        }
        refreshCounter()
        // switchMap()
        getUserBtn.setOnClickListener {
            val userId = (0..1000).random().toString()
            viewModel.getUser(userId)
        }
        viewModel.user.observe(this){
            infoText.text = it.firstName
        }

        // <editor-folder desc="map">
        getUserName.setOnClickListener {
            val s = (0..100).random().toString()
            viewModel.getUserName("steven.${s}")
        }
        viewModel.userName.observe(this){
            infoText.text = it
        }
        // </editor-folder>
    }
    private fun refreshCounter() {
        infoText.text = viewModel.counter.toString()
    }
}
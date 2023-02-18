package com.steven.kotlin_demo.recycleView

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.steven.kotlin_demo.R
import com.steven.kotlin_demo.view.Fruit
import kotlinx.android.synthetic.main.activity_recycle_view.*

class RecycleActivity : AppCompatActivity(){
    private val list = ArrayList<Fruit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_view)
        initData()
        val layoutManager = LinearLayoutManager(this)
        val adapter = FruitRecycleAdapter(list)
        recycle_view.layoutManager = layoutManager
        recycle_view.adapter = adapter

    }

    private fun initData() {
        for (i in 0..30){
            list.add(Fruit("apple" +i, R.drawable.ic_launcher_background))
        }
    }
}
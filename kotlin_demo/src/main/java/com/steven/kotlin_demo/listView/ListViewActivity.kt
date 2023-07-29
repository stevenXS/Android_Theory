package com.steven.kotlin_demo.listView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.steven.kotlin_demo.R
import com.steven.kotlin_demo.view.Fruit
import kotlinx.android.synthetic.main.activity_list_view.*

class ListViewActivity : AppCompatActivity() {
    private val fruitList: ArrayList<Fruit> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)
        initData()
        val fruitAdapter = FruitAdapter(this, R.layout.list_view_fruit, fruitList)
        listView.adapter = fruitAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            // 对每个item设置监听器
            val fruit = fruitList[position]
            Toast.makeText(this, "name: ${fruit.name}", Toast.LENGTH_LONG).show()
        }
    }

    fun initData() {
        for (i in 0..30){
            fruitList.add(Fruit("apple" + i, R.drawable.ic_launcher_foreground))
        }
    }

}
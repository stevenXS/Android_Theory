package com.steven.kotlin_demo.view

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : AppCompatActivity() {
    private val data = listOf<String>("aa","bbb","ccc")

    val fruitList = ArrayList<Fruit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        initData()

        // list view
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        list_view.adapter = adapter

        // recycle view
        var layoutManager = LinearLayoutManager(this)
        val adapter_recycle = FruitAdapter(fruitList)
        recycle_view.layoutManager = layoutManager
        recycle_view.adapter = adapter_recycle
    }

    fun initData(){
        repeat(2) {
            fruitList.add(Fruit("Apple", R.drawable.ic_launcher_background))
            fruitList.add(Fruit("Orange", R.drawable.ic_launcher_background))
            fruitList.add(Fruit("Banana", android.R.drawable.ic_dialog_email))
            fruitList.add(Fruit("Pear", android.R.drawable.alert_light_frame))
        }
    }

    /**
     * recycle view
     */

    class FruitAdapter(val list: List<Fruit>): Adapter<FruitAdapter.MyViewHolder>(){

        fun FruitAdapter.toast(context: Context, desc: String){
            Toast.makeText(context, desc, Toast.LENGTH_LONG).show()
        }

        // 初始化每个item的view
        inner class MyViewHolder(view: View): ViewHolder(view){
            val fruitImage : ImageView = view.findViewById(R.id.fruit_image)
            val fruitName : TextView = view.findViewById(R.id.fruit_name)
        }

        // 初始化view holder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_view, parent, false)
            var myViewHolder = MyViewHolder(view)
            myViewHolder.itemView.setOnClickListener{
                var position = myViewHolder.adapterPosition
                val fruit = list[position]
                toast(parent.context,"${fruit.name}")
            }
            return myViewHolder
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val fruit = list[position]
            holder.fruitImage.setImageResource(fruit.resourceId)
            holder.fruitName.setText(fruit.name)
        }
    }

}

class Fruit(val name: String, val resourceId : Int) {
}

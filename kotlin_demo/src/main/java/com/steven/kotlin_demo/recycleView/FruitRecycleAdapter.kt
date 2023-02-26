package com.steven.kotlin_demo.recycleView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.steven.kotlin_demo.R
import com.steven.kotlin_demo.view.Fruit

class FruitRecycleAdapter(val data: List<Fruit>): Adapter<FruitRecycleAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val fruitImage: ImageView = view.findViewById(R.id.fruit_image)
        val fruitName: TextView = view.findViewById(R.id.fruit_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_view_fruit, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = data[position]
        holder.fruitImage.setImageResource(fruit.resourceId)
        holder.fruitName.setText(fruit.name)
        Log.d("bindViewHolder", "${fruit.name}")
    }
}
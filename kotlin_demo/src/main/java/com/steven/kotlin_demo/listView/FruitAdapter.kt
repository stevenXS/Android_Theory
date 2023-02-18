package com.steven.kotlin_demo.listView

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.steven.kotlin_demo.R
import com.steven.kotlin_demo.view.Fruit

// 主构造函数加val or var表示可以当作成员变量来使用
class FruitAdapter(activity:Activity, val resourceId: Int, data: List<Fruit>)
    :ArrayAdapter<Fruit>(activity, resourceId, data) {

    @SuppressLint("MissingInflatedId", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ListViewHolder
        // 基于缓存做优化
        if  (convertView == null){
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val imageView: ImageView = view.findViewById(R.id.fruit_img)
            val textView = view.findViewById<TextView>(R.id.fruit_name)
            viewHolder = ListViewHolder(imageView, textView)
            view.tag = viewHolder
        }else{
            view = convertView // convertView用于缓存加载的布局
            viewHolder = view.tag as ListViewHolder
        }

        val item = getItem(position)
        if (item !=null){
            viewHolder.fruitImg.setImageResource(item.resourceId)
            viewHolder.fruitName.setText(item.name)
        }

        return view
    }

    inner class ListViewHolder(val fruitImg: ImageView, val fruitName: TextView)
}
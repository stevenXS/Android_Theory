package com.steven.kotlin_demo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.frag_news_content.*

class NewsContentFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("NewsContentFragment", "onCreateView")
        return inflater.inflate(R.layout.frag_news_content, container, false);
    }

    fun refreshNews(title: String, content: String){
        layout_content.visibility = View.VISIBLE // 内容布局设置为可见
        news_title.text = title
        news_content.text = content
    }
}
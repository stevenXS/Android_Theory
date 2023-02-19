package com.steven.kotlin_demo.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_news_content.*

class NewsContentActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_content)
        val title = intent.getStringExtra("news_title")
        val content = intent.getStringExtra("news_content")
        if (title != null && content != null){
            val newsContentFragment = frag_news_content as NewsContentFragment
            newsContentFragment.refreshNews(title, content)
        }
    }

    companion object{
        fun actionStart(context: Context, title: String, content: String){
            // 使用了标准扩展函数
            val intent = Intent(context, NewsContentActivity::class.java).apply {
                putExtra("news_title", title)
                putExtra("news_content", content)
            }
            context.startActivity(intent)
        }
    }
}
package com.steven.kotlin_demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_news_main.*
import kotlinx.android.synthetic.main.frag_news_title.*

class NewsTitleFragment: Fragment() {
    private var isTowPane = false;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_news_title, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isTowPane = activity?.findViewById<View>(R.id.layout_news_content) != null
        recycle_news_title.adapter = NewsAdapter(getNewsList())
        recycle_news_title.layoutManager = LinearLayoutManager(activity)
    }

    fun getNewsList(): List<News>{
        val newsList = ArrayList<News>()
        for (i in 1..50){
            val news = News("News Title ${i}", "News Content ########## ${i}")
            newsList.add(news)
        }
        return newsList
    }

    /**
     * RecycleView
     */
    inner class NewsAdapter(val newsList: List<News>): Adapter<NewsAdapter.ViewHolder>(){
        inner class ViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
            val newsTitle: TextView = view.findViewById(R.id.item_news_title)
        }

        /**
         * 创建ViewHolder时判断单页or双叶逻辑
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val news = newsList[holder.adapterPosition]
                if (isTowPane){
                    // 如果是双叶模式，则动态加载内容fragment并刷新数据
                    val fragment = frag_news_content as NewsContentFragment
                    fragment.refreshNews(news.title, news.content)
                }else{
                    // 如果是单页模式，则直接启动NewsContentActivity
                    NewsContentActivity.actionStart(parent.context, news.title, news.content)
                }
            }
            return holder
        }

        override fun getItemCount(): Int {
            return newsList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val news = newsList[position]
            holder.newsTitle.text = news.title
        }
    }
}
package com.steven.fragment_demo.news_demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.steven.fragment_demo.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class FragmentNewsTitle extends Fragment {
    private View view;
    private static boolean isTowPane;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_title, container, false);

        RecyclerView recyclerView =(RecyclerView) view.findViewById(R.id.rv_title);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        NewsAdapter adapter = new NewsAdapter(initNewsData());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public static List<NewsBean> initNewsData(){
        List<NewsBean> newsBeanList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            NewsBean bean = new NewsBean();
            bean.setTitle("新闻标题：" + String.valueOf(i));
            bean.setContent("++++++++++++新闻内容++++++++++：" + String.valueOf(i));
            newsBeanList.add(bean);
        }
        return newsBeanList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.news_content_frame_layout) != null){
            isTowPane = true;
        }else{
            isTowPane = false;
        }
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{
        private List<NewsBean> newsBeanList;

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_title;
            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_title = view.findViewById(R.id.news_item);
            }
        }

        public NewsAdapter(List<NewsBean> newsBeanList) {
            this.newsBeanList = newsBeanList;
        }

        @NonNull
        @Override
        public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View news_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            final MyViewHolder holder = new MyViewHolder(news_view);
            news_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsBean bean = newsBeanList.get(holder.getPosition());
                    if (isTowPane){
                        // 双页模式
                        FragmetNewsContent fragment=(FragmetNewsContent) getFragmentManager().findFragmentById(R.id.news_content_fragment);
                        fragment.refresh(bean.getTitle(), bean.getContent());
                        this.notifyAll();
                    }else{
                        // 单页模式：点击recycleview的item并通过activity跳转
                        ActivitySinglePageNewsContent.actionStart(getContext(), bean.getTitle(), bean.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position) {
            NewsBean bean = newsBeanList.get(position);
            holder.tv_title.setText(bean.getTitle());
        }

        @Override
        public int getItemCount() {
            return newsBeanList.size();
        }

    }
}

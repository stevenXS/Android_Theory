package com.steven.ui_demo.recycleView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.steven.ui_demo.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewholder> {
    private Context context;
    private List<Fruit> fruits = new ArrayList<>();

    public MyAdapter(Context context){
        this.context = context;
        initData();
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.fruit_item, null);
        MyViewholder viewholder = new MyViewholder(inflate);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        Fruit fruit = fruits.get(position);
        holder.tv.setText(fruit.getName());
    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder{
        private TextView tv;
        public MyViewholder(@NonNull View view) {
            super(view);
            tv = view.findViewById(R.id.fruit_name);
        }
    }

    public void initData(){
        for (int i = 0; i < 20; i++) {
            Fruit fruit = new Fruit("a" + String.valueOf(i), 1);
            fruits.add(fruit);
        }
    }
}

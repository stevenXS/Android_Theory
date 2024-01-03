package com.example.design_pattern.composite;

import android.util.Log;

public class Department extends Component{
    private String name;
    private String desc;

    public Department(String name, String desc) {
        super(name);
        this.name = name;
        this.desc = desc;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void print() {
        Log.d("aaa###", "----" + getName() + "----");
    }
}

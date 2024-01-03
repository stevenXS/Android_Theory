package com.example.design_pattern.composite;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class University extends Component{
    private final List<Component> list = new ArrayList<>();

    public University(String name) {
        super(name);
    }

    @Override
    public void add(Component component) {
        list.add(component);
    }

    @Override
    public void remove(Component component) {
        list.remove(component);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void print() {
        Log.d("aaa###", "------------" + getName() + "---------------");
        for (Component component : list) {
            component.print();
        }
    }
}

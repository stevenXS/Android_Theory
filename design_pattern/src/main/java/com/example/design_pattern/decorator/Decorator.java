package com.example.design_pattern.decorator;

import android.util.Log;

public class Decorator extends Component{
    private Component obj;

    public Decorator(Component component) {
        this.obj = component;
    }

    @Override
    public int cost() {
        return super.getPrice() +obj.cost();
    }

    @Override
    public String getDesc() {
        return super.getDesc() + ", 被装饰者的描述信息 " + obj.getDesc();
    }
}

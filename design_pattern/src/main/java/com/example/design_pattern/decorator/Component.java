package com.example.design_pattern.decorator;

public abstract class Component {
    private int price;
    private String desc;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public abstract int cost();
}

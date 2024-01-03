package com.example.design_pattern.composite;

public abstract class Component {
    protected String name;

    public Component(String name) {
        this.name = name;
    }

    public void add(Component component){

    }

    public  void remove(Component component) {

    }

    public String getName() {
        return name;
    }

    public abstract void print();
}

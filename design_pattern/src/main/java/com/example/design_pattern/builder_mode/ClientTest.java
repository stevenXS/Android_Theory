package com.example.design_pattern.builder_mode;

public class ClientTest {
    public static void main(String[] args) {
        NyPizza.Builder pizza = new NyPizza.Builder(NyPizza.Size.SMALL);
        Calzone calzone = new Calzone.Builder().addTopping(Pizza.Topping.HAM).sauceInside().build();
    }
}

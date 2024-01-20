package com.example.design_pattern.decorator;

import com.example.design_pattern.decorator.CoffeeCom;

public class Express extends CoffeeCom {
    public Express() {
        this.setDesc("espresso coffee");
        this.setPrice(15);
    }
}

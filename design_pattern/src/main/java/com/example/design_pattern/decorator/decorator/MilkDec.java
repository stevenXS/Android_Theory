package com.example.design_pattern.decorator.decorator;

import com.example.design_pattern.decorator.Component;
import com.example.design_pattern.decorator.decorator.Decorator;

public class MilkDec extends Decorator {
    public MilkDec(Component component) {
        super(component);
        this.setDesc("调味品：牛奶");
        this.setPrice(11);
    }
}

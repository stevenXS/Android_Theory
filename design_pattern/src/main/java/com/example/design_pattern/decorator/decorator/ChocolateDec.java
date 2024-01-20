package com.example.design_pattern.decorator.decorator;

import com.example.design_pattern.decorator.Component;
import com.example.design_pattern.decorator.decorator.Decorator;

public class ChocolateDec extends Decorator {
    public ChocolateDec(Component component) {
        super(component);
        this.setDesc("调味品：巧克力");
        this.setPrice(21);
    }
}

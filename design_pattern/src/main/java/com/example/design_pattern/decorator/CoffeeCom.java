package com.example.design_pattern.decorator;

public class CoffeeCom extends Component{

    @Override
    public int cost() {
        return this.getPrice();
    }
}

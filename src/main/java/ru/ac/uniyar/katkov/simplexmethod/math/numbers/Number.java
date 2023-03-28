package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public abstract class Number {
    public Number(){};
    public Number(String s){
        parse(s);
    }
    protected abstract void parse(String s);
}

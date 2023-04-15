package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.Doubl.D;

public class DoublArithmetic implements Arithmetic<Doubl>{
    public static final DoublArithmetic instance = new DoublArithmetic();
    private DoublArithmetic(){}
    @Override
    public Doubl plus(Doubl a, Doubl b) {
        return D(a.value+b.value);
    }

    @Override
    public Doubl minus(Doubl a, Doubl b) {
        return D(a.value-b.value);
    }

    @Override
    public Doubl multiply(Doubl a, Doubl b) {
        return D(a.value*b.value);
    }

    @Override
    public Doubl divide(Doubl a, Doubl b) {
        if(b.value==0) throw new ArithmeticException("Division by zero");
        return D(a.value/b.value);
    }

    @Override
    public Doubl revert(Doubl a) {
        return D(-a.value);
    }

    @Override
    public Doubl flip(Doubl a) {
        if(a.value==0) throw new ArithmeticException("Division by zero");
        return D(1/a.value);
    }

    @Override
    public Doubl zero() {
        return D(0);
    }

    @Override
    public Doubl parse(String s) {
        return D(Double.parseDouble(s));
    }

    @Override
    public String toString() {
        return "D";
    }
}

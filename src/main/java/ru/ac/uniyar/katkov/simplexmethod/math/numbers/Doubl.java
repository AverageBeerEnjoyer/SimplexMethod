package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public class Doubl extends Number implements Num<Doubl>{
    private final static double eps = 0.000001;
    public static final Doubl ZERO = D(0);
    private java.lang.Double value;
    public Doubl(double value){
        this.value = value;
    }
    public Doubl(String s){
        super(s);
    }

    @Override
    protected void parse(String s) {
        this.value = Double.parseDouble(s);
    }

    public static Doubl D(double value){
        return new Doubl(value);
    }

    @Override
    public Doubl plus(Doubl a) {
        return D(value+a.value);
    }

    @Override
    public Doubl minus(Doubl a) {
        return D(value-a.value);
    }

    @Override
    public Doubl multiply(Doubl a) {
        return D(value*a.value);
    }

    @Override
    public Doubl divide(Doubl a) {
        return D(value/a.value);
    }


    @Override
    public Doubl revert() {
        return D(-value);
    }

    @Override
    public Doubl flip() {
        if(value == 0) throw new ArithmeticException("Division by zero");
        return D(1/value);
    }

    @Override
    public boolean isZero() {
        return Math.abs(value) < eps;
    }

    @Override
    public Doubl zero() {
        return D(0);
    }


    @Override
    public int compareTo(Doubl doubl) {
        return Double.compare(value,doubl.value);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Doubl)) return false;
        return value - ((Doubl) obj).value <eps;

    }

    public Double getValue() {
        return value;
    }
}

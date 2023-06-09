package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public class Doubl extends Number implements Cloneable, Comparable<Doubl>{
    public final static double eps = 0.000001;
    public final Double value;
    public Doubl(double value){
        this.value = value;
    }


    public static Doubl D(double value){
        return new Doubl(value);
    }
    @Override
    public double doubleValue() {
        return value;
    }


    @Override
    public int compareTo(Doubl doubl) {
        return Double.compare(value,doubl.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        return D(value);
    }
}

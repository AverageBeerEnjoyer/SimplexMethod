package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public class Doubl implements Num<Doubl>{
    private final static double eps = 0.000001;
    public final Double value;
    public Doubl(double value){
        this.value = value;
    }


    public static Doubl D(double value){
        return new Doubl(value);
    }




    @Override
    public boolean isZero() {
        return Math.abs(value) < eps;
    }


    @Override
    public int compareTo(Doubl doubl) {
        return Double.compare(value,doubl.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public class OrdinaryFraction extends Number implements Cloneable, Comparable<OrdinaryFraction> {
    public static final OrdinaryFraction ZERO = OF(0, 1);

    public final int numerator;
    public final int denominator;



    public OrdinaryFraction(int numerator, int denominator) {
        int scm = Euclid.scm(numerator, denominator);
        this.numerator = numerator / scm;
        this.denominator = denominator / scm;
    }

    public static OrdinaryFraction OF(int numerator, int denominator) {
        return new OrdinaryFraction(numerator, denominator);
    }

    @Override
    public double doubleValue() {
        return (double) numerator/denominator;
    }


    @Override
    public int compareTo(OrdinaryFraction o) {
        int res = numerator*o.denominator - o.numerator*denominator;
        return Integer.compare(res, 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrdinaryFraction)) return false;
        return (this.numerator == ((OrdinaryFraction) obj).numerator && this.denominator == ((OrdinaryFraction) obj).denominator);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(numerator);
        if(denominator!=1){
            sb.append("/").append(denominator);
        }
        return sb.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        return OF(numerator,denominator);
    }
}

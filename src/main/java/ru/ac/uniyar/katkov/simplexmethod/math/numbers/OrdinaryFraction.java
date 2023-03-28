package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public class OrdinaryFraction extends Number implements Num<OrdinaryFraction> {
    private static final String regex = "(-?[1-9][0-9]{0,8}|0)(/?[1-9][0-9]{0,9})?";
    public static final OrdinaryFraction ZERO = OF(0, 1);

    private int numerator;
    private int denominator;

    public OrdinaryFraction(String s){
        super(s);
    }


    public OrdinaryFraction(int numerator, int denominator) {
        int scm = Euclid.scm(numerator, denominator);
        this.numerator = numerator / scm;
        this.denominator = denominator / scm;
    }

    public static OrdinaryFraction OF(int numerator, int denominator) {
        return new OrdinaryFraction(numerator, denominator);
    }

    @Override
    public OrdinaryFraction plus(OrdinaryFraction b) {
        int n = this.numerator * b.denominator + b.numerator * this.denominator;
        int d = this.denominator * b.denominator;
        return new OrdinaryFraction(n, d);
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public OrdinaryFraction minus(OrdinaryFraction b) {
        return plus(b.revert());
    }

    public OrdinaryFraction multiply(OrdinaryFraction b) {
        int n = this.numerator * b.numerator;
        int d = this.denominator * b.denominator;
        return new OrdinaryFraction(n, d);
    }


    @Override
    public OrdinaryFraction divide( OrdinaryFraction b) {
        return this.multiply(b.flip());
    }

    public OrdinaryFraction revert() {
        return new OrdinaryFraction(-this.numerator, this.denominator);
    }

    public OrdinaryFraction flip() {
        OrdinaryFraction tmp = OF(numerator, denominator);
        if (tmp.numerator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        boolean negative = tmp.numerator < 0;
        if (negative) {
            tmp = tmp.revert();
        }
        tmp = new OrdinaryFraction(tmp.denominator, tmp.numerator);
        if (negative) {
            tmp = tmp.revert();
        }
        return tmp;
    }

    @Override
    public boolean isZero() {
        return numerator == 0;
    }

    @Override
    public OrdinaryFraction zero() {
        return ZERO;
    }

    @Override
    public int compareTo(OrdinaryFraction o) {
        OrdinaryFraction res = this.minus(o);
        return Integer.compare(res.numerator, 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrdinaryFraction)) return false;
        return (this.numerator == ((OrdinaryFraction) obj).numerator && this.denominator == ((OrdinaryFraction) obj).denominator);
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    protected void parse(String value) {
        if (!value.matches(regex)) {
            throw new NumberFormatException();
        }
        String[] s = value.split("/");
        numerator = Integer.parseInt(s[0]);
        if (s.length > 1) {
            denominator = Integer.parseInt(s[1]);
        } else {
            denominator = 1;
        }
    }
}

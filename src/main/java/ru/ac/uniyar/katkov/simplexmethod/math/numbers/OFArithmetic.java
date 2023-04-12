package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public class OFArithmetic implements Arithmetic<OrdinaryFraction> {
    public static final OFArithmetic instance = new OFArithmetic();
    private static final String regex = "(-?[1-9][0-9]{0,8}|0)(/?[1-9][0-9]{0,9})?";
    public static final OrdinaryFraction ZERO = OF(0, 1);

    public static OrdinaryFraction OF(int numerator, int denominator) {
        return new OrdinaryFraction(numerator, denominator);
    }

    @Override
    public OrdinaryFraction plus(OrdinaryFraction a, OrdinaryFraction b) {
        int n = a.numerator * b.denominator + b.numerator * a.denominator;
        int d = a.denominator * b.denominator;
        return new OrdinaryFraction(n, d);
    }

    public OrdinaryFraction minus(OrdinaryFraction a, OrdinaryFraction b) {
        return plus(a, revert(b));
    }

    public OrdinaryFraction multiply(OrdinaryFraction a, OrdinaryFraction b) {
        int n = a.numerator * b.numerator;
        int d = a.denominator * b.denominator;
        return new OrdinaryFraction(n, d);
    }


    @Override
    public OrdinaryFraction divide(OrdinaryFraction a, OrdinaryFraction b) {
        return multiply(a, flip(b));
    }

    public OrdinaryFraction revert(OrdinaryFraction a) {
        return new OrdinaryFraction(-a.numerator, a.denominator);
    }

    public OrdinaryFraction flip(OrdinaryFraction a) {
        if (a.numerator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        OrdinaryFraction tmp;
        boolean negative = a.numerator < 0;
        if (negative) {
            tmp = revert(a);
        } else {
            tmp = a;
        }
        tmp = new OrdinaryFraction(tmp.denominator, tmp.numerator);
        if (negative) {
            tmp = revert(tmp);
        }
        return tmp;
    }

    @Override
    public OrdinaryFraction zero() {
        return ZERO;
    }

    @Override
    public OrdinaryFraction parse(String value) {
        int numerator, denominator;
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
        return new OrdinaryFraction(numerator, denominator);
    }

    @Override
    public String toString() {
        return "OF";
    }
}

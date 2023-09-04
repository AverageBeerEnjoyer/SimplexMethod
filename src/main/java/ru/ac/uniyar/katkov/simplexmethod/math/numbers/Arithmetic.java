package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public abstract class Arithmetic<T extends Number> {

    @SuppressWarnings("unchecked")
    public static <T extends Number> Arithmetic<T> getArithmeticOfType(T value) {
        if (value instanceof OrdinaryFraction) {
            return (Arithmetic<T>) OFArithmetic.instance;
        } else return (Arithmetic<T>) DoublArithmetic.instance;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> Arithmetic<T> parseArithmetic(String s) {
        return switch (s) {
            case "OF" -> (Arithmetic<T>) OFArithmetic.instance;
            case "D" -> (Arithmetic<T>) DoublArithmetic.instance;
            default -> throw new IllegalArgumentException();
        };
    }

    public abstract boolean isZero(T a);

    public abstract T plus(T a, T b);

    public abstract T minus(T a, T b);

    public abstract T multiply(T a, T b);

    public abstract T divide(T a, T b);

    public abstract T revert(T a);

    public abstract T flip(T a);

    public abstract T zero();

    public abstract T parse(String s);

    public abstract T[] emptyArray(int n);

    public abstract T[][] empty2DimArray(int n, int m);

    public abstract int compare(T a, T b);

    public abstract T cast(int n);
}

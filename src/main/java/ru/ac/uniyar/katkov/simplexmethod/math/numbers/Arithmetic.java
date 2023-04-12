package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public interface Arithmetic<T extends Num<T>> {
    @SuppressWarnings("unchecked")
    static <T extends Num<T>> Arithmetic<T> getArithmeticOfType(T value) {
        if (value instanceof OrdinaryFraction) {

            return (Arithmetic<T>) OFArithmetic.instance;
        } else return (Arithmetic<T>) DoublArithmetic.instance;
    }
    @SuppressWarnings("unchecked")
    static <T extends Num<T>> Arithmetic<T> parseArithmetic(String s){
        return switch (s) {
            case "OF" -> (Arithmetic<T>) OFArithmetic.instance;
            case "D" -> (Arithmetic<T>) DoublArithmetic.instance;
            default -> throw new IllegalArgumentException();
        };
    }

    T plus(T a, T b);

    T minus(T a, T b);

    T multiply(T a, T b);

    T divide(T a, T b);

    T revert(T a);

    T flip(T a);

    T zero();

    T parse(String s);
}

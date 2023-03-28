package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public interface Num<T> extends Comparable<T> {
    public T plus(T a);
    public T minus(T a);
    public T multiply(T a);
    public T divide(T a);
    public T revert();
    public T flip();
    public boolean isZero();
    public T zero();
}

package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

public class Euclid {

    public static int scm(int a, int b) {
        if (a == 0 && b == 0) return 1;
        a = Math.abs(a);
        b = Math.abs(b);
        if (b > a) {
            int x = b;
            b = a;
            a = x;
        }
        while (b != 0) {
            int x = a%b;
            a = b;
            b = x;
        }
        return a;
    }
}

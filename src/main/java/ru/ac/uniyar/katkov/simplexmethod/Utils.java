package ru.ac.uniyar.katkov.simplexmethod;

import javafx.util.Pair;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Arithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Number;


import java.util.List;
import java.util.NoSuchElementException;

public class Utils {
    public static <T extends Number> int findPosOfMinEl(T[] ar, Arithmetic<T> ametic) {
        T min;
        min = ar[0];
        int col = 0;
        for (int i = 1; i < ar.length; ++i) {
            if (ametic.compare(ar[i],min) < 0) {
                min = ar[i];
                col = i;
            }
        }
        return col;
    }

    public static <T extends Comparable<T>> int findPosOfMaxEl(T[] ar) {
        T max;
        max = ar[0];
        int col = 0;
        for (int i = 1; i < ar.length; ++i) {
            if (ar[i].compareTo(max) > 0) {
                max = ar[i];
                col = i;
            }
        }
        return col;
    }

    public static <T> T getlast(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static int indexOf(int[] array, int obj) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == obj) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    public static <T> Pair<T,T> pair(T arg1,T arg2){
        return new Pair<>(arg1,arg2);
    }
}

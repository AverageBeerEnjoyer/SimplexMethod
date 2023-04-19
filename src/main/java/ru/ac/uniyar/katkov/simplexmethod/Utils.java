package ru.ac.uniyar.katkov.simplexmethod;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.List;
import java.util.NoSuchElementException;

public class Utils {
    public static <T extends Comparable<T>> int findPosOfMinEl(T[] ar) {
        T min;
        min = ar[0];
        int col = 0;
        for (int i = 1; i < ar.length; ++i) {
            if (ar[i].compareTo(min) < 0) {
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

    public static <T> T[][] Empty2DimArray(Class<T> tClass, int rows, int cols) {
        T[][] newArray = (T[][]) Array.newInstance(tClass.arrayType(), rows);
        for (int i = 0; i < rows; ++i){
            newArray[i] = (T[]) Array.newInstance(tClass,cols);
        }
        return newArray;
    }

    public static <T> Pair<T,T> pair(T arg1,T arg2){
        return new Pair<>(arg1,arg2);
    }
}

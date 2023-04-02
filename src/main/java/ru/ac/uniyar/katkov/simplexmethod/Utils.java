package ru.ac.uniyar.katkov.simplexmethod;

public class Utils {
    public static <T extends Comparable<T>> int findPosOfMinEl(T[] ar){
        T min;
        min = ar[0];
        int col=0;
        for(int i=1;i<ar.length;++i){
            if(ar[i].compareTo(min)<0){
                min = ar[i];
                col = i;
            }
        }
        return col;
    }
    public static <T extends Comparable<T>> int findPosOfMaxEl(T[] ar){
        T max;
        max = ar[0];
        int col=0;
        for(int i=1;i<ar.length;++i){
            if(ar[i].compareTo(max)>0){
                max = ar[i];
                col = i;
            }
        }
        return col;
    }
}

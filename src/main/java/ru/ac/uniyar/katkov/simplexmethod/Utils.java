package ru.ac.uniyar.katkov.simplexmethod;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction;

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
    public static Node getFromGridRowCol(int row, int col, GridPane gridPane){
        for(Node child: gridPane.getChildren()){
            if(GridPane.getRowIndex(child)==row && GridPane.getColumnIndex(child)==col)
                return child;
        }
        return null;
    }
}

package ru.ac.uniyar.katkov.simplexmethod.math.simplex.table;

import javafx.util.Pair;
import ru.ac.uniyar.katkov.simplexmethod.math.GaussMethod;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;

import java.util.Arrays;

public class SimplexTableABM<T extends Num<T>> extends SimplexTable<T> {
    public SimplexTableABM(T[] func, Matrix<T> matrix) {
        super(func, matrix);
    }

    @Override
    protected void basicVariables() {
        GaussMethod<T> gaussMethod = new GaussMethod<>();
        gaussMethod.solve(matrix);
    }

    @Override
    public SimplexTable<T> next() {
        Pair<Integer,Integer> elementToSwap= choseSwapElements();
        matrix.swapColumns(elementToSwap.getKey(),elementToSwap.getValue());
        matrix.swapColumns(elementToSwap.getValue(), matrix.columns-1);

        return createNextTable();
    }

    private SimplexTable<T> createNextTable(){
        T[][] newNumbers = Arrays.copyOf(matrix.getNumbers(),matrix.rows);
        T[] newExtension = Arrays.copyOf(matrix.getExtension(),matrix.rows);
        T[] f = Arrays.copyOf(func,func.length-1);
        f[f.length-1] = func[func.length-1];
        for(int i=0;i< matrix.columns;++i){
            newNumbers[i] = Arrays.copyOf(matrix.getNumbers()[i],matrix.columns-1);
        }
        return new SimplexTableABM<>(f,new Matrix<>(newNumbers,newExtension));
    }

    @Override
    protected Pair<Integer, Integer> choseSwapElements() {
        int rowToSwap = 0, colToSwap = 0;
        for(int i=0;i< matrix.rows;++i){
            if(matrix.getOrder()[i]> matrix.columns- matrix.rows-1){
                rowToSwap = i;
            }
        }
        for(int i = matrix.rows;i< matrix.columns;++i){
            if(matrix.get(rowToSwap,i).compareTo(ametic.zero())>0){
                colToSwap = i;
                break;
            }
        }
        return new Pair<>(rowToSwap,colToSwap);
    }
}

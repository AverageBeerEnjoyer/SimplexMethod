package ru.ac.uniyar.katkov.simplexmethod.math.simplex;

import javafx.util.Pair;
import ru.ac.uniyar.katkov.simplexmethod.math.GaussMethod;
import ru.ac.uniyar.katkov.simplexmethod.Utils;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SimplexTable<T extends Num<T>> {
    private final T[] func;
    private final Matrix<T> matrix;
    private List<T> vector;
    private SimplexTableCondition condition;

    public SimplexTable(T[] func, Matrix<T> matrix) {
        this.func = func;
        this.matrix = matrix;
        basicVariables();
        countTargetFunction();
        countVector();
        defineCondition();
    }

    private void basicVariables() {
        GaussMethod<T> gauss = new GaussMethod<>();
        gauss.solveWithColSwaps(matrix);
    }

    private void countTargetFunction() {
        int[] order = matrix.getOrder();
        for (int i = 0; i < matrix.rows; ++i) {
            for (int j = matrix.rows; j < matrix.columns; ++j) {
                func[order[j]] = func[order[j]].minus(func[order[i]].multiply(matrix.get(i, j)));
            }
            func[matrix.columns] = func[matrix.columns].minus(func[order[i]].multiply(matrix.getExt(i)));
            func[order[i]] = func[order[i]].zero();

        }
    }

    private void countVector() {
        this.vector = new ArrayList<>(matrix.columns);
        for(int i = 0;i<matrix.columns;++i){
            vector.add(matrix.get(0,0).zero());
        }
        for (int i = 0; i < matrix.rows; ++i) {
            vector.set(matrix.getOrder()[i],matrix.getExt(i));
        }
        for (int i = matrix.rows; i < matrix.columns; ++i) {
            vector.set(matrix.getOrder()[i],matrix.get(0,0).zero());
        }
    }

    private void defineCondition() {
        boolean noLimit = false;
        boolean finalTable = true;
        int[] order = matrix.getOrder();
        for (int i = matrix.rows; i < matrix.columns; ++i) {
            if (func[order[i]].compareTo(func[order[i]].zero()) < 0) {
                finalTable = false;
                for (int j = 0; j < matrix.rows; ++j) {
                    if (matrix.get(j, i).compareTo(matrix.get(j,i).zero()) > 0) {
                        noLimit = false;
                        break;
                    }
                    noLimit = true;
                }
                if (noLimit) break;
            }
        }
        if (finalTable) {
            condition = SimplexTableCondition.FINAL;
            return;
        }
        if (noLimit) {
            condition = SimplexTableCondition.NO_LIMIT;
            return;
        }
        condition = SimplexTableCondition.NOT_FINAL;
    }

    public Optional<List<T>> getSolution() {
        if (condition == SimplexTableCondition.FINAL) {
            return Optional.of(vector);
        }
        return Optional.empty();
    }

    public SimplexTable<T> next() {
        if (condition != SimplexTableCondition.NOT_FINAL) return this;
        T[] function = func.clone();
        Matrix<T> matrix = this.matrix.clone();
        Pair<Integer, Integer> swap = choseSwapElements();
        matrix.swapColumns(swap.getKey(), swap.getValue());
        return new SimplexTable<T>(function, matrix);
    }

    private int choseRowToSwap(int colToSwap) {
        int rowToSwap = -1;
        T max = matrix.get(0,0).zero();
        for (int i = 0; i < matrix.rows; ++i) {
            T el = matrix.get(i, colToSwap);
            if (el.compareTo(el.zero()) < 0) continue;
            T ext = matrix.getExt(i);
            if (ext.isZero()) return i;
            el = el.divide(ext);
            if (el.compareTo(max) > 0) {
                max = el;
                rowToSwap = i;
            }
        }
        return rowToSwap;
    }

    private Pair<Integer, Integer> choseSwapElements() {
        int colToSwap = Utils.findPosOfMinEl(Arrays.copyOf(func, func.length - 1));
        int rowToSwap = choseRowToSwap(colToSwap);
        return new Pair<>(rowToSwap, colToSwap);
    }

    public SimplexTableCondition getCondition() {
        return condition;
    }
}

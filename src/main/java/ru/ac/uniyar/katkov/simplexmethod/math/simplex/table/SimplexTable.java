package ru.ac.uniyar.katkov.simplexmethod.math.simplex.table;

import javafx.util.Pair;
import ru.ac.uniyar.katkov.simplexmethod.math.GaussMethod;
import ru.ac.uniyar.katkov.simplexmethod.Utils;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Arithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SimplexTableCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SimplexTable<T extends Num<T>> {
    protected Arithmetic<T> ametic;
    protected final T[] func;
    protected final Matrix<T> matrix;
    protected List<T> vector;
    protected SimplexTableCondition condition;

    public SimplexTable(T[] func, Matrix<T> matrix) {
        this.ametic = Arithmetic.getArithmeticOfType(func[0]);
        this.func = func;
        this.matrix = matrix;
        basicVariables();
        countTargetFunction();
        countVector();
        defineCondition();
    }

    protected void basicVariables() {
        GaussMethod<T> gauss = new GaussMethod<>();
        gauss.solveWithColSwaps(matrix);
    }

    private void countTargetFunction() {
        int[] order = matrix.getOrder();
        for (int i = 0; i < matrix.rows; ++i) {
            for (int j = matrix.rows; j < matrix.columns; ++j) {
                func[order[j]] = ametic.minus(func[order[j]], ametic.multiply(func[order[i]],matrix.get(i, j)));
            }
            func[matrix.columns] = ametic.minus(func[matrix.columns], ametic.multiply(func[order[i]], matrix.getExt(i)));
            func[order[i]] = ametic.zero();
        }
    }

    private void countVector() {
        this.vector = new ArrayList<>(matrix.columns);
        for(int i = 0;i<matrix.columns;++i){
            vector.add(ametic.zero());
        }
        for (int i = 0; i < matrix.rows; ++i) {
            vector.set(matrix.getOrder()[i],matrix.getExt(i));
        }
        for (int i = matrix.rows; i < matrix.columns; ++i) {
            vector.set(matrix.getOrder()[i],ametic.zero());
        }
    }

    protected void defineCondition() {
        boolean noLimit = false;
        boolean finalTable = true;
        int[] order = matrix.getOrder();
        for (int i = matrix.rows; i < matrix.columns; ++i) {
            if (func[order[i]].compareTo(ametic.zero()) < 0) {
                finalTable = false;
                for (int j = 0; j < matrix.rows; ++j) {
                    if (matrix.get(j, i).compareTo(ametic.zero()) > 0) {
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
        return new SimplexTable<>(function, matrix);
    }
    private int choseRowToSwap(int colToSwap) {
        int rowToSwap = -1;
        T max = ametic.zero();
        for (int i = 0; i < matrix.rows; ++i) {
            T el = matrix.get(i, colToSwap);
            if (el.compareTo(ametic.zero()) < 0) continue;
            T ext = matrix.getExt(i);
            if (ext.isZero()) return i;
            el = ametic.divide(el, ext);
            if (el.compareTo(max) > 0) {
                max = el;
                rowToSwap = i;
            }
        }
        return rowToSwap;
    }
    private int choseColToSwap(){
        return Utils.indexOf(matrix.getOrder(),Utils.findPosOfMinEl(Arrays.copyOf(func,func.length-1)));
    }
    protected Pair<Integer, Integer> choseSwapElements() {
        int colToSwap = choseColToSwap();
        int rowToSwap = choseRowToSwap(colToSwap);
        return new Pair<>(rowToSwap, colToSwap);
    }

    public SimplexTableCondition getCondition() {
        return condition;
    }
    public T getFunctionValue(){
        return func[func.length-1];
    }

    /**
     * for ABM tasks
     * @return true if there are artificial variables in current basis
     */
    public boolean isArtificialVarsInBasis(){
        for(int i=0;i< matrix.rows;++i){
            if(matrix.getOrder()[i]>= matrix.columns-matrix.rows){
                return true;
            }
        }
        return false;
    }

    public Matrix<T> getCloneMatrix(){
        return matrix.clone();
    }
    public T[] getCloneFunc(){
        return func.clone();
    }
}

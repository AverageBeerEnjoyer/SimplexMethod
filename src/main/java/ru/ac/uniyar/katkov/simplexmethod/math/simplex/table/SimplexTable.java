package ru.ac.uniyar.katkov.simplexmethod.math.simplex.table;

import javafx.util.Pair;
import ru.ac.uniyar.katkov.simplexmethod.math.GaussMethod;
import ru.ac.uniyar.katkov.simplexmethod.Utils;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Arithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Number;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SimplexTableCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SwapAbility;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ru.ac.uniyar.katkov.simplexmethod.Utils.pair;

public class SimplexTable<T extends Number> {

    private Task<T> task;
    public Arithmetic<T> ametic;
    protected final T[] func;
    protected final Matrix<T> matrix;
    protected List<T> vector;
    protected SimplexTableCondition condition;
    protected SwapAbility[][] swapAbility;

    private Pair<Integer, Integer> swapElement;

    public SimplexTable(T[] func, Matrix<T> matrix, Task<T> task) {
        this.task = task;
        this.ametic = matrix.ametic;
        this.func = func;
        this.matrix = matrix;
        basicVariables();
        countTargetFunction();
        countVector();
        defineCondition();
        swapElement = choseSwapElements();
        countSwapAbility();
    }

    protected void basicVariables() {
        GaussMethod<T> gauss = new GaussMethod<>(ametic);
        gauss.solveWithColSwaps(matrix);
    }

    private void countTargetFunction() {
        int[] order = matrix.getOrder();
        for (int i = 0; i < matrix.rows; ++i) {
            for (int j = matrix.rows; j < matrix.columns; ++j) {
                func[order[j]] = ametic.minus(func[order[j]], ametic.multiply(func[order[i]], matrix.get(i, j)));
            }
            func[matrix.columns] = ametic.plus(func[matrix.columns], ametic.multiply(func[order[i]], matrix.getExt(i)));
            func[order[i]] = ametic.cast(0);
        }
    }

    private void countVector() {
        this.vector = new ArrayList<>(matrix.columns);
        for (int i = 0; i < matrix.columns; ++i) {
            vector.add(ametic.cast(0));
        }
        for (int i = 0; i < matrix.rows; ++i) {
            vector.set(matrix.getOrder()[i], matrix.getExt(i));
        }
        for (int i = matrix.rows; i < matrix.columns; ++i) {
            vector.set(matrix.getOrder()[i], ametic.cast(0));
        }
    }

    protected void defineCondition() {
        boolean noLimit = false;
        boolean finalTable = true;
        int[] order = matrix.getOrder();
        for (int i = matrix.rows; i < matrix.columns; ++i) {
            if (ametic.compare(func[order[i]], ametic.cast(0)) < 0) {
                finalTable = false;
                for (int j = 0; j < matrix.rows; ++j) {
                    if (ametic.compare(matrix.get(j, i), ametic.cast(0)) > 0) {
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

    public SimplexTable<T> next(Pair<Integer, Integer> swapElement) {
        if (ametic.isZero(matrix.get(swapElement.getKey(), swapElement.getValue()))) {
            throw new ArithmeticException("You can not choose 0 as swap element");
        }
        if (condition != SimplexTableCondition.NOT_FINAL) return this;
        T[] function = func.clone();
        Matrix<T> matrix = this.matrix.clone();
        matrix.swapColumns(swapElement.getKey(), swapElement.getValue());
        return new SimplexTable<>(function, matrix, task);
    }

    public SimplexTable<T> next() {
        return next(swapElement);
    }

    private void countSwapAbility() {
        int[] order = matrix.getOrder();
        swapAbility = new SwapAbility[matrix.rows][matrix.columns - matrix.rows];
        for (int i = 0; i < matrix.rows; ++i) {
            Arrays.fill(swapAbility[i], SwapAbility.NO);
        }
        if(condition != SimplexTableCondition.NOT_FINAL) return;
        for (int i = matrix.rows; i < matrix.columns; ++i) {
            boolean notNegative = ametic.compare(func[order[i]], ametic.cast(0)) != -1;
            if (notNegative) continue;
            int row = choseRowToSwap(i);
            if(row == -1) continue;
            swapAbility[row][i-matrix.rows] = SwapAbility.YES;
        }
        swapAbility[swapElement.getKey()][swapElement.getValue()-matrix.rows] = SwapAbility.BEST;
    }

    public SwapAbility getSwapAbility(int i, int j) {
        return swapAbility[i][j];
    }

    private int choseRowToSwap(int colToSwap) {
        int rowToSwap = -1;
        T max = ametic.cast(0);
        for (int i = 0; i < matrix.rows; ++i) {
            T el = matrix.get(i, colToSwap);
            if (ametic.compare(el, ametic.cast(0)) < 0) continue;
            T ext = matrix.getExt(i);
            if (ametic.isZero(ext)) return i;
            el = ametic.divide(el, ext);
            if (ametic.compare(el, max) > 0) {
                max = el;
                rowToSwap = i;
            }
        }
        return rowToSwap;
    }

    private int choseColToSwap() {
        return Utils.indexOf(matrix.getOrder(), Utils.findPosOfMinEl(Arrays.copyOf(func, func.length - 1), ametic));
    }

    protected Pair<Integer, Integer> choseSwapElements() {
        int colToSwap = choseColToSwap();
        int rowToSwap = choseRowToSwap(colToSwap);
        return pair(rowToSwap, colToSwap);
    }

    public String getVectorString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < vector.size(); ++i) {
            sb.append(vector.get(i));
            if (i != vector.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public SimplexTableCondition getCondition() {
        return condition;
    }

    public T getFunctionValue() {
        return func[func.length - 1];
    }

    /**
     * for ABM tasks
     *
     * @return true if there are artificial variables in current basis
     */
    public boolean isArtificialVarsInBasis() {
        for (int i = 0; i < matrix.rows; ++i) {
            if (matrix.getOrder()[i] >= matrix.columns - matrix.rows) {
                return true;
            }
        }
        return false;
    }

    public Matrix<T> getCloneMatrix() {
        return matrix.clone();
    }

    public T[] getCloneFunc() {
        return func.clone();
    }

    public Pair<Integer, Integer> getSwapElement() {
        return swapElement;
    }

    public List<String> getUnequals() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < matrix.rows; ++i) {
            StringBuilder sb = new StringBuilder();
            for (int j = matrix.rows; j < matrix.columns; ++j) {
                T el = matrix.get(i, j);
                switch (ametic.compare(el, ametic.cast(0))) {
                    case -1 -> {
                        if (j != matrix.rows) {
                            sb.append(" + ");
                        }
                        sb.append(ametic.revert(el));
                        sb.append(" * ");
                        sb.append("x").append(matrix.getOrder()[j] + 1);
                    }
                    case 1 -> {
                        sb.append(" - ");
                        sb.append(el);
                        sb.append(" * ");
                        sb.append("x").append(matrix.getOrder()[j] + 1);
                    }
                    default -> {
                    }
                }
            }
            T el = matrix.getExt(i);
            switch (ametic.compare(el, ametic.cast(0))) {
                case -1 -> {
                    sb.append(" - ");
                    sb.append(ametic.revert(el));
                }
                case 1 -> {
                    sb.append(" + ");
                    sb.append(el);
                }
                default -> {
                }
            }
            sb.append(" >= 0");
            res.add(sb.toString());
        }
        return res;
    }

    public Matrix<T> getMatrix() {
        return matrix;
    }

    public Task<T> getTask() {
        return task;
    }
}

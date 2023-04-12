package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ru.ac.uniyar.katkov.simplexmethod.Utils;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.TaskABM;

import java.io.StringReader;
import java.lang.reflect.Array;

public class TaskParser {

    public static <T extends Num<T>> Task<T> createTaskWithChosenBasisFromGrid(Arithmetic<T> ametic, GridPane gridPane, int[] order, int rows, int cols) {
        Task<T> task = createDefaultTask(ametic, gridPane, rows, cols);
        for (int i = 0; i < rows; ++i) {
            task.getLimits().swapColumns(i, order[i]);
        }
        return task;
    }

    public static <T extends Num<T>> Task<T> createDefaultTask(Arithmetic<T> ametic, GridPane gridPane, int rows, int cols) {
        Matrix<T> matrix = fillTaskLimits(ametic, gridPane, rows, cols);
        T[] func = fillTaskFunc(ametic, gridPane, cols);
        return new Task<>(func, matrix);
    }

    public static <T extends Num<T>> TaskABM<T> createABMTaskFromGrid(Arithmetic<T> ametic, GridPane gridPane, int rows, int cols) {
        Matrix<T> m = fillTaskLimits(ametic, gridPane, rows, cols);
        m.prepareToABM();
        Matrix<T> abmMatrix = fillABMLimits(ametic, m, rows, cols);
        T[] func = fillABMTaskFunc(ametic, rows, cols);
        return new TaskABM<>(func, abmMatrix );
    }

    @SuppressWarnings("unchecked")
    private static <T extends Num<T>> T parseNumber(Arithmetic<? extends T> ametic, int row, int col, GridPane gridPane) {
        TextField tf = (TextField) getFromGridRowCol(row, col, gridPane);
        if (tf == null) {
            throw new NullPointerException();
        }
        return ametic.parse(tf.getText());
    }

    private static Node getFromGridRowCol(int row, int col, GridPane gridPane) {
        for (Node child : gridPane.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col)
                return child;
        }
        return null;
    }

    public static <T extends Num<T>> Matrix<T> fillTaskLimits(Arithmetic<T> ametic, GridPane gridPane, int rows, int cols) {
        T[][] limits = (T[][]) Utils.Empty2DimArray(ametic.zero().getClass(), rows, cols);
        T[] ext = (T[]) Array.newInstance(ametic.zero().getClass(), rows);
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                limits[i][j] = parseNumber(ametic, i + 2, j + 1, gridPane);
            }
            ext[i] = parseNumber(ametic, i + 2, cols + 2, gridPane);
        }
        return new Matrix<>(limits, ext);
    }

    public static <T extends Num<T>> T[] fillTaskFunc(Arithmetic<T> ametic, GridPane gridPane, int cols) {
        T[] func = (T[]) Array.newInstance(ametic.zero().getClass(), cols + 1);
        for (int j = 0; j < cols; ++j) {
            func[j] = parseNumber(ametic, 1, j + 1, gridPane);
        }
        func[cols] = ametic.zero();
        return func;
    }

    private static <T extends Num<T>> Matrix<T> fillABMLimits(Arithmetic<T> ametic, Matrix<T> matrix, int rows, int cols) {
        T[][] ABMLimits = (T[][]) Utils.Empty2DimArray(ametic.zero().getClass(), rows, cols + rows);
        int[] order = new int[rows + cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < rows; ++j) {
                if (i == j) {
                    ABMLimits[i][j] = ametic.parse("1");
                } else {
                    ABMLimits[i][j] = ametic.zero();
                }
            }
        }
        for (int i = 0; i < rows; ++i) {
            if (cols >= 0) System.arraycopy(matrix.getNumbers()[i], 0, ABMLimits[i], rows, cols);
        }
        for (int i = 0; i < rows; ++i) {
            order[i] = cols + i;
        }
        for (int i = rows; i < cols + rows; ++i) {
            order[i] = i - rows;
        }
        return new Matrix<>(ABMLimits, matrix.getExtension(), order);
    }

    private static <T extends Num<T>> T[] fillABMTaskFunc(Arithmetic<T> ametic, int rows, int cols) {
        T[] func = (T[]) Array.newInstance(ametic.zero().getClass(), cols + rows + 1);
        for (int j = 0; j < cols; ++j) {
            func[j] = ametic.zero();
        }
        for (int j = 0; j < rows; ++j) {
            func[j + cols] = ametic.parse("1");
        }
        func[cols + rows] = ametic.zero();
        return func;
    }


}

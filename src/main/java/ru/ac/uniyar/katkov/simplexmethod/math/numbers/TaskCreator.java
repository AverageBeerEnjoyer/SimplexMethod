package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.TaskABM;

public class TaskCreator {

    public static Task<OrdinaryFraction> createOFTaskFromGrid(GridPane gridPane, int rows, int cols) {
        OFArithmetic ametic = new OFArithmetic();
        OrdinaryFraction[][] limits = new OrdinaryFraction[rows][cols];
        OrdinaryFraction[] ext = new OrdinaryFraction[rows];
        OrdinaryFraction[] func = new OrdinaryFraction[cols + 1];

        fillTaskLimits(ametic, limits, ext, gridPane, rows, cols);
        fillTaskFunc(ametic, func, gridPane, cols);
        return new Task<>(func, new Matrix<>(limits, ext));
    }

    public static Task<Doubl> createDoubleTaskFromGrid(GridPane gridPane, int rows, int cols) {
        DoublArithmetic ametic = new DoublArithmetic();
        Doubl[][] limits = new Doubl[rows][cols];
        Doubl[] ext = new Doubl[rows];
        Doubl[] func = new Doubl[cols + 1];

        fillTaskLimits(ametic, limits, ext, gridPane, rows, cols);
        fillTaskFunc(ametic, func, gridPane, cols);
        return new Task<>(func, new Matrix<>(limits, ext));
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

    private static <T extends Num<T>> void fillTaskLimits(Arithmetic<T> ametic, T[][] limits, T[] ext, GridPane gridPane, int rows, int cols) {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                limits[i][j] = parseNumber(ametic, i + 2, j + 1, gridPane);
            }
            ext[i] = parseNumber(ametic, i + 2, cols + 2, gridPane);
        }
    }

    private static <T extends Num<T>> void fillTaskFunc(Arithmetic<T> ametic, T[] func, GridPane gridPane, int cols) {
        for (int j = 0; j < cols; ++j) {
            func[j] = parseNumber(ametic, 1, j + 1, gridPane);
        }
        func[cols] = ametic.zero();
    }

    private static <T extends Num<T>> void fillABMLimits(Arithmetic<T> ametic, T[][] limits, T[][] ABMLimits, int[] order, int rows, int cols) {
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
            if (cols >= 0) System.arraycopy(limits[i], 0, ABMLimits[i], rows, cols);
        }
        for (int i = 0; i < rows; ++i) {
            order[i] = cols + i;
        }
        for (int i = rows; i < cols + rows; ++i) {
            order[i] = i - rows;
        }
    }

    private static <T extends Num<T>> void fillABMTaskFunc(Arithmetic<T> ametic, T[] func, int rows, int cols) {
        for (int j = 0; j < cols; ++j) {
            func[j] = ametic.zero();
        }
        for (int j = 0; j < rows; ++j) {
            func[j + cols] = ametic.parse("1");
        }

        func[cols + rows] = ametic.zero();
    }

    public static Task<Doubl> createDoubleABMTaskFromGrid(GridPane gridPane, int rows, int cols) {
        DoublArithmetic ametic = new DoublArithmetic();
        Doubl[][] limits = new Doubl[rows][cols];
        Doubl[][] ABMLimits = new Doubl[rows][cols + rows];
        Doubl[] ext = new Doubl[rows];
        Doubl[] func = new Doubl[cols + rows + 1];
        int[] order = new int[rows + cols];

        fillTaskLimits(ametic, limits, ext, gridPane, rows, cols);
        Matrix<Doubl> m = new Matrix<>(limits, ext);
        m.prepareToABM();
        fillABMLimits(ametic, limits, ABMLimits, order, rows, cols);
        fillABMTaskFunc(ametic, func, rows, cols);

        return new TaskABM<>(func, new Matrix<>(ABMLimits, m.getExtension(),order));
    }

    public static Task<OrdinaryFraction> createOFABMTaskFromGrid(GridPane gridPane, int rows, int cols) {
        OFArithmetic ametic = new OFArithmetic();
        OrdinaryFraction[][] limits = new OrdinaryFraction[rows][cols + rows];
        OrdinaryFraction[][] ABMLimits = new OrdinaryFraction[rows][rows + cols];
        OrdinaryFraction[] ext = new OrdinaryFraction[rows];
        OrdinaryFraction[] func = new OrdinaryFraction[cols + rows + 1];
        int[] order = new int[rows + cols];

        fillTaskLimits(ametic, limits, ext, gridPane, rows, cols);
        Matrix<OrdinaryFraction> m = new Matrix<>(limits, ext);
        m.prepareToABM();
        fillABMLimits(ametic, limits, ABMLimits, order, rows, cols);
        fillABMTaskFunc(ametic, func, rows, cols);

        return new TaskABM<>(func, new Matrix<>(ABMLimits, m.getExtension(),order));
    }
}

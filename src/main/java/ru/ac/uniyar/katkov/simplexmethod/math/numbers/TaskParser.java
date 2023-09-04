package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.TaskABM;

import static ru.ac.uniyar.katkov.simplexmethod.Utils.getFromGridRowCol;


public class TaskParser {
    private static TaskParser instance;

    public static TaskParser getInstance() {
        if (instance == null) {
            instance = new TaskParser();
        }
        return instance;
    }

    private TaskParser() {
    }

    public <T extends Number> Task<T> createTaskWithChosenBasisFromGrid(Arithmetic<T> ametic, GridPane gridPane, int[] order, int rows, int cols, boolean min) {
        Task<T> task = createDefaultTask(ametic, gridPane, rows, cols, min);
        for (int i = 0; i < rows; ++i) {
            task.getLimits().swapColumns(i, order[i]);
        }
        return task;
    }

    public <T extends Number> Task<T> createDefaultTask(Arithmetic<T> ametic, GridPane gridPane, int rows, int cols, boolean min) {
        Matrix<T> matrix = fillTaskLimits(ametic, gridPane, rows, cols);
        T[] func = fillTaskFunc(ametic, gridPane, cols);
        return new Task<>(func, matrix, min);
    }

    public <T extends Number> TaskABM<T> createABMTaskFromGrid(Arithmetic<T> ametic, GridPane gridPane, int rows, int cols, boolean min) {
        Matrix<T> m = fillTaskLimits(ametic, gridPane, rows, cols);
        m.prepareToABM();
        Matrix<T> abmMatrix = fillABMLimits(ametic, m, rows, cols);
        T[] func = fillABMTaskFunc(ametic, rows, cols);
        return new TaskABM<>(func, abmMatrix, min);
    }

    private <T extends Number> T parseNumber(Arithmetic<? extends T> ametic, int row, int col, GridPane gridPane) {
        TextField tf = (TextField) getFromGridRowCol(row, col, gridPane);
        if (tf == null) {
            throw new NullPointerException();
        }
        return ametic.parse(tf.getText());
    }



    public <T extends Number> Matrix<T> fillTaskLimits(Arithmetic<T> ametic, GridPane gridPane, int rows, int cols) {
        T[][] limits = ametic.empty2DimArray(rows, cols);
        T[] ext = ametic.emptyArray(rows);
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                limits[i][j] = parseNumber(ametic, i + 2, j + 1, gridPane);
            }
            ext[i] = parseNumber(ametic, i + 2, cols + 1, gridPane);
        }
        return new Matrix<>(limits, ext);
    }

    public <T extends Number> T[] fillTaskFunc(Arithmetic<T> ametic, GridPane gridPane, int cols) {
        T[] func = ametic.emptyArray(cols + 1);
        for (int j = 0; j < cols + 1; ++j) {
            func[j] = parseNumber(ametic, 1, j + 1, gridPane);
        }
        return func;
    }

    private <T extends Number> Matrix<T> fillABMLimits(Arithmetic<T> ametic, Matrix<T> matrix, int rows, int cols) {
        T[][] ABMLimits = ametic.empty2DimArray(rows, cols + rows);
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

    private <T extends Number> T[] fillABMTaskFunc(Arithmetic<T> ametic, int rows, int cols) {
        T[] func = ametic.emptyArray(cols + rows + 1);
        for (int j = 0; j < cols; ++j) {
            func[j] = ametic.zero();
        }
        for (int j = 0; j < rows; ++j) {
            func[j + cols] = ametic.parse("1");
        }
        func[cols + rows] = ametic.zero();
        return func;
    }

    public void setTaskToGrid(Task<?> task, GridPane gridPane) {
        for (int i = 0; i < task.getLimits().rows; ++i) {
            for (int j = 0; j < task.getLimits().columns; ++j) {
                TextField textField = (TextField) getFromGridRowCol(i + 2, j + 1, gridPane);
                if (textField == null) return;
                textField.textProperty().setValue(task.getLimits().get(i, j).toString());
            }
            TextField textField = (TextField) getFromGridRowCol(i + 2, task.getLimits().columns + 1, gridPane);
            if (textField == null) return;
            textField.textProperty().setValue(task.getLimits().getExt(i).toString());
        }
        for (int i = 0; i < task.getTargetFunction().length; ++i) {
            TextField textField = (TextField) getFromGridRowCol(1, i + 1, gridPane);
            if (textField == null) return;
            textField.textProperty().setValue(task.getTargetFunction()[i].toString());
        }
    }
}

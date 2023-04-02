package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.Task;
public class NumberParser {
    public static Task<OrdinaryFraction> createOFTaskFromGrid(GridPane gridPane, int rows, int cols) {
        OFArithmetic ametic = new OFArithmetic();
        OrdinaryFraction[][] numbers = new OrdinaryFraction[rows][cols];
        OrdinaryFraction[] ext = new OrdinaryFraction[rows];
        OrdinaryFraction[] func = new OrdinaryFraction[cols + 1];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                numbers[i][j] = parseNumber(ametic, i + 2, j + 1, gridPane);
            }
            ext[i] = parseNumber(ametic, i + 2, cols + 2, gridPane);
        }
        for (int j = 0; j < cols; ++j) {
            func[j] = parseNumber(ametic, 1, j + 1, gridPane);
        }
        func[cols] = ametic.zero();
        return new Task<>(func, new Matrix<>(numbers, ext));
    }

    public static Task<Doubl> createDoubleTaskFromGrid(GridPane gridPane, int rows, int cols) {
        DoublArithmetic ametic = new DoublArithmetic();
        Doubl[][] numbers = new Doubl[rows][cols];
        Doubl[] ext = new Doubl[rows];
        Doubl[] func = new Doubl[cols + 1];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                numbers[i][j] = parseNumber(ametic, i + 2, j + 1, gridPane);
            }
            ext[i] = parseNumber(ametic, i + 2, cols + 2, gridPane);
        }
        for (int j = 0; j < cols; ++j) {
            func[j] = parseNumber(ametic, 1, j + 1, gridPane);
        }
        func[cols] = ametic.zero();
        return new Task<>(func, new Matrix<>(numbers, ext));
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
}

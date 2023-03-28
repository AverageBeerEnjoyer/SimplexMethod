package ru.ac.uniyar.katkov.simplexmethod.math.numbers;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.Utils;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.Task;

import java.lang.reflect.Constructor;
import java.text.ParseException;

public class NumberParser {
    public static <T extends Num<T>> T parse(Class<T> c, String s) {
        Constructor<T> constructor;
        try {
            constructor = c.getDeclaredConstructor(String.class);
            return constructor.newInstance(s);
        } catch (ReflectiveOperationException e) {
            Alerts.showCriticalError();
            return null;
        } catch (NumberFormatException e) {
            Alerts.showError("Incorrect number format");
            throw e;
        }
    }

    public static <T extends Num<T>> Task<T> taskFromGrid(Class<T> c, GridPane gridPane, int rows, int cols) {
        Object[][] numbers = new Object[rows][cols];
        Object[] ext = new Object[rows];
        Object[] func = new Object[cols + 1];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                TextField tf = (TextField) Utils.getFromGridRowCol(i + 2, j + 1, gridPane);
                if (tf == null) {
                    throw new NullPointerException();
                }
                numbers[i][j] = parse(c, tf.getText());
            }
            TextField tf = (TextField) Utils.getFromGridRowCol(i + 2, cols + 2, gridPane);
            if (tf == null) {
                throw new NullPointerException();
            }
            ext[i] = parse(c, tf.getText());
        }
        for (int j = 0; j < cols; ++j) {
            TextField tf = (TextField) Utils.getFromGridRowCol(1, j + 1, gridPane);
            if (tf == null) {
                throw new NullPointerException();
            }
            String text = tf.getText();
            func[j] = parse(c, text);
            func[cols] = ((T) func[0]).zero();
        }
        return new Task<>((T[]) func, new Matrix<T>((T[][]) numbers, (T[]) ext));
    }
}

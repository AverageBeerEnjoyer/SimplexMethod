package ru.ac.uniyar.katkov.simplexmethod.presenters.factories;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import ru.ac.uniyar.katkov.simplexmethod.ResourcesURLs;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Number;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.presenters.alerts.Alerts;

import java.io.IOException;
import java.util.Objects;

public class TaskTableFactory {
    private static GridPane createTaskTable() {
        try {
            return new FXMLLoader(ResourcesURLs.getInstance().taskTableURL).load();
        } catch (IOException e) {
            Alerts.showCriticalError(e);
            return null;
        }
    }
    public static GridPane createInputTaskTable(int rows, int cols) {
        GridPane taskTable = Objects.requireNonNull(createTaskTable());
        for (int i = 0; i < cols; ++i) {
            taskTable.add(new Label("x" + (i + 1)), i + 1, 0);
        }
        taskTable.add(new Label("b"),cols+1,0);
        for (int i = 0; i < rows+1; ++i) {
            for (int j = 0; j < cols+1; ++j) {
                taskTable.add(new TextField(), j + 1, i + 1);
            }
        }
        for (ColumnConstraints col : taskTable.getColumnConstraints()) {
            col.setPrefWidth(75);
        }
        return taskTable;

    }
    public static  <T extends Number> GridPane createTaskView(Task<T> task) {
        GridPane grid = Objects.requireNonNull(createTaskTable());
        Matrix<T> matrix = task.getLimits();
        T[] f = task.getTargetFunction();
        for (int i = 0; i < matrix.columns; ++i) {
            Label label = new Label("x" + (matrix.getOrder()[i] + 1));
            grid.add(label, i + 1, 0);
            Label label1 = new Label(f[matrix.getOrder()[i]].toString());
            grid.add(label1, i + 1, 1);
        }
        grid.add(new Label("-> min"), matrix.columns + 1, 1);
        for (int i = 0; i < matrix.rows; ++i) {
            for (int j = 0; j < matrix.columns; ++j) {
                Label label = new Label(matrix.get(i, j).toString());
                grid.add(label, j + 1, i + 2);
            }
            Label label = new Label(matrix.getExtension()[i].toString());
            grid.add(label, matrix.columns + 1, i + 2);
        }
        return grid;
    }
}

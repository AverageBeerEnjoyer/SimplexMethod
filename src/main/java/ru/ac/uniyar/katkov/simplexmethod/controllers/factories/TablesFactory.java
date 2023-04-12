package ru.ac.uniyar.katkov.simplexmethod.controllers.factories;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import ru.ac.uniyar.katkov.simplexmethod.ResourcesURLs;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;

import java.io.IOException;

public class TablesFactory {
    public static GridPane createInputTaskTable(int rows, int cols) {
        GridPane taskTable = createTaskTable();
        for (int i = 0; i < cols; ++i) {
            taskTable.add(new Label("x" + (i + 1)), i + 1, 0);
            taskTable.add(new TextField(), i + 1, 1);
        }
        taskTable.add(new Label("-> min"), cols + 2, 1);
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                taskTable.add(new TextField(), j + 1, i + 2);
            }
            taskTable.add(new TextField(), cols + 2, i + 2);
        }
        for (ColumnConstraints col : taskTable.getColumnConstraints()) {
            col.setPrefWidth(75);
        }
//        taskTable.setGridLinesVisible(true);
        return taskTable;

    }

    private static GridPane createTaskTable() {
        try {
            return new FXMLLoader(ResourcesURLs.getInstance().taskTableURL).load();
        } catch (IOException e) {
            Alerts.showCriticalError();
            return null;
        }
    }

    public static <T extends Num<T>> GridPane createSimplexTableView(SimplexTable<T> simplexTable) {
        GridPane grid;
        try {
            grid = new FXMLLoader(ResourcesURLs.getInstance().simplexTableURL).load();
        } catch (IOException e) {
            Alerts.showCriticalError();
            return null;
        }
        Matrix<T> matrix = simplexTable.getCloneMatrix();
        T[] func = simplexTable.getCloneFunc();
        for (int i = 0; i < matrix.rows; ++i) {
            grid.add(l("x" + (matrix.getOrder()[i]+1)), 0, i + 1);
        }

        for (int i = matrix.rows; i < matrix.columns; ++i) {
            grid.add(l("x" + (matrix.getOrder()[i]+1)), i - matrix.rows + 1, 0);
        }

        for (int i = 0; i < matrix.rows; ++i) {
            for (int j = matrix.rows; j < matrix.columns; ++j) {
                grid.add(l(matrix.get(i, j).toString()), j - matrix.rows + 1, i + 1);
            }
            grid.add(l(matrix.getExtension()[i].toString()), matrix.columns-matrix.rows+1, i + 1);
        }

        for (int i = matrix.rows; i < matrix.columns; ++i) {
            grid.add(l(func[matrix.getOrder()[i]].toString()), i - matrix.rows + 1, matrix.rows + 2);
        }

        grid.add(
                l(matrix.ametic.revert(simplexTable.getFunctionValue()).toString()),
                matrix.columns - matrix.rows + 1,
                matrix.rows + 2);
        return grid;
    }

    public static <T extends Num<T>> GridPane createTaskView(Task<T> task) {
        GridPane grid = createTaskTable();
        Matrix<T> matrix = task.getLimits();
        T[] f = task.getTargetFunction();
        for (int i = 0; i < matrix.columns; ++i) {
            Label label = new Label("x" +(matrix.getOrder()[i]+1));
            grid.add(label, i + 1, 0);
            Label label1 = new Label(f[i].toString());
            grid.add(label1, i + 1, 1);
        }
        grid.add(new Label("-> min"),matrix.columns+1,1);
        for (int i = 0; i < matrix.rows; ++i) {
            for (int j = 0; j < matrix.columns; ++j) {
                Label label = new Label(matrix.get(i, j).toString());
                grid.add(label, j + 1, i + 2);
            }
            Label label = new Label(matrix.getExtension()[i].toString());
            grid.add(label,matrix.columns+1,i+2);
        }
        return grid;
    }

    private static Label l(String text){
        Label label = new Label(text);
        label.minHeight(Region.USE_PREF_SIZE);
        label.setMinWidth(Region.USE_PREF_SIZE);
        label.prefHeight(Region.USE_COMPUTED_SIZE);
        label.prefWidth(Region.USE_COMPUTED_SIZE);
        return label;
    }
}

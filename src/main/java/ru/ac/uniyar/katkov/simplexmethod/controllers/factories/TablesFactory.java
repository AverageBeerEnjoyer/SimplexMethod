package ru.ac.uniyar.katkov.simplexmethod.controllers.factories;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ru.ac.uniyar.katkov.simplexmethod.ResourcesURLs;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;

import java.io.IOException;

public class TablesFactory {
    public static GridPane createNewTaskTable(int rows, int cols) {
        GridPane taskTable;
        try {
            taskTable = new FXMLLoader(ResourcesURLs.getInstance().taskTableURL).load();
        } catch (IOException e) {
            Alerts.showCriticalError();
            return null;
        }
        for (int i = 0; i < cols; ++i) {
            taskTable.add(new Label("x" + (i + 1)), i + 1, 0);
            taskTable.add(new TextField(), i + 1, 1);
        }
        taskTable.add(new Label("-> min"), cols+2,1);
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

    public VBox createSimplexTable(int rows, int cols) throws IOException {
        FXMLLoader loader = new FXMLLoader(ResourcesURLs.getInstance().simplexTableURL);
        VBox vBox = new VBox(loader.load());
        return vBox;
    }
}

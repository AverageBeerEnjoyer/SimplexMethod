package ru.ac.uniyar.katkov.simplexmethod.controllers.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.*;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.Task;

import java.net.URL;
import java.util.ResourceBundle;

import static ru.ac.uniyar.katkov.simplexmethod.controllers.factories.TablesFactory.createNewTaskTable;

public class MainSceneController implements Initializable {
    @FXML
    Spinner<Integer> rows, cols;
    @FXML
    Pane forTask;
    @FXML
    VBox forSolution;
    @FXML
    RadioButton ordinary, decimal;
    GridPane taskGrid;

    private void initSpinners() {
        rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 16, 4));
        cols.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 16, 4));
    }

    private void initButtons() {
        ToggleGroup tg = new ToggleGroup();
        ordinary.setToggleGroup(tg);
        decimal.setToggleGroup(tg);
    }

    @FXML
    private void changeDimension() {
        if (rows.getValue() > cols.getValue()) {
            Alerts.showError("too many equations");
            return;
        }
        forTask.getChildren().clear();
        taskGrid = createNewTaskTable(rows.getValue(), cols.getValue());
        forTask.getChildren().add(taskGrid);
    }

    private void createNewTable() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSpinners();
        initButtons();
        changeDimension();
    }

    @FXML
    private void createTask() {
        Task<? extends Num<?>> task;
        if (ordinary.isSelected()) {
            task = NumberParser.createOFTaskFromGrid(taskGrid, rows.getValue(), cols.getValue());
        } else {
            task = NumberParser.createDoubleTaskFromGrid(taskGrid, rows.getValue(), cols.getValue());
        }
        task.solve();
        task.printSolution();
    }
}
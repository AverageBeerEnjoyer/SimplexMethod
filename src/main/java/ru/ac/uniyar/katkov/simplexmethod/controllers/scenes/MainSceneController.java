package ru.ac.uniyar.katkov.simplexmethod.controllers.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.*;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;

import java.net.URL;
import java.util.ResourceBundle;

import static ru.ac.uniyar.katkov.simplexmethod.controllers.factories.TablesFactory.createNewTaskTable;

public class MainSceneController implements Initializable {
    private int curRows, curCols;
    @FXML
    Spinner<Integer> rows, cols;
    @FXML
    Pane forTask;
    @FXML
    VBox forSolution, basicVariables;
    @FXML
    RadioButton ordinary, decimal, artBasisMethod, mutableStartBasis;
    GridPane taskGrid;

    private void initSpinners() {
        rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 16, 3));
        cols.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 16, 4));
    }

    private void initButtons() {
        ToggleGroup numbers = new ToggleGroup();
        ordinary.setToggleGroup(numbers);
        decimal.setToggleGroup(numbers);
        ordinary.selectedProperty().set(true);

        ToggleGroup basis = new ToggleGroup();
        artBasisMethod.setToggleGroup(basis);
        mutableStartBasis.setToggleGroup(basis);
        artBasisMethod.selectedProperty().set(true);
    }

    @FXML
    private void changeDimension() {
        if (rows.getValue() > cols.getValue()) {
            Alerts.showError("too many equations");
            return;
        }
        this.curRows = rows.getValue();
        this.curCols = cols.getValue();
        forTask.getChildren().clear();
        taskGrid = createNewTaskTable(curRows, curCols);
        forTask.getChildren().add(taskGrid);
        refillBasicVars();
    }

    private void refillBasicVars() {
        basicVariables.getChildren().removeIf(child -> child instanceof CheckBox);
        for (int i = 0; i < curCols; ++i) {
            CheckBox checkBox = new CheckBox("x" + (i + 1));
            checkBox.setId(String.valueOf(i));
            checkBox.setOnAction((event) -> blockCheckBoxes());
            basicVariables.getChildren().add(checkBox);
        }
    }

    private int countSelectedCheckbox() {
        int cnt = 0;
        for (Node child : basicVariables.getChildren()) {
            if (child instanceof CheckBox) {
                if (((CheckBox) child).isSelected()) {
                    ++cnt;
                }
            }
        }
        return cnt;
    }

    private void blockCheckBoxes() {
        for (Node child : basicVariables.getChildren()) {
            child.setDisable(false);
        }
        if (countSelectedCheckbox() < curRows) {
            for (Node child : basicVariables.getChildren()) {
                child.setDisable(false);
            }
            return;
        }
        for (Node child : basicVariables.getChildren()) {
            if (child instanceof CheckBox) {
                if (!((CheckBox) child).isSelected()) {
                    child.setDisable(true);
                }
            }
        }
    }

    private int[] getChosenBasicOrder() {
        int[] order = new int[curCols];
        int l = 0, r = curRows;
        for (Node child : basicVariables.getChildren()) {
            if (child instanceof CheckBox) {
                if (((CheckBox) child).isSelected()) {
                    order[l] = Integer.parseInt(child.getId());
                    ++l;
                } else {
                    order[r] = Integer.parseInt(child.getId());
                    ++r;
                }
            }
        }
        return order;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSpinners();
        initButtons();
        changeDimension();
        setBasicVarsNotVisible();
    }

    @FXML
    private void setBasicVarsVisible() {
        basicVariables.setVisible(true);
    }

    @FXML
    private void setBasicVarsNotVisible() {
        basicVariables.setVisible(false);
    }

    @FXML
    private void createTask() {
        Task<? extends Num<?>> task;
        if (artBasisMethod.isSelected()) {
            if (ordinary.isSelected()) {
                task = TaskCreator.createOFABMTaskFromGrid(taskGrid, curRows, curCols);
            } else {
                task = TaskCreator.createDoubleABMTaskFromGrid(taskGrid, curRows, curCols);
            }
        } else {
            if (countSelectedCheckbox() < curRows) {
                Alerts.showError("Chose more basic variables.");
                return;
            }
            if (ordinary.isSelected()) {
                task = TaskCreator.createOFTaskWithChosenBasisFromGrid(taskGrid,getChosenBasicOrder(), curRows, curCols);
            } else {
                task = TaskCreator.createDoubleTaskWithChosenBasisFromGrid(taskGrid,getChosenBasicOrder(), curRows, curCols);
            }
        }
        task.solve();
        task.printSolution();
    }
}
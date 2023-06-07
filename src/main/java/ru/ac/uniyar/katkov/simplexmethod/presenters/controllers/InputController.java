package ru.ac.uniyar.katkov.simplexmethod.presenters.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ru.ac.uniyar.katkov.simplexmethod.presenters.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.presenters.factories.NodesFactory;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.*;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Number;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.TaskABM;

import java.net.URL;
import java.util.ResourceBundle;

public class InputController implements Initializable {
    private MainSceneController parent;
    private int curRows, curCols;
    @FXML
    Spinner<Integer> rows, cols;
    @FXML
    VBox forTask;
    @FXML
    VBox basicVariables;
    @FXML
    Label forSolution;
    @FXML
    RadioButton ordinary, decimal,
            artBasisMethod, mutableStartBasis,
            min, max;

    GridPane taskGrid;
    Task<? extends Number> task;
    Arithmetic<? extends Number> ametic;
    NodesFactory factory;

    TaskParser parser;

    public void setParent(MainSceneController parent) {
        this.parent = parent;
    }

    private void initSpinners() {
        rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 16, 3));
        cols.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 16, 4));
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

        ToggleGroup extr = new ToggleGroup();
        max.setToggleGroup(extr);
        min.setToggleGroup(extr);
        min.selectedProperty().set(true);
    }

    private boolean getExtr() {
        return min.isSelected();
    }

    public Task<? extends Number> getTaskForSave() {
        if (areThereEmptyFields()) {
            Alerts.showError(Alerts.Causes.notFilled);
            return null;
        }
        return parser.createDefaultTask(ametic, taskGrid, curRows, curCols, true);
    }

    public void setTask(Task<? extends Number> newTask) {
        if (newTask.getLimits().rows > 15 || newTask.getLimits().columns > 16) return;
        rows.getValueFactory().setValue(newTask.getLimits().rows);
        cols.getValueFactory().setValue(newTask.getLimits().columns);
        changeDimension();
        parser.setTaskToGrid(newTask, taskGrid);
    }

    @FXML
    private void setOFArithmetic() {
        ametic = OFArithmetic.instance;
    }

    @FXML
    private void setDoublArithmetic() {
        ametic = DoublArithmetic.instance;
    }

    public void clear() {
        forTask.getChildren().clear();
        taskGrid = factory.createInputTaskTable(curRows, curCols);
        forTask.getChildren().add(taskGrid);
        forSolution.setText("");
    }

    @FXML
    private void changeDimension() {
        if (rows.getValue() >= cols.getValue()) {
            Alerts.showError(Alerts.Causes.wrongDim);
            return;
        }
        this.curRows = rows.getValue();
        this.curCols = cols.getValue();
        clear();
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
        parser = TaskParser.getInstance();
        factory = NodesFactory.getInstance();
        initSpinners();
        initButtons();
        changeDimension();
        setBasicVarsNotVisible();
        setOFArithmetic();
    }

    @FXML
    private void setBasicVarsVisible() {
        basicVariables.setVisible(true);
    }

    @FXML
    private void setBasicVarsNotVisible() {
        basicVariables.setVisible(false);
    }

    private boolean areThereEmptyFields() {
        for (Node node : taskGrid.getChildren()) {
            if (node instanceof TextField && ((TextField) node).textProperty().isEmpty().get()) {
                return true;
            }
        }
        return false;
    }

    private Number[] getFunction() {
        return parser.fillTaskFunc(ametic, taskGrid, curCols);
    }

    private void createTask() {
        if (areThereEmptyFields()) {
            task = null;
            Alerts.showError(Alerts.Causes.notFilled);
            return;
        }
        try {
            if (artBasisMethod.isSelected()) {
                task = parser.createABMTaskFromGrid(ametic, taskGrid, curRows, curCols, getExtr());
            } else {
                if (countSelectedCheckbox() == curRows) {
                    task = parser.createTaskWithChosenBasisFromGrid(ametic, taskGrid, getChosenBasicOrder(), curRows, curCols, getExtr());
                } else {
                    Alerts.showError("Choose more basic variables");
                }
            }
        } catch (NumberFormatException e) {
            Alerts.showError(Alerts.Causes.wrongInput);
        }
    }

    @FXML
    private void solveTask() {
        createTask();
        if (task == null) return;
        try {
            if (task instanceof TaskABM) {
                Number[] func = getFunction();
                parent.solveTask((TaskABM<? extends Number>) task, func);
            } else {
                parent.solveTask(task);
            }
            parent.display();
        } catch (NumberFormatException e) {
            Alerts.showError(Alerts.Causes.wrongInput);
        } catch (ClassCastException e) {
            Alerts.showCriticalError(e);
        }
    }

    public void displaySolution(Task<?> task) {
        forSolution.setText("Solution\n" + task.getSolutionString());
    }


}
package ru.ac.uniyar.katkov.simplexmethod.controllers.scenes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ru.ac.uniyar.katkov.simplexmethod.controllers.SaveManager;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.controllers.factories.NodesFactory;
import ru.ac.uniyar.katkov.simplexmethod.controllers.graphics.CanvasGraphDrawer;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.*;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.TaskABM;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {
    private int curRows, curCols;
    private SaveManager saveManager;
    private CanvasGraphDrawer cd;
    @FXML
    Canvas canvas;
    @FXML
    Spinner<Integer> rows, cols;
    @FXML
    VBox forTask;
    @FXML
    VBox basicVariables;
    @FXML
    VBox unequals;
    @FXML
    Label forSolution;
    @FXML
    RadioButton ordinary, decimal, artBasisMethod, mutableStartBasis;
    GridPane taskGrid;
    Task<? extends Num<?>> task;
    Arithmetic<? extends Num<?>> ametic;
    NodesFactory factory;

    TaskParser parser;

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
    }
    private void initCanvasDrawer(){
        cd = new CanvasGraphDrawer(canvas);
    }
    private void initSaveManager() {
        saveManager = new SaveManager();
    }

    @FXML
    private void exit() {
        Platform.exit();
    }

    @FXML
    private void saveTask() {
        if (isThereEmptyFilled()) {
            Alerts.showError(Alerts.Causes.notFilled);
            return;
        }
        Task<?> task1 = parser.createDefaultTask(ametic, taskGrid, curRows, curCols);
        saveManager.save(task1);
    }

    @FXML
    private void openTask() {
        Task<?> newTask = saveManager.open();
        if (newTask == null) return;
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

    private void clearSolution() {
        forTask.getChildren().removeIf((node -> node != taskGrid));
        unequals.getChildren().clear();
    }

    @FXML
    private void clearTask() {
        forTask.getChildren().clear();
        taskGrid = factory.createInputTaskTable(curRows, curCols);
        forTask.getChildren().add(taskGrid);
        forSolution.setText("");
        unequals.getChildren().clear();
    }

    @FXML
    private void changeDimension() {
        if (rows.getValue() >= cols.getValue()) {
            Alerts.showError(Alerts.Causes.wrongDim);
            return;
        }
        this.curRows = rows.getValue();
        this.curCols = cols.getValue();
        clearTask();
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
        initSaveManager();
        initCanvasDrawer();
    }

    @FXML
    private void setBasicVarsVisible() {
        basicVariables.setVisible(true);
    }

    @FXML
    private void setBasicVarsNotVisible() {
        basicVariables.setVisible(false);
    }

    private boolean isThereEmptyFilled() {
        for (Node node : taskGrid.getChildren()) {
            if (node instanceof TextField && ((TextField) node).textProperty().isEmpty().get()) {
                return true;
            }
        }
        return false;
    }

    private <T extends Num<T>> T[] getFunction() {
        return (T[]) parser.fillTaskFunc(ametic, taskGrid, curCols);
    }

    private void defineTask() {
        clearSolution();
        if (isThereEmptyFilled()) {
            task = null;
            Alerts.showError("");
            return;
        }
        if (artBasisMethod.isSelected()) {
            task = parser.createABMTaskFromGrid(ametic, taskGrid, curRows, curCols);
        } else {
            if (countSelectedCheckbox() == curRows) {
                task = parser.createTaskWithChosenBasisFromGrid(ametic, taskGrid, getChosenBasicOrder(), curRows, curCols);
            } else {
                Alerts.showError("Choose more basic variables");
            }
        }
    }

    @FXML
    private <T extends Num<T>> void createTask() {
        defineTask();
        Task<T> mainTask;
        if (task == null) return;
        try {
            if (task instanceof TaskABM) {
                task.solve();
                displayTask(task);
                T[] func = getFunction();
                mainTask = new Task<>((TaskABM<T>) task, func);
            } else {
                mainTask = (Task<T>) task;
            }
        } catch (ClassCastException e) {
            Alerts.showCriticalError(e);
            return;
        }
        mainTask.solve();
        displayTask(mainTask);
        displaySolution(mainTask);
        if (curCols - curRows == 2) {
            drawTask(mainTask);
        }
    }

    private void displaySolution(Task<?> task) {
        forSolution.setText("Solution\n" + task.getSolutionString());
    }

    private void displayTask(Task<?> task) {
        forTask.getChildren().add(factory.createTaskView(task));
        for (SimplexTable<?> table : task.getSteps()) {
            forTask.getChildren().add(factory.createSimplexTableView(table));
        }
        Label label = factory.l(task.getSolutionString());
        label.getStyleClass().add("beer-color-background");
        forTask.getChildren().add(label);
    }

    private void drawTask(Task<?> task) {
        cd.setTask(task);
        List<String> uneq = cd.getUnequals();
        for(String s:uneq){
            unequals.getChildren().add(factory.l(s));
        }
    }

    @FXML
    private void increaseGraphScale() {
        cd.increaseInitInterval();
    }

    @FXML
    private void decreaseGraphScale() {
        cd.decreaseInitInterval();
    }
}
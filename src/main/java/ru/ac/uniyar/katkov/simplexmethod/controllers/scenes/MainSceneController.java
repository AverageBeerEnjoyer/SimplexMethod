package ru.ac.uniyar.katkov.simplexmethod.controllers.scenes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.controllers.factories.TablesFactory;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.*;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.TaskABM;

import java.net.URL;
import java.util.ResourceBundle;

import static ru.ac.uniyar.katkov.simplexmethod.controllers.factories.TablesFactory.createInputTaskTable;
import static ru.ac.uniyar.katkov.simplexmethod.controllers.factories.TablesFactory.createSimplexTableView;

public class MainSceneController implements Initializable {
    private int curRows, curCols;
    private SaveManager saveManager;
    @FXML
    Spinner<Integer> rows, cols;
    @FXML
    VBox forTask;
    @FXML
    VBox basicVariables;
    @FXML
    Label forSolution;
    @FXML
    RadioButton ordinary, decimal, artBasisMethod, mutableStartBasis;
    GridPane taskGrid;
    Task<? extends Num<?>> task;
    Arithmetic<? extends Num<?>> ametic;

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
        Task<?> task1 = TaskParser.createDefaultTask(ametic, taskGrid, curRows, curCols);
        saveManager.save(task1);
    }

    @FXML
    private void openTask() {
        Task<?> newTask = saveManager.open();
        if (newTask == null) return;
        if(newTask.getLimits().rows > 15 || newTask.getLimits().columns>16) return;
        rows.getValueFactory().setValue(newTask.getLimits().rows);
        cols.getValueFactory().setValue(newTask.getLimits().columns);
        changeDimension();
        TaskParser.setTaskToGrid(newTask,taskGrid);
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
    }

    @FXML
    private void clearTask(){
        forTask.getChildren().clear();
        taskGrid = createInputTaskTable(curRows, curCols);
        forTask.getChildren().add(taskGrid);
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
        initSpinners();
        initButtons();
        changeDimension();
        setBasicVarsNotVisible();
        setOFArithmetic();
        initSaveManager();
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
        return (T[]) TaskParser.fillTaskFunc(ametic, taskGrid, curCols);
    }

    private void defineTask() {
        clearSolution();
        if (isThereEmptyFilled()) {
            task = null;
            Alerts.showError("");
            return;
        }
        if (artBasisMethod.isSelected()) {
            task = TaskParser.createABMTaskFromGrid(ametic, taskGrid, curRows, curCols);
        } else {
            if (countSelectedCheckbox() == curRows) {
                task = TaskParser.createTaskWithChosenBasisFromGrid(ametic, taskGrid, getChosenBasicOrder(), curRows, curCols);
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
                mainTask = new Task<T>((TaskABM<T>) task, func);
            } else {
                mainTask = (Task<T>) task;
            }
        } catch (ClassCastException e) {
            Alerts.showCriticalError();
            return;
        }
        mainTask.solve();
        displayTask(mainTask);
        displaySolution(mainTask);
    }
    private void displaySolution(Task<?> task){
        forSolution.setText("Solution\n"+task.solutionString());
    }
    private void displayTask(Task<?> task) {
        forTask.getChildren().add(TablesFactory.createTaskView(task));
        for (SimplexTable<?> table : task.getSteps()) {
            forTask.getChildren().add(createSimplexTableView(table));
        }
    }
}
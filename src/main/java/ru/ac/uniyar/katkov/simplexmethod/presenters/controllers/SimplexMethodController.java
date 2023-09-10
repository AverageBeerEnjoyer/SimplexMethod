package ru.ac.uniyar.katkov.simplexmethod.presenters.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Number;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SimplexTableCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.TaskCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.TaskABM;
import ru.ac.uniyar.katkov.simplexmethod.presenters.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.presenters.factories.LabelsFactory;
import ru.ac.uniyar.katkov.simplexmethod.presenters.factories.SimplexTableViewFactory;
import ru.ac.uniyar.katkov.simplexmethod.presenters.factories.TaskTableFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SimplexMethodController implements Initializable {
    private Task<? extends Number> task;
    private TaskABM<? extends Number> taskABM;

    @FXML
    private Label forSolution;
    @FXML
    private VBox forTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    private void clearLayoutSince(GridPane table) {
        int index = forTask.getChildren().indexOf(table);
        forTask.getChildren().remove(index + 1, forTask.getChildren().size());
    }

    public boolean nextTable(Pair<Integer, Integer> swap, SimplexTable<? extends Number> table, GridPane gridPane) {
        if (table.getCondition() != SimplexTableCondition.NOT_FINAL) {
            return false;
        }

        SimplexTable<? extends Number> newTable;
        try {
            newTable = table.next(swap);
        } catch (ArithmeticException e) {
            Alerts.showError(e.getMessage());
            return false;
        }
        Task<? extends Number> task1 = newTable.getTask();
        clearLayoutSince(gridPane);
        task1.removeStepsSince(newTable);

        forTask.getChildren().add(SimplexTableViewFactory.createSimplexTableView(newTable, this));
        if (newTable.getCondition() != SimplexTableCondition.NOT_FINAL) {

            forTask.getChildren().add(LabelsFactory.beerL(task1.getSolutionString()));
            if (taskABM == task1 && task1.getCondition() == TaskCondition.HAS_SOLUTION) {
                displayTask(task);
            }
        }
        return true;
    }

    public void setTasks(TaskABM<? extends Number> taskABM, Task<? extends Number> task) {
        clear();
        this.taskABM = taskABM;
        this.task = task;
        if (taskABM != null) {
            displayTask(taskABM);
        }
        if (task != null)
            displayTask(task);
    }

    public void displayTask(Task<? extends Number> task) {
        if (task == null) return;
        forTask.getChildren().add(TaskTableFactory.createTaskView(task));
        for (SimplexTable<?> table : task.getBestSteps()) {
            forTask.getChildren().add(SimplexTableViewFactory.createSimplexTableView(table, this));
        }
        Label label = LabelsFactory.beerL(task.getSolutionString());
        forTask.getChildren().add(label);
        displaySolution(task);
    }

    private void displaySolution(Task<?> task) {
        forSolution.setText("Solution\n" + task.getSolutionString());
    }

    public void clear() {
        forTask.getChildren().clear();
        forSolution.setText("");
    }

    @FXML
    private void reset() {
        clear();
        setTasks(taskABM, task);
    }
}

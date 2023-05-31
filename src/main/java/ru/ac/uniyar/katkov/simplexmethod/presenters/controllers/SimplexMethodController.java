package ru.ac.uniyar.katkov.simplexmethod.presenters.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.presenters.factories.NodesFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SimplexMethodController implements Initializable {
    private MainSceneController parent;
    private NodesFactory factory;

    @FXML
    private Label forSolution;
    @FXML
    private VBox forTask;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        factory = NodesFactory.getInstance();
    }

    public void setParent(MainSceneController parent) {
        this.parent = parent;
    }
    public void displayTask(Task<?> task) {
        if(task == null) return;
        forTask.getChildren().add(factory.createTaskView(task));
        for (SimplexTable<?> table : task.getSteps()) {
            forTask.getChildren().add(factory.createSimplexTableView(table));
        }
        Label label = factory.l(task.getSolutionString());
        label.getStyleClass().add("beer-color-background");
        forTask.getChildren().add(label);
        displaySolution(task);
    }
    private void displaySolution(Task<?> task) {
        forSolution.setText("Solution\n" + task.getSolutionString());
    }

    public void clear(){
        forTask.getChildren().clear();
        forSolution.setText("");
    }
}

package ru.ac.uniyar.katkov.simplexmethod.presenters.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import ru.ac.uniyar.katkov.simplexmethod.ResourcesURLs;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Number;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.TaskABM;
import ru.ac.uniyar.katkov.simplexmethod.presenters.SaveManager;
import ru.ac.uniyar.katkov.simplexmethod.presenters.alerts.Alerts;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {
    @FXML
    private Tab input, simplexMethod, graphicMethod;
    @FXML
    private TabPane tabs;

    private InputController inputController;
    private GraphicMethodController graphicMethodController;
    private SimplexMethodController simplexMethodController;

    private SaveManager saveManager;

    private Task<? extends Number> task;
    private TaskABM<? extends Number> taskABM;

    private void initChildren() throws IOException {
        FXMLLoader inputLoader = new FXMLLoader(ResourcesURLs.getInstance().inputURL);
        inputLoader.load();
        Node inputRoot = inputLoader.getRoot();
        input.setContent(inputRoot);
        inputController = inputLoader.getController();
        inputController.setParent(this);

        FXMLLoader graphicMethodLoader = new FXMLLoader(ResourcesURLs.getInstance().graphicMethodURL);
        graphicMethodLoader.load();
        Node graphicMethodRoot = graphicMethodLoader.getRoot();
        graphicMethod.setContent(graphicMethodRoot);
        graphicMethodController = graphicMethodLoader.getController();

        FXMLLoader simplexMethodLoader = new FXMLLoader(ResourcesURLs.getInstance().simplexMethodURL);
        simplexMethodLoader.load();
        Node simplexMethodRoot = simplexMethodLoader.getRoot();
        simplexMethod.setContent(simplexMethodRoot);
        simplexMethodController = simplexMethodLoader.getController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            saveManager = new SaveManager();
            initChildren();
        } catch (IOException e) {
            Alerts.showCriticalError(e);
        }
    }

    public void solveTask(Task<? extends Number> task) {
        this.task = task;
        this.taskABM = null;
        task.solve();
    }

    public void solveTask(TaskABM<? extends Number> taskABM, Number[] function) {
        this.task = null;
        this.taskABM = taskABM;
        taskABM.solve();
        try {
            this.task = new Task(taskABM, function);
            task.solve();
        } catch (IllegalArgumentException e) {
            this.task = null;
        }
    }

    public void display() {
        graphicMethodController.drawTask(task);
        simplexMethodController.setTasks(taskABM, task);
        if (task != null)
            inputController.displaySolution(task);
        else inputController.displaySolution(taskABM);
    }

    @FXML
    private void saveTask() {
        tabs.getSelectionModel().select(input);
        Task<? extends Number> taskToSave = inputController.getTaskForSave();
        if (taskToSave == null) return;
        saveManager.save(taskToSave);
    }

    @FXML
    private void openTask() {
        tabs.getSelectionModel().select(input);
        Task<? extends Number> taskFromSave = saveManager.open();
        if (taskFromSave == null) return;
        inputController.setTask(taskFromSave);
    }

    @FXML
    private void clearTask() {
        inputController.clear();
        simplexMethodController.clear();
        graphicMethodController.clear();
    }

    @FXML
    private void help() {
        SceneManager.getInstance().getHelpStage().show();
    }

    @FXML
    private void exit() {
        Platform.exit();
    }
}

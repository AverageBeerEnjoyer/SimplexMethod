package ru.ac.uniyar.katkov.simplexmethod.presenters.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.presenters.factories.LabelsFactory;
import ru.ac.uniyar.katkov.simplexmethod.presenters.graphics.CanvasGraphDrawer;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GraphicMethodController implements Initializable {

    private CanvasGraphDrawer cd;

    @FXML
    VBox unequals;
    @FXML
    Canvas canvas;

    private void initCanvasDrawer(){
        cd = new CanvasGraphDrawer(canvas);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCanvasDrawer();
    }

    @FXML
    private void increaseGraphScale() {
        cd.increaseInitInterval();
    }

    @FXML
    private void decreaseGraphScale() {
        cd.decreaseInitInterval();
    }

    public void clear(){
        unequals.getChildren().clear();
        cd.setTask(null);
    }

    public void drawTask(Task<?> task) {
        clear();
        cd.setTask(task);
        List<String> uneq = cd.getUnequals();
        for(String s:uneq){
            unequals.getChildren().add(LabelsFactory.l(s));
        }
    }
}

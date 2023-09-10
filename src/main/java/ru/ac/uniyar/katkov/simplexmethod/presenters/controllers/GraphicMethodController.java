package ru.ac.uniyar.katkov.simplexmethod.presenters.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.GraphicTask;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.presenters.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.presenters.factories.LabelsFactory;
import ru.ac.uniyar.katkov.simplexmethod.presenters.graphics.CanvasGraphDrawer;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GraphicMethodController implements Initializable {

    private CanvasGraphDrawer graphDrawer;
    private double dragStartX, dragStartY;
    private GraphicTask<?> graphicTask;

    @FXML
    VBox unequals;
    @FXML
    Canvas canvas;
    @FXML
    Spinner<Integer> abscissa, ordinate;
    @FXML
    GridPane spinners;

    private void initCanvasDrawer() {
        graphDrawer = new CanvasGraphDrawer(canvas);
    }

    private void activateSpinners() {
        ((SpinnerValueFactory.IntegerSpinnerValueFactory) (abscissa.getValueFactory())).setMax(graphicTask.getMatrix().columns);
        ((SpinnerValueFactory.IntegerSpinnerValueFactory) (ordinate.getValueFactory())).setMax(graphicTask.getMatrix().columns);
        spinners.setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCanvasDrawer();
        abscissa.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2, 1));
        ordinate.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2, 2));
    }

    @FXML
    private void increaseGraphScale() {
        graphDrawer.increaseInitInterval();
    }

    @FXML
    private void decreaseGraphScale() {
        graphDrawer.decreaseInitInterval();
    }

    public void clear() {
        unequals.getChildren().clear();
        graphDrawer.setTask(null);
        graphicTask = null;
        spinners.setDisable(true);
    }

    public void drawTask(Task<?> task) {
        clear();
        try {
            graphicTask = new GraphicTask<>(task);
            List<String> unequalsList = graphicTask.getUnequals();
            for (String s : unequalsList) {
                unequals.getChildren().add(LabelsFactory.l(s));
            }
            unequals.getChildren().add(LabelsFactory.l("Solution:"));
            unequals.getChildren().addAll(Arrays.stream(
                    graphicTask.getSolutionString().split("\n")).map(LabelsFactory::l).collect(Collectors.toList()));
            graphDrawer.setTask(graphicTask);
            activateSpinners();
        } catch (IllegalArgumentException | NullPointerException e) {
            unequals.getChildren().add(LabelsFactory.l("Can not draw in 2 dimensions"));
        }
    }


    @FXML
    private void startDrag(MouseEvent event) {
        dragStartX = event.getX();
        dragStartY = event.getY();
    }

    @FXML
    private void endDrag(MouseEvent event) {
        graphDrawer.move(event.getX() - dragStartX, event.getY() - dragStartY);
        dragStartX = event.getX();
        dragStartY = event.getY();
    }

    @FXML
    private void zoom(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            graphDrawer.increaseInitInterval();
        } else {
            graphDrawer.decreaseInitInterval();
        }
    }

    @FXML
    private void update() {
        if (abscissa.getValue().equals(ordinate.getValue())) {
            Alerts.showError("Choose different variables!");
            return;
        }
        graphicTask.solve(abscissa.getValue() - 1, ordinate.getValue() - 1);
        graphDrawer.setTask(graphicTask);
    }
}

package ru.ac.uniyar.katkov.simplexmethod;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.ac.uniyar.katkov.simplexmethod.presenters.controllers.SceneManager;

public class SimplexApp extends Application {
    @Override
    public void start(Stage stage) {
        SceneManager sceneManager = SceneManager.getInstance();
        stage.setTitle("Simplex method");
        stage.setResizable(false);
        stage.setScene(sceneManager.getMainScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
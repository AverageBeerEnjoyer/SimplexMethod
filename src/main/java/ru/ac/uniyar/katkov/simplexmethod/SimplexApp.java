package ru.ac.uniyar.katkov.simplexmethod;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.controllers.scenes.SceneManager;

import java.io.IOException;

public class SimplexApp extends Application {
    @Override
    public void start(Stage stage) {
        try {
            SceneManager sceneManager = new SceneManager();
            stage.setTitle("Simplex method");
            stage.setScene(sceneManager.getMainScene());
            stage.show();
        } catch (IOException e){
            Alerts.showCriticalError();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
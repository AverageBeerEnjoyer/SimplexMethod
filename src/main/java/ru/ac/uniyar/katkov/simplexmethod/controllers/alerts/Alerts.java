package ru.ac.uniyar.katkov.simplexmethod.controllers.alerts;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerts {
    public static void showError(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.show();
    }
    public static void showCriticalError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Something bad happened...\nPress FINISH to close this scary window");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.FINISH);
        alert.showAndWait();
        Platform.exit();
    }
}

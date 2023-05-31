package ru.ac.uniyar.katkov.simplexmethod.presenters.alerts;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerts {
    public static class Causes {
        public static final String
                notFilled = "There are empty fields",
                wrongInput = "Incorrect input values",
                wrongDim = "Too many equations",
                fileError = "Can not open file";
    }


    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.show();
    }

    public static void showCriticalError(Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Something bad happened...\nPress FINISH to close this scary window");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.FINISH);
        alert.showAndWait();
        Platform.exit();
    }
}

package ru.ac.uniyar.katkov.simplexmethod.controllers.scenes;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.TaskParser;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveManager {
    private FileChooser fileChooser;
    private Stage stage;

    public SaveManager() {
        fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        fileChooser.setTitle("Chose task file");

        stage = new Stage();
    }

    public void save(Task<?> task) {
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) return;
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(task.toString());
            fileWriter.flush();
        } catch (IOException e){
            Alerts.showError("Can not open file "+file.getName());
        }
    }
}

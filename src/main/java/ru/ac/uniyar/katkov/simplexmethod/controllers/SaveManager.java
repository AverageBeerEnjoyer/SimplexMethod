package ru.ac.uniyar.katkov.simplexmethod.controllers;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;

import java.io.*;
import java.util.Scanner;

public class SaveManager {
    private final FileChooser fileChooser;
    private final Stage stage;

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
            fileWriter.close();
        } catch (IOException e) {
            Alerts.showError(Alerts.Causes.fileError);
        }
    }

    public Task<?> open() {
        File file = fileChooser.showOpenDialog(stage);
        if(file == null) return null;
        try {
            FileReader fileReader = new FileReader(file);
            Scanner scanner = new Scanner(fileReader);
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            scanner.close();
            fileReader.close();
            return Task.parseTask(sb.toString());
        } catch (IOException e) {
            Alerts.showError(Alerts.Causes.fileError);
            return null;
        }
    }
}

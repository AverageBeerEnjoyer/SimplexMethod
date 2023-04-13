package ru.ac.uniyar.katkov.simplexmethod.controllers.scenes;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.ac.uniyar.katkov.simplexmethod.ResourcesURLs;
import ru.ac.uniyar.katkov.simplexmethod.controllers.alerts.Alerts;

import java.io.IOException;

public class SceneManager {
    private static final SceneManager instance;
    private Stage helpStage;

    static {
        try {
            instance = new SceneManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SceneManager getInstance() {
        return instance;
    }

    private Scene mainScene;
    private Scene helpScene;
    private final double screenWidth;
    private final double screenHeight;

    public SceneManager() throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        screenWidth = screenSize.getMaxX();
        screenHeight = screenSize.getMaxY();
        createMainScene();
    }

    private void createMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourcesURLs.getInstance().mainSceneURL);
            mainScene = new Scene(loader.load(), screenWidth * 0.75, screenHeight * 0.75);
        } catch (IOException e){
            Alerts.showCriticalError();
        }
    }

    private void createHelpScene(){

    }


    public Scene getMainScene() {
        return mainScene;
    }
}

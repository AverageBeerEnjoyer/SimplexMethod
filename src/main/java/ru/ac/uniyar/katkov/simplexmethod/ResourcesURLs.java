package ru.ac.uniyar.katkov.simplexmethod;

import java.net.MalformedURLException;
import java.net.URL;

public class ResourcesURLs {
    private static ResourcesURLs instance;

    public final URL mainSceneURL;
    public final URL simplexTableURL;
    public final URL taskTableURL;

    public static ResourcesURLs getInstance() {
        if (instance == null) {
            instance = new ResourcesURLs();
        }
        return instance;
    }

    private ResourcesURLs() {
        mainSceneURL = getClass().getResource("main-scene.fxml");
        simplexTableURL = getClass().getResource("simplex-table.fxml");
        taskTableURL = getClass().getResource("task-table.fxml");
    }
}

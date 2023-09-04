package ru.ac.uniyar.katkov.simplexmethod.presenters.factories;

import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class LabelsFactory {

    public static Label l(String text) {
        Label label = new Label(text);
        label.minHeight(Region.USE_PREF_SIZE);
        label.setMinWidth(Region.USE_PREF_SIZE);
        label.prefHeight(Region.USE_COMPUTED_SIZE);
        label.prefWidth(Region.USE_COMPUTED_SIZE);
        return label;
    }
    public static Label beerL(String text){
        Label label = l(text);
        label.getStyleClass().add("beer-color-background");
        return label;
    }
}

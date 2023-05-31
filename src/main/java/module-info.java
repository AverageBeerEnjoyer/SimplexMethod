module SimplexMethod.main {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    exports ru.ac.uniyar.katkov.simplexmethod;
    exports ru.ac.uniyar.katkov.simplexmethod.presenters.controllers;
    opens ru.ac.uniyar.katkov.simplexmethod.presenters.controllers to javafx.fxml;
    opens ru.ac.uniyar.katkov.simplexmethod.math.numbers;
}
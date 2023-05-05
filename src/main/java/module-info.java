module SimplexMethod.main {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    exports ru.ac.uniyar.katkov.simplexmethod;
    exports ru.ac.uniyar.katkov.simplexmethod.controllers.scenes;
    opens ru.ac.uniyar.katkov.simplexmethod.controllers.scenes to javafx.fxml;
}
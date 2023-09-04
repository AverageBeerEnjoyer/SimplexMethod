package ru.ac.uniyar.katkov.simplexmethod.presenters.factories;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import ru.ac.uniyar.katkov.simplexmethod.ResourcesURLs;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Number;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SwapAbility;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;
import ru.ac.uniyar.katkov.simplexmethod.presenters.alerts.Alerts;
import ru.ac.uniyar.katkov.simplexmethod.presenters.controllers.SimplexMethodController;
import ru.ac.uniyar.katkov.simplexmethod.presenters.listeners.SimplexTableCellClickListener;

import java.io.IOException;
import java.util.Objects;

import static ru.ac.uniyar.katkov.simplexmethod.Utils.getFromGridRowCol;
import static ru.ac.uniyar.katkov.simplexmethod.presenters.factories.LabelsFactory.l;

public class SimplexTableViewFactory {
    public static <T extends Number> GridPane createSimplexTableView(SimplexTable<T> simplexTable, SimplexMethodController controller) {
        GridPane grid;
        try {
            grid = new FXMLLoader(ResourcesURLs.getInstance().simplexTableURL).load();
        } catch (IOException e) {
            Alerts.showCriticalError(e);
            return null;
        }

        Matrix<T> matrix = simplexTable.getCloneMatrix();
        T[] func = simplexTable.getCloneFunc();

        for (int i = 0; i < matrix.rows; ++i) {
            grid.add(l("x" + (matrix.getOrder()[i] + 1)), 0, i + 1);
        }

        for (int i = matrix.rows; i < matrix.columns; ++i) {
            grid.add(l("x" + (matrix.getOrder()[i] + 1)), i - matrix.rows + 1, 0);
        }

        grid.add(l("func"), 0, matrix.rows + 1);
        grid.add(l(simplexTable.getVectorString()), 0, 0);

        for (int i = 0; i < matrix.rows; ++i) {
            for (int j = matrix.rows; j < matrix.columns; ++j) {
                Label label = l(matrix.get(i, j).toString());
                label.setOnMouseClicked(new SimplexTableCellClickListener(i, j, simplexTable, grid, controller));
                grid.add(label, j - matrix.rows + 1, i + 1);
            }
            grid.add(l(matrix.getExtension()[i].toString()), matrix.columns - matrix.rows + 1, i + 1);
        }

        colorizeSimplexTable(simplexTable, grid);

        for (int i = matrix.rows; i < matrix.columns; ++i) {
            grid.add(l(func[matrix.getOrder()[i]].toString()), i - matrix.rows + 1, matrix.rows + 1);
        }
        grid.add(
                l(matrix.ametic.revert(simplexTable.getFunctionValue()).toString()),
                matrix.columns - matrix.rows + 1,
                matrix.rows + 1);
        return grid;
    }

    public static void colorizeSimplexTable(SimplexTable<?> simplexTable, GridPane simplexTableView) {
        for (int k = 0; k < simplexTable.getMatrix().rows; ++k) {
            for (int l = 0; l < simplexTable.getMatrix().columns - simplexTable.getMatrix().rows; ++l) {
                SwapAbility swapAbility = simplexTable.getSwapAbility(k, l);
                Node label = Objects.requireNonNull(getFromGridRowCol(k + 1, l + 1, simplexTableView));
                switch (swapAbility) {
                    case BEST -> label.setStyle("-fx-background-color: #7CFC00");
                    case YES -> label.setStyle("-fx-background-color: #FFEA00");
                    case NO -> label.setStyle("-fx-background-color: default");
                }
            }
        }
    }
}

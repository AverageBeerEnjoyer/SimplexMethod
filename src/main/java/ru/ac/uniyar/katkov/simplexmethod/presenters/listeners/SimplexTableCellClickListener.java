package ru.ac.uniyar.katkov.simplexmethod.presenters.listeners;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SwapAbility;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;
import ru.ac.uniyar.katkov.simplexmethod.presenters.controllers.SimplexMethodController;
import ru.ac.uniyar.katkov.simplexmethod.presenters.factories.SimplexTableViewFactory;

import java.util.Objects;

import static ru.ac.uniyar.katkov.simplexmethod.Utils.getFromGridRowCol;

public class SimplexTableCellClickListener implements EventHandler<MouseEvent> {
    private final int i, j;
    private final SimplexTable<?> simplexTable;
    private final GridPane simplexTableView;
    private final SimplexMethodController controller;

    public SimplexTableCellClickListener(int i, int j, SimplexTable<?> simplexTable, GridPane simplexTableView, SimplexMethodController controller) {
        this.i = i;
        this.j = j;
        this.simplexTableView = simplexTableView;
        this.controller = controller;
        this.simplexTable = simplexTable;
    }

    @Override
    public void handle(MouseEvent event) {
        int rows = simplexTable.getMatrix().rows;
        if(simplexTable.getSwapAbility(i,j-rows) == SwapAbility.NO) return;
        if (controller.nextTable(new Pair<>(i, j), simplexTable, simplexTableView)) {
            SimplexTableViewFactory.colorizeSimplexTable(simplexTable,simplexTableView);
            Objects.requireNonNull(getFromGridRowCol(i+1,j-rows+1,simplexTableView)).setStyle("-fx-background-color: coral");
        }
    }
}

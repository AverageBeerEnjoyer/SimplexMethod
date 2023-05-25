package ru.ac.uniyar.katkov.simplexmethod.math.simplex.task;

import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Number;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.TaskCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;

import java.util.List;
import java.util.Optional;

import static ru.ac.uniyar.katkov.simplexmethod.Utils.getlast;
import static ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SimplexTableCondition.FINAL;

public class TaskABM<T extends Number> extends Task<T> {
    public TaskABM(T[] targetFunction, Matrix<T> limits) {
        super(targetFunction, limits);
    }

    @Override
    protected void defineCondition() {
        if (isSolutionCorrect()) {
            Optional<List<T>> sol = getlast(steps).getSolution();
            sol.ifPresent(ts -> solution = ts);
            condition = TaskCondition.HAS_SOLUTION;
        } else {
            condition = TaskCondition.NO_SOLUTION;
        }
    }

    private boolean isSolutionCorrect() {
        SimplexTable<T> last = getlast(steps);
        return last.getCondition() == FINAL && !last.isArtificialVarsInBasis() && ametic.isZero(last.getFunctionValue());
    }

    public Matrix<T> getMatrixForNewTask() {
        Matrix<T> last = getlast(steps).getCloneMatrix();
        T[][] nums = ametic.empty2DimArray(last.rows, last.columns - last.rows);
        int[] newOrder = new int[last.columns - last.rows];
        for (int i = 0, tmp = 0; i < last.columns - last.rows && tmp < last.columns; ++i, ++tmp) {
            while (last.getOrder()[tmp] >= last.columns - last.rows) {
                ++tmp;
            }
            newOrder[i] = last.getOrder()[tmp];
            for (int j = 0; j < last.rows; ++j) {
                nums[j][i] = last.getNumbers()[j][tmp];
            }
        }
        return new Matrix<>(nums, last.getExtension(), newOrder);
    }
}

package ru.ac.uniyar.katkov.simplexmethod.math.simplex.task;

import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.TaskCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ru.ac.uniyar.katkov.simplexmethod.Utils.getlast;
import static ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SimplexTableCondition.FINAL;

public class TaskABM<T extends Num<T>> extends Task<T> {
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
        return last.getCondition() == FINAL && !last.isArtificialVarsInBasis() && last.getFunctionValue().isZero();
    }

    public Matrix<T> getMatrixForNewTask() {
        Matrix<T> last = getlast(steps).getCloneMatrix();
        T[][] nums = Arrays.copyOf(last.getNumbers(), last.rows);
        int[] newOrder = new int[last.columns-last.rows];
        for (int i = 0; i < last.rows; ++i) {
            nums[i] = Arrays.copyOf(last.getNumbers()[i], last.columns - last.rows);
        }
        for (int i = 0, tmp = 0; i < last.columns-last.rows && tmp<last.columns; ++i, ++tmp) {
            while(last.getOrder()[tmp]>=last.columns-last.rows){
                ++tmp;
            }
            newOrder[i]=last.getOrder()[tmp];
            for(int j=0;j<last.rows;++j){
                nums[j][i] = last.getNumbers()[j][tmp];
            }
        }
        return new Matrix<>(nums,last.getExtension(),newOrder);
    }
}

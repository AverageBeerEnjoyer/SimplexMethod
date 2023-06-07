package ru.ac.uniyar.katkov.simplexmethod.math.simplex.task;

import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Arithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Number;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SimplexTableCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.TaskCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.ac.uniyar.katkov.simplexmethod.Utils.getlast;


public class Task<T extends Number> {

    public final Arithmetic<T> ametic;
    protected T[] targetFunction;
    protected Matrix<T> limits;
    protected List<SimplexTable<T>> bestSteps;
    protected List<SimplexTable<T>> steps;
    protected List<T> solution;
    protected TaskCondition condition;
    protected boolean min;

    public Task(TaskABM<T> taskABM, T[] func) {
        this(taskABM.ametic, taskABM.getMatrixForNewTask(), func, taskABM.min);
        if (taskABM.condition != TaskCondition.HAS_SOLUTION) throw new IllegalArgumentException();
    }

    private Task(Arithmetic<T> ametic, Matrix<T> limits, T[] targetFunction, boolean min) {
        bestSteps = new ArrayList<>();
        steps = new ArrayList<>();
        this.ametic = ametic;
        this.condition = TaskCondition.NOT_SOLVED;
        this.limits = limits;
        this.targetFunction = targetFunction;
        this.min = min;
        if (!(this instanceof TaskABM) && !min) {
            reversTargetFunction();
        }
    }


    public Task(T[] targetFunction, Matrix<T> limits, boolean min) {
        this(limits.ametic, limits, targetFunction, min);
    }

    public void solve() {
        if (condition != TaskCondition.NOT_SOLVED) return;
        SimplexTable<T> start = new SimplexTable<>(targetFunction.clone(), limits.clone(), this);
        bestSteps.add(start);
        steps.add(start);
        while (getlast(bestSteps).getCondition() == SimplexTableCondition.NOT_FINAL) {
            SimplexTable<T> table = getlast(bestSteps).next();
            bestSteps.add(table);
        }
        defineCondition();
    }

    private void reversTargetFunction() {
        for (int i = 0; i < targetFunction.length; ++i) {
            targetFunction[i] = ametic.revert(targetFunction[i]);
        }
    }

    protected void defineCondition() {
        Optional<List<T>> solution = getlast(bestSteps).getSolution();
        if (solution.isPresent()) {
            condition = TaskCondition.HAS_SOLUTION;
            this.solution = solution.get();
        } else {
            condition = TaskCondition.NOT_LIMITED;
        }
    }

    public void removeStepsSince(SimplexTable<? extends Number> table) {
        int index = steps.indexOf(table);
        while (steps.size() > index + 1) {
            steps.remove(index + 1);
        }
    }

    private String solutionString() {
        StringBuilder sb = new StringBuilder();
        sb.append("x* = (");
        for (int i = 0; i < solution.size(); ++i) {
            sb.append(solution.get(i).toString());
            if (i != solution.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")\n");
        sb.append("function value: ");
        T res = getlast(bestSteps).getFunctionValue();
        if (min) {
            sb.append(res);
        } else{
            sb.append(ametic.revert(res));
        }
        return sb.toString();
    }

    public String getSolutionString() {
        switch (condition) {
            case NOT_SOLVED -> {
                return "Task not solved yet";
            }

            case NOT_LIMITED -> {
                return "function is not limited";
            }
            case NO_SOLUTION -> {
                return "No solution";
            }
            default -> {
                return solutionString();
            }
        }
    }

    public List<T> getSolution() {
        if (condition == TaskCondition.HAS_SOLUTION)
            return solution;
        return null;
    }

    public List<SimplexTable<T>> getBestSteps() {
        return bestSteps;
    }

    public Matrix<T> getLimits() {
        return limits;
    }

    public T[] getTargetFunction() {
        return targetFunction;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ametic.toString()).append(" ");
        sb.append(limits.rows).append(" ").append(limits.columns).append(" ");
        for (T t : targetFunction) {
            sb.append(t).append(" ");
        }
        for (T[] ts : limits.getNumbers()) {
            for (T t : ts) {
                sb.append(t).append(" ");
            }
        }
        for (T t : limits.getExtension()) {
            sb.append(t).append(" ");
        }
        return sb.toString();
    }

    public static <T extends Number> Task<T> parseTask(String s) {
        String[] split = s.split(" ");
        Arithmetic<T> ametic = Arithmetic.parseArithmetic(split[0]);
        int rows, cols;
        rows = Integer.parseInt(split[1]);
        cols = Integer.parseInt(split[2]);
        int it = 3;

        T[] targetFunc = ametic.emptyArray(cols + 1);
        for (int i = 0; i < targetFunc.length; ++i) {
            targetFunc[i] = ametic.parse(split[it]);
            ++it;
        }

        T[][] limits = ametic.empty2DimArray(rows, cols);
        for (T[] ts : limits) {
            for (int i = 0; i < ts.length; ++i) {
                ts[i] = ametic.parse(split[it]);
                ++it;
            }
        }

        T[] ext = ametic.emptyArray(rows);
        for (int i = 0; i < ext.length; ++i) {
            ext[i] = ametic.parse(split[it]);
            ++it;
        }
        Matrix<T> matrix = new Matrix<>(limits, ext);
        return new Task<>(targetFunc, matrix, true);
    }

    public TaskCondition getCondition() {
        return condition;
    }
}

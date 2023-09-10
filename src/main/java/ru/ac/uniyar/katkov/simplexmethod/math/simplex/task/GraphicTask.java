package ru.ac.uniyar.katkov.simplexmethod.math.simplex.task;

import ru.ac.uniyar.katkov.simplexmethod.Utils;
import ru.ac.uniyar.katkov.simplexmethod.math.GaussMethod;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Arithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Number;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.TaskCondition;
import ru.ac.uniyar.katkov.simplexmethod.presenters.graphics.Dot;

import java.util.ArrayList;
import java.util.List;


public class GraphicTask<T extends Number> {
    public final Arithmetic<T> ametic;
    private final Matrix<T> matrix;
    private final T[] function;
    private final T[] solution;
    private final String solutionString;
    private Dot solutionDot;
    private Dot antiNormalVector;


    public GraphicTask(Task<T> task) {
        if (task.getLimits().columns - task.getLimits().rows != 2) throw new IllegalArgumentException();
        this.matrix = task.getLimits().clone();
        this.ametic = matrix.ametic;
        this.function = task.getTargetFunction().clone();
        if (task.condition != TaskCondition.NOT_LIMITED) {
            this.solution = task.getSolution().toArray(ametic.emptyArray(task.getSolution().size()));
        } else solution = null;
        this.solutionString = task.getSolutionString();
        solve(0, 1);
    }

    public void solve(int x1, int x2) {
        if (x1 == x2 || x1 < 0 || x2 < 0 || x1 > matrix.columns || x2 > matrix.columns) return;
        matrix.setDefaultOrder();
        matrix.swapColumns(matrix.columns - 2, Utils.indexOf(matrix.getOrder(), x1));
        matrix.swapColumns(matrix.columns - 1, Utils.indexOf(matrix.getOrder(), x2));
        new GaussMethod<>(ametic).solve(matrix);
        if (solution != null) {
            solutionDot = new Dot(solution[x2].doubleValue(), solution[x1].doubleValue());
        }
        countAntiNormalVector();
    }

    private void countAntiNormalVector() {
        int[] order = matrix.getOrder();
        T[] normalVector = ametic.emptyArray(2);
        normalVector[0] = function[matrix.getOrder()[matrix.columns - 2]];
        normalVector[1] = function[matrix.getOrder()[matrix.columns - 1]];
        for (int i = 0; i < matrix.rows; ++i) {
            for (int j = matrix.rows; j < matrix.columns; ++j) {
                normalVector[j - matrix.rows] = ametic.minus(normalVector[j - matrix.rows], ametic.multiply(function[order[i]], matrix.get(i, j)));
            }
        }
        double norm = Math.sqrt(Math.pow(normalVector[0].doubleValue(), 2) + Math.pow(normalVector[1].doubleValue(), 2));
        antiNormalVector = new Dot(ametic.revert(normalVector[1]).doubleValue() / norm, ametic.revert(normalVector[0]).doubleValue() / norm);
    }


    public List<String> getUnequals() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < matrix.rows; ++i) {
            StringBuilder sb = new StringBuilder();
            for (int j = matrix.rows; j < matrix.columns; ++j) {
                T el = matrix.get(i, j);
                switch (ametic.compare(el, ametic.cast(0))) {
                    case -1 -> {
                        if (j != matrix.rows) {
                            sb.append(" + ");
                        }
                        sb.append(ametic.revert(el));
                        sb.append(" * ");
                        sb.append("x").append(matrix.getOrder()[j] + 1);
                    }
                    case 1 -> {
                        sb.append(" - ");
                        sb.append(el);
                        sb.append(" * ");
                        sb.append("x").append(matrix.getOrder()[j] + 1);
                    }
                    default -> {
                    }
                }
            }
            T el = matrix.getExt(i);
            switch (ametic.compare(el, ametic.cast(0))) {
                case -1 -> {
                    sb.append(" - ");
                    sb.append(ametic.revert(el));
                }
                case 1 -> {
                    sb.append(" + ");
                    sb.append(el);
                }
                default -> {
                }
            }
            sb.append(" >= 0");
            res.add(sb.toString());
        }
        return res;
    }

    public Matrix<T> getMatrix() {
        return matrix;
    }

    public T[] getFunction() {
        return function;
    }

    public String getSolutionString() {
        return solutionString;
    }

    public Dot getSolutionDot() {
        return solutionDot;
    }

    public Dot getAntiNormalVector() {
        return antiNormalVector;
    }
}

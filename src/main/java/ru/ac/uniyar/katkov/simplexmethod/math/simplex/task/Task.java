package ru.ac.uniyar.katkov.simplexmethod.math.simplex.task;

import ru.ac.uniyar.katkov.simplexmethod.Utils;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Arithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SimplexTableCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.TaskCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.ac.uniyar.katkov.simplexmethod.Utils.getlast;


public class Task<T extends Num<T>> {

    public final Arithmetic<T> ametic;
    protected T[] targetFunction;
    protected Matrix<T> limits;
    protected List<SimplexTable<T>> steps;
    protected List<T> solution;
    protected TaskCondition condition;

    public Task(TaskABM<T> taskABM, T[] func) {
        if (taskABM.condition != TaskCondition.HAS_SOLUTION) throw new IllegalArgumentException();
        ametic = taskABM.ametic;
        this.targetFunction = func;
        this.limits = taskABM.getMatrixForNewTask();
        this.condition = TaskCondition.NOT_SOLVED;
        this.steps = new ArrayList<>();
    }

    public Task(T[] targetFunction, Matrix<T> limits) {
        this.ametic = limits.ametic;
        this.targetFunction = targetFunction;
        this.limits = limits;
        this.condition = TaskCondition.NOT_SOLVED;
        this.steps = new ArrayList<>();
    }

    public void solve() {
        SimplexTable<T> start = new SimplexTable<>(targetFunction.clone(), limits.clone());
        steps.add(start);
        while (getlast(steps).getCondition() == SimplexTableCondition.NOT_FINAL) {
            SimplexTable<T> table = getlast(steps).next();
            steps.add(table);
        }
        defineCondition();
    }

    protected void defineCondition() {
        Optional<List<T>> solution = getlast(steps).getSolution();
        if (solution.isPresent()) {
            condition = TaskCondition.HAS_SOLUTION;
            this.solution = solution.get();
        } else {
            condition = TaskCondition.NOT_LIMITED;
        }
    }

    //DEBUG
    public void printSolution() {
        switch (condition) {
            case NOT_SOLVED -> System.out.println("Task not solved yet");
            case NOT_LIMITED -> System.out.println("function is not limited");
            case NO_SOLUTION -> System.out.println("No solution");
            default -> {
                for (T t : solution) {
                    System.out.println(t.toString());
                }
                System.out.println("function value: " + ametic.revert(getlast(steps).getFunctionValue()));
            }
        }
    }

    public List<T> getSolution() {
        if (condition == TaskCondition.HAS_SOLUTION)
            return solution;
        return null;
    }

    public List<SimplexTable<T>> getSteps() {
        return steps;
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
        sb.append(ametic.toString()).append("\n");
        sb.append(limits.rows).append(" ").append(limits.columns).append("\n");
        for(T t:targetFunction){
            sb.append(t).append(" ");
        }
        sb.append("\n");
        for(T[] ts:limits.getNumbers()){
            for(T t:ts){
                sb.append(t).append(" ");
            }
            sb.append("\n");
        }
        for(T t:limits.getExtension()){
            sb.append(t).append(" ");
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static  <T extends Num<T>> Task<T> parseTask(String s) {
        String[] split = s.split(" ");
        Arithmetic<T> ametic = Arithmetic.parseArithmetic(split[0]);
        int rows, cols;
        rows = Integer.parseInt(split[1]);
        cols = Integer.parseInt(split[2]);
        int it = 3;

        T[] targetFunc = (T[]) Array.newInstance(ametic.zero().getClass(),cols+1);
        for(T t:targetFunc){
            t = ametic.parse(split[it]);
            ++it;
        }

        T[][] limits = (T[][])Utils.Empty2DimArray(ametic.zero().getClass(),rows,cols);
        for (T[] ts:limits) {
            for (T t:ts) {
                t = ametic.parse(split[it]);
                ++it;
            }
        }

        T[] ext = (T[]) Array.newInstance(ametic.zero().getClass(),rows);
        for(T t: ext){
            t = ametic.parse(split[it]);
            ++it;
        }
        Matrix<T> matrix= new Matrix<>(limits,ext);
        return new Task<>(targetFunc,matrix);
    }
}

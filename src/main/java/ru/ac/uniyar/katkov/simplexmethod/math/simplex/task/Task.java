package ru.ac.uniyar.katkov.simplexmethod.math.simplex.task;

import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Arithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.SimplexTableCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.conditions.TaskCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.ac.uniyar.katkov.simplexmethod.Utils.getlast;


public class Task<T extends Num<T>> {

    protected final Arithmetic<T> ametic;
    protected T[] targetFunction;
    protected Matrix<T> limits;
    protected List<SimplexTable<T>> steps;
    protected List<T> solution;
    protected TaskCondition condition;

    public Task(T[] targetFunction, Matrix<T> limits){
        this.ametic = Arithmetic.getArithmeticOfType(targetFunction[0]);
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

    protected void defineCondition(){
        Optional<List<T>> solution = getlast(steps).getSolution();
        if(solution.isPresent()){
            condition = TaskCondition.HAS_SOLUTION;
            this.solution = solution.get();
        }else{
            condition = TaskCondition.NOT_LIMITED;
        }
    }

    //DEBUG
    public void printSolution(){
        switch (condition){
            case NOT_SOLVED:{
                System.out.println("Task not solved yet");
                return;
            }
            case NOT_LIMITED:{
                System.out.println("function is not limited");
                return;
            }
            case NO_SOLUTION:{
                System.out.println("No solution");
                return;
            }
            default:{
                for (T t : solution) {
                    System.out.println(t.toString());
                }
                System.out.println("function value: "+ametic.revert(getlast(steps).getFunctionValue()));
            }
        }
    }


}

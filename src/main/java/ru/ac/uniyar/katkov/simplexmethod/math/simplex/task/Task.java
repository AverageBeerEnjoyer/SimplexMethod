package ru.ac.uniyar.katkov.simplexmethod.math.simplex.task;

import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Arithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.SimplexTableCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.TaskCondition;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Task<T extends Num<T>> {

    private final Arithmetic<T> ametic;
    private T[] targetFunction;
    private Matrix<T> limits;
    private List<SimplexTable<T>> steps;
    private List<T> solution;
    private TaskCondition condition;

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
        while (steps.get(steps.size() - 1).getCondition() == SimplexTableCondition.NOT_FINAL) {
            SimplexTable<T> table = steps.get(steps.size() - 1).next();
            steps.add(table);
        }
        Optional<List<T>> solution = steps.get(steps.size()-1).getSolution();
        if(solution.isPresent()){
            condition = TaskCondition.HAS_SOLUTION;
            this.solution = solution.get();
        }else{
            condition = TaskCondition.NOT_LIMITED;
        }
    }

    //DEBUG
    public void printSolution(){
        if(condition==TaskCondition.NOT_LIMITED){
            System.out.println("function is not limited");
            return;
        }
        if(condition == TaskCondition.NOT_SOLVED){
            System.out.println("Task not solved yet");
            return;
        }
        for (T t : solution) {
            System.out.println(t.toString());
        }
        T f=ametic.zero();
        for(int i=0;i< solution.size();++i){
            f = ametic.plus(f, ametic.multiply(solution.get(i),targetFunction[i]));
        }
        f = ametic.plus(f,targetFunction[targetFunction.length-1]);
        System.out.println("function falue: "+f);
    }


}

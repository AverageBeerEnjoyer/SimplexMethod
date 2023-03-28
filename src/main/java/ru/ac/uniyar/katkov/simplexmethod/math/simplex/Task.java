package ru.ac.uniyar.katkov.simplexmethod.math.simplex;

import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Task<T extends Num<T>> {
    private T[] targetFunction;
    private Matrix<T> limits;
    private List<SimplexTable<T>> steps;
    private List<T> solution;
    private TaskCondition condition;

    public Task(T[] targetFunction, Matrix<T> limits){
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
        T f=limits.get(0,0).zero();
        for(int i=0;i< solution.size();++i){
            f = f.plus(solution.get(i).multiply(targetFunction[i]));
        }
        f = f.plus(targetFunction[targetFunction.length-1]);
        System.out.println("function falue: "+f);
    }


}

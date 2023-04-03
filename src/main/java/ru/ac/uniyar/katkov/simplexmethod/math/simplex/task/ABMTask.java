package ru.ac.uniyar.katkov.simplexmethod.math.simplex.task;

import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;

public class ABMTask<T extends Num<T>> extends Task<T> {
    public ABMTask(T[] targetFunction, Matrix<T> limits) {
        super(targetFunction, limits);
    }

    @Override
    public void solve() {
        super.solve();
    }
}

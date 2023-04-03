package ru.ac.uniyar.katkov.simplexmethod.math.simplex;

import org.junit.jupiter.api.Test;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;

import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction.OF;
import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction.ZERO;

public class TaskTest {
    @Test
    void simpleTask(){
        OrdinaryFraction[] func = new OrdinaryFraction[4];
        func[0]= OF(-1,1);
        func[1] = OF(2,1);
        func[2] = OF(-1,1);
        func[3] = ZERO;
        OrdinaryFraction[][] limits = new OrdinaryFraction[2][3];

        limits[0][0] = OF(1,1);
        limits[0][1] = OF(4,1);
        limits[0][2] = OF(1,1);

        limits[1][0] = OF(1,1);
        limits[1][1] = OF(-2,1);
        limits[1][2] = OF(-1,1);


        OrdinaryFraction[] ext = new OrdinaryFraction[3];
        ext[0] = OF(5,1);
        ext[1] = OF(1,1);

        Task<OrdinaryFraction> task = new Task<>(func, new Matrix<>(limits,ext));
        task.solve();
        task.printSolution();
    }
}

package ru.ac.uniyar.katkov.simplexmethod.math.simplex;

import org.junit.jupiter.api.Test;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;

import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction.OF;
import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction.ZERO;

public class TaskTest {
    @Test
    void simpleTask(){
        OrdinaryFraction[] func = new OrdinaryFraction[6];
        func[0]= OF(-2,1);
        func[1] = OF(2,1);
        func[2] = OF(1,1);
        func[3] = OF(2,1);
        func[4] = OF(-3,1);
        func[5] = ZERO;
        OrdinaryFraction[][] limits = new OrdinaryFraction[3][5];

        limits[0][0] = OF(-2,1);
        limits[0][1] = OF(1,1);
        limits[0][2] = OF(-1,1);
        limits[0][3] = OF(-1,1);
        limits[0][4] = OF(0,1);

        limits[1][0] = OF(1,1);
        limits[1][1] = OF(-1,1);
        limits[1][2] = OF(2,1);
        limits[1][3] = OF(1,1);
        limits[1][4] = OF(1,1);

        limits[2][0] = OF(-1,1);
        limits[2][1] = OF(1,1);
        limits[2][2] = OF(0,1);
        limits[2][3] = OF(0,1);
        limits[2][4] = OF(-1,1);


        OrdinaryFraction[] ext = new OrdinaryFraction[3];
        ext[0] = OF(1,1);
        ext[1] = OF(4,1);
        ext[2] = OF(4,1);

        Task<OrdinaryFraction> task = new Task<>(func, new Matrix<>(limits,ext));
        task.solve();
        task.printSolution();
    }
}

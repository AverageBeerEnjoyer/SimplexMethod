package ru.ac.uniyar.katkov.simplexmethod.math.simplex;

import org.junit.jupiter.api.Test;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.TaskABM;

import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction.OF;
import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction.ZERO;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    TaskABM<OrdinaryFraction> createABMTask() {
        OrdinaryFraction[] func = new OrdinaryFraction[7];
        func[0] = OF(0, 1);
        func[1] = OF(0, 1);
        func[2] = OF(0, 1);
        func[3] = OF(0, 1);
        func[4] = OF(1, 1);
        func[5] = OF(1, 1);
        func[6] = ZERO;
        OrdinaryFraction[][] limits = new OrdinaryFraction[2][6];

        limits[0][0] = OF(1, 1);
        limits[0][1] = OF(0, 1);
        limits[0][2] = OF(1, 1);
        limits[0][3] = OF(3, 1);
        limits[0][4] = OF(3, 1);
        limits[0][5] = OF(1, 1);

        limits[1][0] = OF(0, 1);
        limits[1][1] = OF(1, 1);
        limits[1][2] = OF(2, 1);
        limits[1][3] = OF(0, 1);
        limits[1][4] = OF(3, 1);
        limits[1][5] = OF(-1, 1);


        OrdinaryFraction[] ext = new OrdinaryFraction[2];
        ext[0] = OF(3, 1);
        ext[1] = OF(4, 1);

        int[] order = new int[]{4, 5, 0, 1, 2, 3};

        return new TaskABM<>(func, new Matrix<>(limits, ext, order));
    }

    @Test
    void simpleTask() {
        OrdinaryFraction[] func = new OrdinaryFraction[4];
        func[0] = OF(-1, 1);
        func[1] = OF(2, 1);
        func[2] = OF(-1, 1);
        func[3] = ZERO;
        OrdinaryFraction[][] limits = new OrdinaryFraction[2][3];

        limits[0][0] = OF(1, 1);
        limits[0][1] = OF(4, 1);
        limits[0][2] = OF(1, 1);

        limits[1][0] = OF(1, 1);
        limits[1][1] = OF(-2, 1);
        limits[1][2] = OF(-1, 1);


        OrdinaryFraction[] ext = new OrdinaryFraction[3];
        ext[0] = OF(5, 1);
        ext[1] = OF(1, 1);

        Task<OrdinaryFraction> task = new Task<>(func, new Matrix<>(limits, ext));
        task.solve();
        task.solutionString();
    }

    @Test
    void simpleNotSolvableABMTask() {
        OrdinaryFraction[] func = new OrdinaryFraction[8];
        func[0] = OF(0, 1);
        func[1] = OF(0, 1);
        func[2] = OF(0, 1);
        func[3] = OF(0, 1);
        func[4] = OF(1, 1);
        func[5] = OF(1, 1);
        func[6] = OF(1, 1);
        func[7] = ZERO;
        OrdinaryFraction[][] limits = new OrdinaryFraction[3][7];

        limits[0][0] = OF(1, 1);
        limits[0][1] = OF(0, 1);
        limits[0][2] = OF(0, 1);
        limits[0][3] = OF(1, 1);
        limits[0][4] = OF(2, 1);
        limits[0][5] = OF(-1, 1);
        limits[0][6] = OF(-1, 1);

        limits[1][0] = OF(0, 1);
        limits[1][1] = OF(1, 1);
        limits[1][2] = OF(0, 1);
        limits[1][3] = OF(-1, 1);
        limits[1][4] = OF(2, 1);
        limits[1][5] = OF(3, 1);
        limits[1][6] = OF(1, 1);

        limits[2][0] = OF(0, 1);
        limits[2][1] = OF(0, 1);
        limits[2][2] = OF(1, 1);
        limits[2][3] = OF(1, 1);
        limits[2][4] = OF(5, 1);
        limits[2][5] = OF(1, 1);
        limits[2][6] = OF(-1, 1);

        OrdinaryFraction[] ext = new OrdinaryFraction[3];
        ext[0] = OF(1, 1);
        ext[1] = OF(2, 1);
        ext[2] = OF(5, 1);

        int[] order = new int[]{4, 5, 6, 0, 1, 2, 3};

        TaskABM<OrdinaryFraction> task = new TaskABM<>(func, new Matrix<>(limits, ext, order));
        task.solve();
        task.solutionString();
    }

    @Test
    void simpleABMTask() {
        TaskABM<OrdinaryFraction> taskABM = createABMTask();
        taskABM.solve();
        OrdinaryFraction[] func = {
                new OrdinaryFraction(-1, 1),
                new OrdinaryFraction(5, 1),
                new OrdinaryFraction(1, 1),
                new OrdinaryFraction(-1, 1),
                new OrdinaryFraction(0, 1)
        };
        Task<OrdinaryFraction> task = new Task<>(taskABM, func);
        task.solve();
        assertNotNull(task.getSolution());
        OrdinaryFraction[] expectedSolution = {
                new OrdinaryFraction(7,3),
                new OrdinaryFraction(0,1),
                new OrdinaryFraction(0,1),
                new OrdinaryFraction(2,3)
        };
        for (int i = 0; i < 4; ++i) {
            assertEquals(expectedSolution[i],task.getSolution().get(i));
        }
    }
}

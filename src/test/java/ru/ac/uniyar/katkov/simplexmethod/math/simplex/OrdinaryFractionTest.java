package ru.ac.uniyar.katkov.simplexmethod.math.simplex;

import org.junit.jupiter.api.Test;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction.*;



public class OrdinaryFractionTest {
    OrdinaryFraction[] testNumbers = {OF(-15,7), OF(0,1), OF(5,1)};
    @Test
    void additionTest(){
        assertThat(testNumbers[0].plus(testNumbers[1])).isEqualTo(OF(-15,7));
        assertThat(testNumbers[0].plus(testNumbers[2])).isEqualTo(OF(20,7));
        assertThat(testNumbers[1].plus(testNumbers[2])).isEqualTo(OF(5,1));
    }

    @Test
    void multiplicationTest(){
        assertThat(testNumbers[0].multiply(testNumbers[1])).isEqualTo(ZERO);
        assertThat(testNumbers[0].multiply(testNumbers[2])).isEqualTo(OF(-75,7));
    }
}

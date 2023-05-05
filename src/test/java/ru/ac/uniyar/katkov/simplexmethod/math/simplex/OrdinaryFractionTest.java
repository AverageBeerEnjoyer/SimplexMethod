package ru.ac.uniyar.katkov.simplexmethod.math.simplex;

import org.junit.jupiter.api.Test;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.OFArithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction.*;



public class OrdinaryFractionTest {

    OFArithmetic ametic = OFArithmetic.instance;
    OrdinaryFraction[] testNumbers = {OF(-15,7), OF(0,1), OF(5,1)};
    @Test
    void additionTest(){
        assertThat(ametic.plus(testNumbers[0],testNumbers[1])).isEqualTo(OF(-15,7));
        assertThat(ametic.plus(testNumbers[0],testNumbers[2])).isEqualTo(OF(20,7));
        assertThat(ametic.plus(testNumbers[1],testNumbers[2])).isEqualTo(OF(5,1));
    }

    @Test
    void multiplicationTest(){
        assertThat(ametic.multiply(testNumbers[0],testNumbers[1])).isEqualTo(ZERO);
        assertThat(ametic.multiply(testNumbers[0],testNumbers[2])).isEqualTo(OF(-75,7));
    }
}

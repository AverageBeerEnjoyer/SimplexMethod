package ru.ac.uniyar.katkov.simplexmethod.math.simplex;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.Doubl.D;
import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction.OF;

import org.junit.jupiter.api.Test;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Doubl;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.NumberParser;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction;
public class NumberParserTest {
    @Test
    void parseTest(){
        OrdinaryFraction of = NumberParser.parse(OrdinaryFraction.class, "-15/7");
        Doubl d = NumberParser.parse(Doubl.class,"1.5");
        assertThat(of).isEqualTo(OF(-15,7));
        assertThat(d).isEqualTo(D(1.5));
    }

}

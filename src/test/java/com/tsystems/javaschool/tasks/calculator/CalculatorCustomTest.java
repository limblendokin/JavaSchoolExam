package com.tsystems.javaschool.tasks.calculator;

import org.junit.Assert;
import org.junit.Test;

public class CalculatorCustomTest {

    private Calculator calc = new Calculator();

    @Test
    public void evaluate20() {
        //given
        String input = "-6";
        String expectedResult = "-6";

        //run
        String result = calc.evaluate(input);

        //assert
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void evaluate21() {
        //given
        String input = "6+";
        String expectedResult = null;

        //run
        String result = calc.evaluate(input);

        //assert
        Assert.assertEquals(expectedResult, result);
    }
    @Test
    public void evaluate22() {
        //given
        String input = "2+3*4-2";
        String expectedResult = "12";

        //run
        String result = calc.evaluate(input);

        //assert
        Assert.assertEquals(expectedResult, result);
    }
    @Test
    public void evaluate23() {
        //given
        String input = "6";
        String expectedResult = "6";

        //run
        String result = calc.evaluate(input);

        //assert
        Assert.assertEquals(expectedResult, result);
    }
    @Test
    public void evaluate24() {
        //given
        String input = "()";
        String expectedResult = null;

        //run
        String result = calc.evaluate(input);

        //assert
        Assert.assertEquals(expectedResult, result);
    }
    @Test
    public void evaluate25() {
        //given
        String input = "1+(0)";
        String expectedResult = "1";

        //run
        String result = calc.evaluate(input);

        //assert
        Assert.assertEquals(expectedResult, result);
    }
    @Test
    public void evaluate26() {
        //given
        String input = "1+(1+1)";
        String expectedResult = "3";

        //run
        String result = calc.evaluate(input);

        //assert
        Assert.assertEquals(expectedResult, result);
    }
    @Test
    public void evaluate27() {
        //given
        String input = "1/0";
        String expectedResult = null;

        //run
        String result = calc.evaluate(input);

        //assert
        Assert.assertEquals(expectedResult, result);
    }
    @Test
    public void evaluate28() {
        //given
        String input = "1*2/0";
        String expectedResult = null;

        //run
        String result = calc.evaluate(input);

        //assert
        Assert.assertEquals(expectedResult, result);
    }

}

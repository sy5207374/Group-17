package com.seglab.calculator;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorFailDemoTest {
    private final Calculator c = new Calculator();

    @Test public void divide_fail_demo() {
        assertEquals(4, c.divide(9,3)); // 故意写错
    }
}
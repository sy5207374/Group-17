package com.seglab.calculator;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorTest {
    private final Calculator c = new Calculator();

    @Test public void add_ok()        { assertEquals(7,  c.add(3,4)); }
    @Test public void subtract_ok()   { assertEquals(-1, c.subtract(3,4)); }
    @Test public void multiply_ok()   { assertEquals(12, c.multiply(3,4)); }
    @Test public void divide_ok()     { assertEquals(2,  c.divide(9,4)); } // int division 9/4=2

    @Test public void divide_throws_onZero() {
        try {
            c.divide(1,0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) { /* pass */ }
    }
}
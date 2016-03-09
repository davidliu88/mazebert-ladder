package com.mazebert.plugins.random;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public abstract class RandomNumberGeneratorTest {

    protected RandomNumberGenerator generator;

    @Before
    public void setUp() throws Exception {
        generator = createGenerator();
    }

    protected abstract SecureRandomNumberGenerator createGenerator();

    @Test
    public void randomIntegerInRange() {
        int min = -2;
        int max = +2;
        Set<Integer> generated = new HashSet<>();

        while (generated.size() < 5) {
            generated.add(generator.randomInteger(min, max));
        }
    }

    @Test
    public void randomDoubleInRange() {
        assertTrue(generator.randomDouble() < 1.0);
    }
}

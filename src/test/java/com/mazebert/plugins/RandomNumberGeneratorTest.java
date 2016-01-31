package com.mazebert.plugins;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

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
        Set<Integer> generated = new HashSet<Integer>();

        while (generated.size() < 5) {
            generated.add(generator.randomInteger(min, max));
        }
    }
}

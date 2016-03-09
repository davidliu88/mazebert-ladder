package com.mazebert.plugins.random.mocks;

import com.mazebert.plugins.random.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RandomNumberGeneratorMock implements RandomNumberGenerator {
    private List<Integer> randomIntegers = new ArrayList<>();
    private List<String> randomIntegerCalls = new ArrayList<>();

    private List<Double> randomDoubles = new ArrayList<>();

    public int randomInteger(int min, int max) {
        randomIntegerCalls.add("min: " + min + ", max: " + max);
        if (randomIntegers.size() > 0) {
            return randomIntegers.remove(0);
        }

        return min;
    }

    @Override
    public double randomDouble() {
        if (randomDoubles.size() > 0) {
            return randomDoubles.remove(0);
        }

        return 0.0;
    }

    public void givenRandomIntegers(Integer ... values) {
        randomIntegers.addAll(Arrays.asList(values));
    }

    public void givenRandomDoubles(Double ... values) {
        for (Double value : values) {
            assertTrue("Random double must be >= 0", value >= 0.0);
            assertTrue("Random double must be < 1", value < 1.0);
        }
        randomDoubles.addAll(Arrays.asList(values));
    }

    public void thenRandomIntegerCallsAre(String ... calls) {
        assertEquals(Arrays.asList(calls), randomIntegerCalls);
    }
}

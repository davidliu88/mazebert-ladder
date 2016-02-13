package com.mazebert.plugins.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RandomNumberGeneratorCoach implements RandomNumberGenerator {
    private List<Integer> randomIntegers = new ArrayList<>();
    private List<String> randomIntegerCalls = new ArrayList<>();

    public int randomInteger(int min, int max) {
        randomIntegerCalls.add("min: " + min + ", max: " + max);
        if (randomIntegers.size() > 0) {
            return randomIntegers.remove(0);
        }

        return min;
    }

    public void givenRandomIntegers(Integer ... values) {
        randomIntegers.addAll(Arrays.asList(values));
    }

    public void thenRandomIntegerCallsAre(String ... calls) {
        assertEquals(Arrays.asList(calls), randomIntegerCalls);
    }
}

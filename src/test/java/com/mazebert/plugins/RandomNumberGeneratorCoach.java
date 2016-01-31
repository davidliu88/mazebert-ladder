package com.mazebert.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RandomNumberGeneratorCoach implements RandomNumberGenerator {
    private List<Integer> randomIntegers = new ArrayList<Integer>();
    private List<String> randomIntegerCalls = new ArrayList<String>();

    public int randomInteger(int min, int max) {
        randomIntegerCalls.add("min: " + min + ", max: " + max);
        return randomIntegers.remove(0);
    }

    public void givenRandomIntegers(Integer ... values) {
        randomIntegers.addAll(Arrays.asList(values));
    }

    public void thenRandomIntegerCallsAre(String ... calls) {
        assertEquals(Arrays.asList(calls), randomIntegerCalls);
    }
}

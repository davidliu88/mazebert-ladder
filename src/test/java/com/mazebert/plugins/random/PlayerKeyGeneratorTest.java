package com.mazebert.plugins.random;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerKeyGeneratorTest {
    private PlayerKeyGenerator generator;
    private RandomNumberGeneratorCoach randomNumberGenerator;

    private String playerKey;

    @Before
    public void setUp() throws Exception {
        randomNumberGenerator = new RandomNumberGeneratorCoach();
        generator = new PlayerKeyGenerator(randomNumberGenerator);
    }

    @Test
    public void firstPossibleCharacter() {
        randomNumberGenerator.givenRandomIntegers(0, 0, 0, 0, 0, 0);
        whenKeyIsGenerated();
        thenKeyIs("aaaaaa");
    }

    @Test
    public void lastPossibleCharacter() {
        randomNumberGenerator.givenRandomIntegers(34, 34, 34, 34, 34, 34);
        whenKeyIsGenerated();
        thenKeyIs("999999");
    }

    @Test
    public void minMaxIsSetCorrectly() {
        randomNumberGenerator.givenRandomIntegers(0, 0, 0, 0, 0, 0);
        whenKeyIsGenerated();
        randomNumberGenerator.thenRandomIntegerCallsAre(
                "min: 0, max: 34",
                "min: 0, max: 34",
                "min: 0, max: 34",
                "min: 0, max: 34",
                "min: 0, max: 34",
                "min: 0, max: 34"
        );
    }

    private void whenKeyIsGenerated() {
        playerKey = generator.createPlayerKey();
    }

    private void thenKeyIs(String expected) {
        assertEquals(expected, playerKey);
    }
}
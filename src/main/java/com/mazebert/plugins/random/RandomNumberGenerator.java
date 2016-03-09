package com.mazebert.plugins.random;

public interface RandomNumberGenerator {
    /**
     * Generates a random integer number that is in range [min, max]
     * @param min minimum value
     * @param max maximum value
     * @return The generated number
     */
    int randomInteger(int min, int max);

    /**
     * Generates a random double number that is in range [0, 1[
     * @return The generated number
     */
    double randomDouble();
}

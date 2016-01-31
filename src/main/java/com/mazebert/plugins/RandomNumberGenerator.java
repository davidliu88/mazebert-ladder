package com.mazebert.plugins;

public interface RandomNumberGenerator {
    /**
     * Generates a random integer number that is in range [min, max]
     * @param min minimum value
     * @param max maximum value
     * @return The generated number
     */
    int randomInteger(int min, int max);
}

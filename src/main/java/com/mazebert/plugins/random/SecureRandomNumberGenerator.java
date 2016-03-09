package com.mazebert.plugins.random;

import java.util.concurrent.ThreadLocalRandom;

public class SecureRandomNumberGenerator implements RandomNumberGenerator {
    public int randomInteger(int min, int max) {
        return getRandom().nextInt(min, max + 1);
    }

    @Override
    public double randomDouble() {
        return getRandom().nextDouble();
    }

    private ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }
}

package com.mazebert.plugins;

import java.util.concurrent.ThreadLocalRandom;

public class SecureRandomNumberGenerator implements RandomNumberGenerator {
    public int randomInteger(int min, int max) {
        return getRandom().nextInt(min, max + 1);
    }

    private ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }
}

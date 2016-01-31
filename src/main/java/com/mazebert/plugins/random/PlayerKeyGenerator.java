package com.mazebert.plugins.random;

import javax.inject.Inject;

public class PlayerKeyGenerator {
    private final RandomNumberGenerator randomNumberGenerator;
    private final char[] characterTable = {
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y',
            'z', '1', '2', '3', '4',
            '5', '6', '7', '8', '9'
    };

    @Inject
    public PlayerKeyGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public String createPlayerKey() {
        String key = "";
        int lastCharacterIndex = characterTable.length - 1;
        for (int i = 0; i < 6; ++i) {
            key += characterTable[randomNumberGenerator.randomInteger(0, lastCharacterIndex)];
        }

        return key;
    }
}

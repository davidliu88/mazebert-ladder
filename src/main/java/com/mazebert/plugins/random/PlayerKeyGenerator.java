package com.mazebert.plugins.random;

public class PlayerKeyGenerator {
    private final RandomNumberGenerator randomNumberGenerator;
    private final int length = 6;
    private final char[] characterTable = {
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y',
            'z', '1', '2', '3', '4',
            '5', '6', '7', '8', '9'
    };

    public PlayerKeyGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public String createPlayerKey() {
        String key = "";
        int lastCharacterIndex = characterTable.length - 1;
        for (int i = 0; i < length; ++i) {
            key += characterTable[randomNumberGenerator.randomInteger(0, lastCharacterIndex)];
        }

        return key;
    }
}

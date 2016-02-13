package com.mazebert.plugins.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerKeyGeneratorCoach extends PlayerKeyGenerator {
    private List<String> keysToGenerate = new ArrayList<>();

    public PlayerKeyGeneratorCoach() {
        super(null);
    }

    @Override
    public String createPlayerKey() {
        if (keysToGenerate.size() > 0) {
            return keysToGenerate.remove(0);
        }

        return null;
    }

    public void givenPlayerKeysWillBeGenerated(String ... keys) {
        keysToGenerate.addAll(Arrays.asList(keys));
    }
}

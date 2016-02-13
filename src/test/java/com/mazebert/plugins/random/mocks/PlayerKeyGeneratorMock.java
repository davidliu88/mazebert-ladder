package com.mazebert.plugins.random.mocks;

import com.mazebert.plugins.random.PlayerKeyGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerKeyGeneratorMock extends PlayerKeyGenerator {
    private List<String> keysToGenerate = new ArrayList<>();

    public PlayerKeyGeneratorMock() {
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

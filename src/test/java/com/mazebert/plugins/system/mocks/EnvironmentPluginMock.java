package com.mazebert.plugins.system.mocks;

import com.mazebert.plugins.system.EnvironmentPlugin;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentPluginMock implements EnvironmentPlugin {
    private Map<String, String> variables = new HashMap<>();

    @Override
    public String getEnvironmentVariable(String key) {
        return variables.get(key);
    }

    public void givenEnvironmentVariableExists(String key, String value) {
        variables.put(key, value);
    }
}
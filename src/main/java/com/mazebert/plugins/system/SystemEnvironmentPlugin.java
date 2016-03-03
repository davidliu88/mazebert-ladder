package com.mazebert.plugins.system;

public class SystemEnvironmentPlugin implements EnvironmentPlugin {
    @Override
    public String getEnvironmentVariable(String key) {
        return System.getenv(key);
    }
}

package com.mazebert.plugins.system;

public class EnvironmentPlugin {
    public String getEnvironmentVariable(String key) {
        return System.getenv(key);
    }
}

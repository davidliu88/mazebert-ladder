package com.mazebert.plugins.system.mocks;

public class EnvironmentPluginStub extends EnvironmentPluginMock {
    public EnvironmentPluginStub() {
        givenEnvironmentVariableExists("MAZEBERT_SETTINGS_FILE", "src/test/resources/Settings.json");
    }
}

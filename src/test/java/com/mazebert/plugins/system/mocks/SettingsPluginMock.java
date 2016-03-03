package com.mazebert.plugins.system.mocks;

import com.mazebert.plugins.system.AbstractSettingsPlugin;

import java.util.HashMap;
import java.util.Map;

public class SettingsPluginMock extends AbstractSettingsPlugin {
    private Map<String, String> properties = new HashMap<>();

    @Override
    public String getProperty(String id) {
        return properties.get(id);
    }

    public void givenPropertyExists(String key, String value) {
        properties.put(key, value);
    }
}

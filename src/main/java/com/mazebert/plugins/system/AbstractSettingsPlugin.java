package com.mazebert.plugins.system;

import com.mazebert.error.InternalServerError;

public abstract class AbstractSettingsPlugin implements SettingsPlugin {
    @Override
    public String getRequiredProperty(String id) {
        String property = getProperty(id);
        if (property == null) {
            throw new InternalServerError("Failed to read required property '" + id + "' from settings.");
        }
        return property;
    }
}

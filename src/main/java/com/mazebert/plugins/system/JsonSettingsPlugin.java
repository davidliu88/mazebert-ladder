package com.mazebert.plugins.system;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazebert.error.InternalServerError;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonSettingsPlugin extends AbstractSettingsPlugin {
    private final EnvironmentPlugin environmentPlugin;
    private final Map<String, String> properties;

    private static final String ENVIRONMENT_VARIABLE = "MAZEBERT_SETTINGS_FILE";

    @Inject
    public JsonSettingsPlugin(EnvironmentPlugin environmentPlugin) {
        this.environmentPlugin = environmentPlugin;
        this.properties = loadProperties();
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> loadProperties() {
        String settingsLocation = getSettingsLocation();

        try {
            JsonParser parser = new ObjectMapper().getFactory().createParser(new File(settingsLocation));
            return (Map<String, String>) parser.readValueAs(HashMap.class);
        } catch (FileNotFoundException e) {
            throw new InternalServerError("Failed to load settings. The file '" + settingsLocation + "' does not exist.", e);
        } catch (IOException e) {
            throw new InternalServerError("Failed to load settings. The file '" + settingsLocation + "' is not a valid json file.", e);
        }
    }

    private String getSettingsLocation() {
        String settingsLocation = environmentPlugin.getEnvironmentVariable(ENVIRONMENT_VARIABLE);
        if (settingsLocation == null) {
            throw new InternalServerError("Failed to load settings. Environment variable '" + ENVIRONMENT_VARIABLE + "' is not set.");
        }
        return settingsLocation;
    }

    @Override
    public String getProperty(String id) {
        return properties.get(id);
    }
}

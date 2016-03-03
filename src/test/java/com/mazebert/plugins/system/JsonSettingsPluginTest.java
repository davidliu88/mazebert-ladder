package com.mazebert.plugins.system;

import com.mazebert.error.Error;
import com.mazebert.error.InternalServerError;
import com.mazebert.plugins.system.mocks.EnvironmentPluginMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonSettingsPluginTest {
    private JsonSettingsPlugin settings;
    private EnvironmentPluginMock environmentPlugin = new EnvironmentPluginMock();
    private Error error;

    @Test
    public void nullFile() {
        environmentPlugin.givenEnvironmentVariableExists("MAZEBERT_SETTINGS_FILE", null);
        whenSettingsAreInitialized();
        thenErrorIs("Failed to load settings. Environment variable 'MAZEBERT_SETTINGS_FILE' is not set.");
    }

    @Test
    public void fileDoesNotExist() {
        environmentPlugin.givenEnvironmentVariableExists("MAZEBERT_SETTINGS_FILE", "src/test/resources/SettingsUnknown.json");
        whenSettingsAreInitialized();
        thenErrorIs("Failed to load settings. The file 'src/test/resources/SettingsUnknown.json' does not exist.");
    }

    @Test
    public void invalidFileFormat() {
        environmentPlugin.givenEnvironmentVariableExists("MAZEBERT_SETTINGS_FILE", "src/test/resources/SettingsInvalid.txt");
        whenSettingsAreInitialized();
        thenErrorIs("Failed to load settings. The file 'src/test/resources/SettingsInvalid.txt' is not a valid json file.");
    }

    @Test
    public void propertiesCanBeRead() {
        environmentPlugin.givenEnvironmentVariableExists("MAZEBERT_SETTINGS_FILE", "src/test/resources/Settings.json");
        whenSettingsAreInitialized();
        thenPropertiesCanBeRead();
    }

    @Test
    public void requiredPropertyDoesNotExist() {
        environmentPlugin.givenEnvironmentVariableExists("MAZEBERT_SETTINGS_FILE", "src/test/resources/Settings.json");
        whenSettingsAreInitialized();

        try {
            settings.getRequiredProperty("unknown");
        } catch (Error error) {
            this.error = error;
        }

        thenErrorIs("Failed to read required property 'unknown' from settings.");
    }

    private void thenPropertiesCanBeRead() {
        assertEquals("localhost/db", settings.getProperty(SettingsPlugin.DB_URL));
        assertEquals("user", settings.getProperty(SettingsPlugin.DB_USER));
        assertEquals("password", settings.getProperty(SettingsPlugin.DB_PASSWORD));
    }

    private void thenErrorIs(String message) {
        assertEquals(new InternalServerError(message), error);
    }

    private void whenSettingsAreInitialized() {
        try {
            settings = new JsonSettingsPlugin(environmentPlugin);
        } catch (Error error) {
            this.error = error;
        }
    }
}
package com.mazebert.plugins.system;

public interface SettingsPlugin {
    String DB_USER = "DB_USER";
    String DB_PASSWORD = "DB_PASSWORD";
    String DB_URL = "DB_URL";
    String EMAIL_PASSWORD = "EMAIL_PASSWORD";
    String PRIVATE_KEY = "PRIVATE_KEY";

    String getProperty(String id);
    String getRequiredProperty(String id);
}

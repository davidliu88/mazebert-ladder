package com.mazebert.plugins.validation;

import com.mazebert.entities.Version;
import com.mazebert.error.BadRequest;

public class VersionValidator {
    private final Version requiredVersion;

    public VersionValidator(String requiredVersion) {
        this.requiredVersion = new Version(requiredVersion);
    }

    public void validate(String version) {
        if (version == null) {
            throw new BadRequest("App version must not be null.");
        }
        if (new Version(version).compareTo(requiredVersion) < 0) {
            throw new BadRequest("At least app version " + requiredVersion.toString() + " is required for this request.");
        }
    }
}

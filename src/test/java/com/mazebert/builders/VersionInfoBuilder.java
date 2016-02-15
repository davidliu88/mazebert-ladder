package com.mazebert.builders;

import com.mazebert.entities.VersionInfo;
import org.jusecase.builders.Builder;

public class VersionInfoBuilder implements Builder<VersionInfo> {
    private VersionInfo versionInfo = new VersionInfo();

    public VersionInfoBuilder withVersion(String version) {
        versionInfo.setVersion(version);
        return this;
    }

    public VersionInfoBuilder withStore(String value) {
        versionInfo.setStore(value);
        return this;
    }

    @Override
    public VersionInfo build() {
        return versionInfo;
    }
}

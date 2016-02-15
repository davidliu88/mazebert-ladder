package com.mazebert.gateways.mocks;

import com.mazebert.entities.VersionInfo;
import com.mazebert.gateways.VersionInfoGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VersionInfoGatewayMock implements VersionInfoGateway {
    private List<VersionInfo> versionInfos = new ArrayList<>();

    @Override
    public VersionInfo findVersionInfo(String store) {
        Optional<VersionInfo> result = versionInfos.stream().filter(versionInfo -> store.equals(versionInfo.getStore())).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public void givenVersionInfo(VersionInfo versionInfo) {
        versionInfos.add(versionInfo);
    }

    public void givenNoVersionInfo() {
        versionInfos.clear();
    }
}

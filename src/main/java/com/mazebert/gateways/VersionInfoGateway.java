package com.mazebert.gateways;

import com.mazebert.entities.VersionInfo;

public interface VersionInfoGateway {
    VersionInfo findVersionInfo(String store);
}

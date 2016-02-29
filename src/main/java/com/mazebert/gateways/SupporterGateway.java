package com.mazebert.gateways;

import com.mazebert.entities.Supporter;

import java.util.List;

public interface SupporterGateway {
    List<Supporter> findAllSupporters();
}

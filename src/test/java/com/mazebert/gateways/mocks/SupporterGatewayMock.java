package com.mazebert.gateways.mocks;

import com.mazebert.entities.Supporter;
import com.mazebert.gateways.SupporterGateway;

import java.util.ArrayList;
import java.util.List;

public class SupporterGatewayMock implements SupporterGateway {
    private List<Supporter> supporters = new ArrayList<>();

    @Override
    public List<Supporter> findAllSupporters() {
        return supporters;
    }

    public void givenSupporters(List<Supporter> supporters) {
        this.supporters = supporters;
    }
}

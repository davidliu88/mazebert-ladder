package com.mazebert.gateways.mocks;

import com.mazebert.entities.BonusTime;
import com.mazebert.gateways.BonusTimeGateway;
import com.mazebert.usecases.bonusround.GetBonusTimes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BonusTimeGatewayMock implements BonusTimeGateway {
    private Map<String, List<BonusTime>> bonusTimes = new HashMap<>();


    @Override
    public List<BonusTime> findBonusTimes(GetBonusTimes.Request request) {
        return bonusTimes.get(
                "M" + request.mapId +
                "D" + request.difficultyType +
                "W" + request.waveAmountType +
                "V" + request.appVersion
        );
    }

    public void givenBonusTimes(String arguments, List<BonusTime> times) {
        bonusTimes.put(arguments, times);
    }
}

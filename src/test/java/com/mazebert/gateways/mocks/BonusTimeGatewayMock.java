package com.mazebert.gateways.mocks;

import com.mazebert.entities.PlayerBonusTime;
import com.mazebert.gateways.BonusTimeGateway;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BonusTimeGatewayMock implements BonusTimeGateway {
    private Map<String, List<PlayerBonusTime>> bonusTimes = new HashMap<>();
    private UpdateBonusTime.Request updateRequest;

    @Override
    public List<PlayerBonusTime> findBonusTimes(GetBonusTimes.Request request) {
        return bonusTimes.get(
                "M" + request.mapId +
                "D" + request.difficultyType +
                "W" + request.waveAmountType +
                "V" + request.appVersion
        );
    }

    @Override
    public void updateBonusTime(UpdateBonusTime.Request request) {
        updateRequest = request;
    }

    public void givenBonusTimes(String arguments, List<PlayerBonusTime> times) {
        bonusTimes.put(arguments, times);
    }

    public UpdateBonusTime.Request getUpdateRequest() {
        return updateRequest;
    }
}

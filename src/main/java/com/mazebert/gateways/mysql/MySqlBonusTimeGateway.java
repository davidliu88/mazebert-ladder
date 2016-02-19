package com.mazebert.gateways.mysql;

import com.mazebert.entities.BonusTime;
import com.mazebert.gateways.BonusTimeGateway;
import com.mazebert.usecases.bonusround.GetBonusTimes;

import java.util.List;

public class MySqlBonusTimeGateway implements BonusTimeGateway {
    @Override
    public List<BonusTime> findBonusTimes(GetBonusTimes.Request request) {
        return null;
    }
}

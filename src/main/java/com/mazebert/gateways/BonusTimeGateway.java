package com.mazebert.gateways;

import com.mazebert.entities.BonusTime;
import com.mazebert.usecases.bonusround.GetBonusTimes;

import java.util.List;

public interface BonusTimeGateway {
    List<BonusTime> findBonusTimes(GetBonusTimes.Request request);
}

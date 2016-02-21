package com.mazebert.gateways;

import com.mazebert.entities.PlayerBonusTime;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;

import java.util.List;

public interface BonusTimeGateway {
    List<PlayerBonusTime> findBonusTimes(GetBonusTimes.Request request);
    void updateBonusTime(UpdateBonusTime.Request request);
}

package com.mazebert.usecases.bonustime;

import com.mazebert.entities.PlayerBonusTime;
import com.mazebert.error.BadRequest;
import com.mazebert.gateways.BonusTimeGateway;
import org.jusecase.Usecase;

import javax.inject.Inject;
import java.util.List;

public class GetBonusTimes implements Usecase<GetBonusTimes.Request, List<PlayerBonusTime>> {
    private final BonusTimeGateway bonusTimeGateway;

    @Inject
    public GetBonusTimes(BonusTimeGateway bonusTimeGateway) {
        this.bonusTimeGateway = bonusTimeGateway;
    }

    public List<PlayerBonusTime> execute(Request request) {
        validateRequest(request);
        return bonusTimeGateway.findBonusTimes(request);
    }

    private void validateRequest(Request request) {
        if (request.mapId == 0) {
            request.mapId = 1;
        }

        if (request.difficultyType == null) throw new BadRequest("Difficulty type must not be null.");
        if (request.waveAmountType == null) throw new BadRequest("Wave amount type must not be null.");
        if (request.start < 0) throw new BadRequest("Start must not be negative.");
        if (request.limit < 0) throw new BadRequest("Limit must not be negative.");
    }

    public static class Request {
        public int mapId;
        public String difficultyType;
        public String waveAmountType;
        public String appVersion;
        public int start;
        public int limit;
    }
}

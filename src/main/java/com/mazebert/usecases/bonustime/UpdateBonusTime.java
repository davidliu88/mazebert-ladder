package com.mazebert.usecases.bonustime;

import com.mazebert.entities.Player;
import com.mazebert.entities.Version;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.error.Unauthorized;
import com.mazebert.gateways.BonusTimeGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.SecureRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;

public class UpdateBonusTime implements Usecase<UpdateBonusTime.Request, UpdateBonusTime.Response> {
    private final BonusTimeGateway bonusTimeGateway;
    private final PlayerGateway playerGateway;

    private static final Version minimumVersion = new Version("1.3.0");

    @Inject
    public UpdateBonusTime(BonusTimeGateway bonusTimeGateway, PlayerGateway playerGateway) {
        this.bonusTimeGateway = bonusTimeGateway;
        this.playerGateway = playerGateway;
    }

    public Response execute(Request request) {
        validateRequest(request);

        Player player = playerGateway.findPlayerByKey(request.key);
        if (player == null) {
            throw new NotFound("A player with this key could not be found.");
        }

        request.playerId = player.getId();
        bonusTimeGateway.updateBonusTime(request);

        return new Response();
    }

    private void validateRequest(Request request) {
        if (request.key == null) {
            throw new BadRequest("Player key is required.");
        }
        if (request.difficultyType < 0 || request.difficultyType >= BonusTimeGateway.DIFFICULTY_AMOUNT) {
            throw new BadRequest("Difficulty type must be in range [0, " + (BonusTimeGateway.DIFFICULTY_AMOUNT - 1) + "]");
        }
        if (request.waveAmountType < 0 || request.waveAmountType >= BonusTimeGateway.WAVE_TYPE_AMOUNT) {
            throw new BadRequest("Wave amount type must be in range [0, " + (BonusTimeGateway.WAVE_TYPE_AMOUNT - 1) + "]");
        }
        if (request.secondsSurvived <= 0) {
            throw new BadRequest("Survival time must be positive");
        }
        if (minimumVersion.compareTo(new Version(request.appVersion)) > 0) {
            throw new Unauthorized("This game version can no longer submit bonus round scores.");
        }
    }

    @SecureRequest
    @StatusResponse
    public static class Request {
        public long playerId;
        public String key;
        public String appVersion;
        public int mapId;
        public int secondsSurvived;
        public int difficultyType;
        public int waveAmountType;
    }

    public static class Response {
    }
}

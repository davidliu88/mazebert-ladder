package com.mazebert.usecases.bonustime;

import com.mazebert.entities.PlayerBonusTime;
import com.mazebert.gateways.BonusTimeGateway;
import org.jusecase.Usecase;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FixBonusTimesWithZeroMapId implements Usecase<FixBonusTimesWithZeroMapId.Request, FixBonusTimesWithZeroMapId.Response> {

    private final BonusTimeGateway bonusTimeGateway;
    private final Logger logger;

    public FixBonusTimesWithZeroMapId(BonusTimeGateway bonusTimeGateway, Logger logger) {
        this.bonusTimeGateway = bonusTimeGateway;
        this.logger = logger;
    }

    @Override
    public Response execute(Request request) {
        fix();
        return null;
    }

    private void fix() {
        GetBonusTimes.Request request = new GetBonusTimes.Request();
        request.mapId = 0;
        request.appVersion = "*";
        request.start = 0;
        request.limit = 10000000;

        for (int difficultyType = 0; difficultyType < 3; ++difficultyType) {
            for (int waveAmountType = 0; waveAmountType < 4; ++waveAmountType) {
                request.difficultyType = "" + difficultyType;
                request.waveAmountType = "" + waveAmountType;

                List<PlayerBonusTime> zeroMapIdScores = bonusTimeGateway.findBonusTimes(request);
                updateAsMapOneScores(zeroMapIdScores, difficultyType, waveAmountType);
            }
        }

        logger.log(Level.INFO, "Done. Now you should delete all scores with mapId=0");
    }

    private void updateAsMapOneScores(List<PlayerBonusTime> zeroMapIdScores, int difficultyType, int waveAmountType) {
        logger.log(Level.INFO, "Fixing " + zeroMapIdScores.size() + " scores for d=" + difficultyType + ", w=" + waveAmountType);

        UpdateBonusTime.Request request = new UpdateBonusTime.Request();
        request.mapId = 1;
        request.appVersion = "1.3.1";
        request.difficultyType = difficultyType;
        request.waveAmountType = waveAmountType;

        for (PlayerBonusTime zeroMapIdScore : zeroMapIdScores) {
            request.playerId = zeroMapIdScore.getId();
            request.secondsSurvived = zeroMapIdScore.getBonusTime();
            bonusTimeGateway.updateBonusTime(request);

            logger.log(Level.INFO, "Fixing score for player " + zeroMapIdScore.getName() + " (" + zeroMapIdScore.getBonusTime() + "s)");
        }
    }

    public static class Request {
    }

    public static class Response {
    }
}

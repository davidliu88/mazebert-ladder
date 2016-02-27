package com.mazebert.usecases.quest;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.entities.Version;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.QuestGateway;
import com.mazebert.plugins.random.DailyQuestGenerator;
import com.mazebert.plugins.random.RandomNumberGenerator;
import com.mazebert.plugins.time.CurrentDatePlugin;
import com.mazebert.plugins.time.TimeZoneParser;
import com.mazebert.plugins.validation.VersionValidator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.SecureRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;
import java.util.List;

public class ReplaceQuest implements Usecase<ReplaceQuest.Request, ReplaceQuest.Response> {
    private final VersionValidator versionValidator = new VersionValidator("1.0.0");
    private final PlayerGateway playerGateway;
    private final QuestGateway questGateway;
    private final DailyQuestGenerator questGenerator;
    private final TimeZoneParser timeZoneParser;

    @Inject
    public ReplaceQuest(PlayerGateway playerGateway,
                        QuestGateway questGateway,
                        FoilCardGateway foilCardGateway,
                        CurrentDatePlugin currentDatePlugin,
                        RandomNumberGenerator randomNumberGenerator) {
        this.playerGateway = playerGateway;
        this.questGateway = questGateway;
        this.questGenerator = new DailyQuestGenerator(questGateway, foilCardGateway, currentDatePlugin, randomNumberGenerator);
        this.timeZoneParser = new TimeZoneParser();
    }

    public Response execute(Request request) {
        validateRequest(request);

        Player player = playerGateway.findPlayerByKey(request.key);
        if (player == null) {
            throw new NotFound("A player with this key does not exist.");
        }

        List<Long> dailyQuestIds = questGateway.findDailyQuestIds(player.getId());
        if (! dailyQuestIds.contains(request.questId)) {
            throw new NotFound("The player currently has no daily quest with this ID.");
        }

        Response response = new Response();
        response.quest = questGenerator.tryToReplaceDailyQuest(player,
                new Version(request.appVersion),
                timeZoneParser.parseAppOffset(request.timeZoneOffset),
                dailyQuestIds, request.questId);
        return response;
    }

    private void validateRequest(Request request) {
        versionValidator.validate(request.appVersion);
        if (request.key == null) {
            throw new BadRequest("Player key must not be null.");
        }
    }

    @SecureRequest
    @StatusResponse(field = "quest")
    public static class Request {
        public String appVersion;
        public String key;
        public long questId;
        public int timeZoneOffset;
    }

    public static class Response {
        public Quest quest;
    }
}

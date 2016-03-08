package com.mazebert.usecases.quest;

import com.mazebert.entities.CardType;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.QuestGateway;
import com.mazebert.gateways.transaction.TransactionRunner;
import com.mazebert.plugins.validation.VersionValidator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.VerifyRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;
import java.util.List;

public class CompleteQuests implements Usecase<CompleteQuests.Request, CompleteQuests.Response> {
    private final VersionValidator versionValidator = new VersionValidator("1.0.0");
    private final TransactionRunner transactionRunner;
    private final PlayerGateway playerGateway;
    private final QuestGateway questGateway;
    private final FoilCardGateway foilCardGateway;

    @Inject
    public CompleteQuests(TransactionRunner transactionRunner, PlayerGateway playerGateway, QuestGateway questGateway, FoilCardGateway foilCardGateway) {
        this.transactionRunner = transactionRunner;
        this.playerGateway = playerGateway;
        this.questGateway = questGateway;
        this.foilCardGateway = foilCardGateway;
    }

    public Response execute(Request request) {
        validateRequest(request);
        return completeQuests(request);
    }

    private Response completeQuests(Request request) {
        Player player = getPlayer(request);
        doQuestTransactions(request, player);
        return createResponse(player);
    }

    private Player getPlayer(Request request) {
        Player player = playerGateway.findPlayerByKey(request.key);
        if (player == null) {
            throw new NotFound("No player with this key exists.");
        }
        return player;
    }

    private void doQuestTransactions(Request request, Player player) {
        List<Quest> quests = questGateway.findQuestsByIds(request.questTransactionIds);
        List<Long> hiddenQuestIds = questGateway.findCompletedHiddenQuestIds(player.getId());
        List<Long> dailyQuestIds = questGateway.findDailyQuestIds(player.getId());

        transactionRunner.runAsTransaction(() -> {
            int reward = 0;
            for (Quest quest : quests) {
                reward += completeQuest(player, hiddenQuestIds, dailyQuestIds, quest);
            }

            playerGateway.addRelics(player.getId(), reward);
        });
    }

    private Response createResponse(Player player) {
        Response response = new Response();
        response.relics = playerGateway.getRelics(player.getId());
        return response;
    }

    private int completeQuest(Player player, List<Long> hiddenQuestIds, List<Long> dailyQuestIds, Quest quest) {
        if (quest.isHidden()) {
            return completeHiddenQuest(player, quest, hiddenQuestIds);
        } else {
            return completeDailyQuest(player, quest, dailyQuestIds);
        }
    }

    private int completeHiddenQuest(Player player, Quest quest, List<Long> hiddenQuestIds) {
        if (!hiddenQuestIds.contains(quest.getId())) {
            questGateway.addCompletedHiddenQuestId(player.getId(), quest.getId());
            unlockSpecialHiddenQuestRewards(player, quest);
            return quest.getReward();
        }
        return 0;
    }

    private int completeDailyQuest(Player player, Quest quest, List<Long> dailyQuestIds) {
        if (dailyQuestIds.contains(quest.getId())) {
            questGateway.removeDailyQuest(player, quest.getId());
            return quest.getReward();
        }
        return 0;
    }

    private void unlockSpecialHiddenQuestRewards(Player player, Quest quest) {
        unlockGoldenScepterOfTimeIfRequired(player, quest);
    }

    private void unlockGoldenScepterOfTimeIfRequired(Player player, Quest quest) {
        if (quest.getId() == 11) {
            FoilCard foilCard = new FoilCard();
            foilCard.setCardId(29);
            foilCard.setCardType(CardType.ITEM);
            foilCardGateway.addFoilCardToPlayer(player.getId(), foilCard);
        }
    }

    private void validateRequest(Request request) {
        versionValidator.validate(request.appVersion);
        if (request.key == null) {
            throw new BadRequest("Player key must be provided.");
        }
        if (request.questTransactionIds == null) {
            throw new BadRequest("Quest transaction IDs need to be provided.");
        }
    }

    @VerifyRequest
    @StatusResponse
    public static class Request {
        public String appVersion;
        public String key;
        public List<Long> questTransactionIds;
    }

    public static class Response {
        public int relics;
    }
}

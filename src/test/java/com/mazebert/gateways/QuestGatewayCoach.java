package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestGatewayCoach implements QuestGateway {
    private Map<Long, List<Long>> completedHiddenQuestIds = new HashMap<>();
    private Map<Long, List<Quest>> dailyQuests = new HashMap<>();

    @Override
    public List<Long> findCompletedHiddenQuestIds(long playerId) {
        return completedHiddenQuestIds.get(playerId);
    }

    @Override
    public List<Quest> findDailyQuests(long playerId) {
        return dailyQuests.get(playerId);
    }

    public void givenCompletedHiddenQuestIdsForPlayer(Player player, List<Long> questIds) {
        completedHiddenQuestIds.put(player.getId(), questIds);
    }

    public void givenDailyQuestsForPlayer(Player player, List<Quest> quests) {
        dailyQuests.put(player.getId(), quests);
    }
}

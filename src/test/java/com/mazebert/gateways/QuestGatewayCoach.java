package com.mazebert.gateways;

import com.mazebert.entities.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestGatewayCoach implements QuestGateway {
    private Map<Long, List<Long>> completedHiddenQuestIds = new HashMap<>();

    @Override
    public List<Long> findCompletedHiddenQuestIds(long playerId) {
        return completedHiddenQuestIds.get(playerId);
    }

    public void givenCompletedHiddenQuestIdsForPlayer(Player player, List<Long> questIds) {
        completedHiddenQuestIds.put(player.getId(), questIds);
    }
}

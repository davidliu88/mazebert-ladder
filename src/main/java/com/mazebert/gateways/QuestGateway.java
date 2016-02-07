package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;

import java.util.List;
import java.util.Set;

public interface QuestGateway {
    List<Long> findCompletedHiddenQuestIds(long playerId);
    List<Quest> findDailyQuests(long playerId);
    Set<Long> findDailyQuestIds(long playerId);
    List<Quest> findAllQuests();
    void addDailyQuest(Player player, Quest quest);
}

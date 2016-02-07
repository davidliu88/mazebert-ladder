package com.mazebert.gateways;

import com.mazebert.entities.Quest;

import java.util.List;

public interface QuestGateway {
    List<Long> findCompletedHiddenQuestIds(long playerId);
    List<Quest> findDailyQuests(long playerId);
}

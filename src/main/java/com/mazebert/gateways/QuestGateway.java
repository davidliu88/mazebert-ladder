package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;

import java.util.Date;
import java.util.List;

public interface QuestGateway {
    List<Long> findCompletedHiddenQuestIds(long playerId);
    List<Quest> findDailyQuests(long playerId);
    List<Long> findDailyQuestIds(long playerId);
    List<Quest> findAllQuests();
    List<Quest> findQuestsByIds(List<Long> questIds);
    void addDailyQuest(Player player, Quest quest, Date creationDate);
    void removeDailyQuest(Player player, long questId);
    void replaceDailyQuest(long playerId, long oldQuestId, long newQuestId, Date creationDate);
    void addCompletedHiddenQuestId(long playerId, long questId);
}

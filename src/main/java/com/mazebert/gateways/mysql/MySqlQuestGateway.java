package com.mazebert.gateways.mysql;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.gateways.QuestGateway;

import java.util.List;
import java.util.Set;

public class MySqlQuestGateway implements QuestGateway {
    @Override
    public List<Long> findCompletedHiddenQuestIds(long playerId) {
        return null;
    }

    @Override
    public List<Quest> findDailyQuests(long playerId) {
        return null;
    }

    @Override
    public Set<Long> findDailyQuestIds(long playerId) {
        return null;
    }

    @Override
    public List<Quest> findAllQuests() {
        return null;
    }

    @Override
    public void addDailyQuest(Player player, Quest quest) {

    }
}

package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;

import java.util.*;

public class QuestGatewayCoach implements QuestGateway {
    private Map<Long, List<Long>> completedHiddenQuestIds = new HashMap<>();
    private Map<Long, List<Quest>> dailyQuests = new HashMap<>();
    private List<Quest> allQuests = new ArrayList<>();

    @Override
    public List<Long> findCompletedHiddenQuestIds(long playerId) {
        return completedHiddenQuestIds.get(playerId);
    }

    @Override
    public List<Quest> findDailyQuests(long playerId) {
        return dailyQuests.get(playerId);
    }

    @Override
    public Set<Long> findDailyQuestIds(long playerId) {
        Set<Long> dailyQuestIds = new HashSet<>();
        List<Quest> dailyQuests = findDailyQuests(playerId);
        if (dailyQuests != null) {
            for (Quest quest : dailyQuests) {
                dailyQuestIds.add(quest.getId());
            }
        }
        return dailyQuestIds;
    }

    @Override
    public List<Quest> findAllQuests() {
        return allQuests;
    }

    @Override
    public void addDailyQuest(Player player, Quest quest) {
        if (!dailyQuests.containsKey(player.getId())) {
            dailyQuests.put(player.getId(), new ArrayList<>());
        }

        dailyQuests.get(player.getId()).add(quest);
    }

    public void givenCompletedHiddenQuestIdsForPlayer(Player player, List<Long> questIds) {
        completedHiddenQuestIds.put(player.getId(), questIds);
    }

    public void givenDailyQuestsForPlayer(Player player, List<Quest> quests) {
        dailyQuests.put(player.getId(), quests);
    }

    public void givenQuests(List<Quest> quests) {
        allQuests = quests;
    }
}

package com.mazebert.gateways.mocks;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.gateways.QuestGateway;

import java.util.*;
import java.util.stream.Collectors;

public class QuestGatewayMock implements QuestGateway {
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
    public List<Long> findDailyQuestIds(long playerId) {
        List<Long> dailyQuestIds = new ArrayList<>();
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
    public List<Quest> findQuestsByIds(List<Long> questIds) {
        return allQuests.stream().filter(quest -> questIds.contains(quest.getId())).collect(Collectors.toList());
    }

    @Override
    public void addDailyQuest(Player player, Quest quest, Date creationDate) {
        if (!dailyQuests.containsKey(player.getId())) {
            dailyQuests.put(player.getId(), new ArrayList<>());
        }

        dailyQuests.get(player.getId()).add(quest);
    }

    @Override
    public void removeDailyQuest(Player player, long questId) {
        List<Quest> dailyQuests = this.dailyQuests.get(player.getId());
        if (dailyQuests != null) {
            for (Iterator<Quest> it = dailyQuests.iterator(); it.hasNext(); ) {
                Quest quest = it.next();
                if (quest.getId() == questId) {
                    it.remove();
                }
            }
        }
    }

    @Override
    public void addCompletedHiddenQuestId(long playerId, long questId) {
        List<Long> ids = completedHiddenQuestIds.get(playerId);
        if (ids == null) {
            ids = new ArrayList<>();
            completedHiddenQuestIds.put(playerId, ids);
        }
        ids.add(questId);
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

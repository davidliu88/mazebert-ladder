package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.mazebert.builders.BuilderFactory.player;
import static com.mazebert.builders.BuilderFactory.quest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.date;

public abstract class QuestGatewayTest extends GatewayTest<QuestGateway> {
    private Player player = a(player().casid());

    @Test
    public void findCompletedHiddenQuestIds_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findCompletedHiddenQuestIds(player.getId()));
        thenGatewayErrorIs("Failed to find completed hidden quest ids for player.");
    }

    @Test
    public void findCompletedHiddenQuestIds_noCompletedQuests() {
        List<Long> ids = gateway.findCompletedHiddenQuestIds(player.getId());
        assertEquals(0, ids.size());
    }

    @Test
    public void findCompletedHiddenQuestIds_oneCompletedQuest() {
        gateway.addCompletedHiddenQuestId(player.getId(), 10);
        List<Long> ids = gateway.findCompletedHiddenQuestIds(player.getId());

        assertEquals(1, ids.size());
        assertEquals(10, ids.get(0).longValue());
    }

    @Test
    public void findCompletedHiddenQuestIds_playerMustMatch() {
        gateway.addCompletedHiddenQuestId(1000, 10);
        List<Long> ids = gateway.findCompletedHiddenQuestIds(player.getId());

        assertEquals(0, ids.size());
    }

    @Test
    public void findDailyQuests_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findDailyQuests(player.getId()));
        thenGatewayErrorIs("Failed to find daily quests for player.");
    }

    @Test
    public void findDailyQuests_noQuests() {
        List<Quest> quests = gateway.findDailyQuests(player.getId());
        assertEquals(0, quests.size());
    }

    @Test
    public void findDailyQuests_oneQuest() {
        gateway.addDailyQuest(player, a(quest().rollStrikesWithBowlingBall()), new Date());
        List<Quest> quests = gateway.findDailyQuests(player.getId());

        assertEquals(1, quests.size());
        assertEquals(20, quests.get(0).getId());
        assertEquals(5, quests.get(0).getRequiredAmount());
        assertEquals(40, quests.get(0).getReward());
        assertEquals(false, quests.get(0).isHidden());
        assertEquals("1.2.0", quests.get(0).getSinceVersion());
    }

    @Test
    public void findDailyQuests_questsAreSortedByDate() {
        gateway.addDailyQuest(player, a(quest().withId(1)), a(date().with("2015-10-10 09:00:00")));
        gateway.addDailyQuest(player, a(quest().withId(2)), a(date().with("2015-10-10 06:00:00")));
        gateway.addDailyQuest(player, a(quest().withId(3)), a(date().with("2015-10-11 17:00:00")));
        List<Quest> quests = gateway.findDailyQuests(player.getId());

        assertEquals(3, quests.size());
        assertEquals(2, quests.get(0).getId());
        assertEquals(1, quests.get(1).getId());
        assertEquals(3, quests.get(2).getId());
    }

    @Test
    public void findDailyQuests_playerIdMustMatch() {
        gateway.addDailyQuest(a(player().withId(1000)), a(quest().withId(1)), new Date());
        List<Quest> quests = gateway.findDailyQuests(player.getId());

        assertEquals(0, quests.size());
    }

    @Test
    public void findDailyQuestIds_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findDailyQuestIds(player.getId()));
        thenGatewayErrorIs("Failed to determine daily quest ids for player.");
    }

    @Test
    public void findDailyQuestIds_noQuests() {
        List<Long> ids = gateway.findDailyQuestIds(player.getId());
        assertEquals(0, ids.size());
    }

    @Test
    public void findDailyQuestIds_oneQuest() {
        gateway.addDailyQuest(player, a(quest().rollStrikesWithBowlingBall()), new Date());
        List<Long> ids = gateway.findDailyQuestIds(player.getId());

        assertEquals(1, ids.size());
    }

    @Test
    public void findDailyQuestIds_questsAreSortedByDate() {
        gateway.addDailyQuest(player, a(quest().withId(1)), a(date().with("2015-10-10 09:00:00")));
        gateway.addDailyQuest(player, a(quest().withId(2)), a(date().with("2015-10-10 06:00:00")));
        gateway.addDailyQuest(player, a(quest().withId(3)), a(date().with("2015-10-11 17:00:00")));
        List<Long> ids = gateway.findDailyQuestIds(player.getId());

        assertEquals(3, ids.size());
        assertEquals(2, ids.get(0).longValue());
        assertEquals(1, ids.get(1).longValue());
        assertEquals(3, ids.get(2).longValue());
    }

    @Test
    public void findDailyQuestIds_playerIdMustMatch() {
        gateway.addDailyQuest(a(player().withId(1000)), a(quest().withId(1)), new Date());
        List<Long> ids = gateway.findDailyQuestIds(player.getId());

        assertEquals(0, ids.size());
    }

    @Test
    public void findAllQuests_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findAllQuests());
        thenGatewayErrorIs("Failed to determine all available quests.");
    }

    @Test
    public void findAllQuests_propertiesAreFilledCorrectly() {
        Quest quest = findQuestWithId(1, gateway.findAllQuests());

        assertNotNull(quest);
        assertEquals(7, quest.getRequiredAmount());
        assertEquals(40, quest.getReward());
        assertEquals(false, quest.isHidden());
        assertEquals("1.0.0", quest.getSinceVersion());
    }

    @Test
    public void addDailyQuest_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.addDailyQuest(player, a(quest().rollStrikesWithBowlingBall()), new Date()));
        thenGatewayErrorIs("Failed to add daily quest to player.");
    }

    @Test
    public void addCompletedHiddenQuestId_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.addCompletedHiddenQuestId(player.getId(), 10));
        thenGatewayErrorIs("Failed to add a completed hidden quest to player.");
    }

    private Quest findQuestWithId(long id, List<Quest> quests) {
        for (Quest quest : quests) {
            if (quest.getId() == id) {
                return quest;
            }
        }
        return null;
    }
}

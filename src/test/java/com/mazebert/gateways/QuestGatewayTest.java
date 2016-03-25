package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.gateways.error.KeyAlreadyExists;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.mazebert.builders.BuilderFactory.player;
import static com.mazebert.builders.BuilderFactory.quest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.jusecase.Builders.*;

public abstract class QuestGatewayTest extends GatewayTest<QuestGateway> {
    protected PlayerGateway playerGateway;

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

    @Test
    public void addCompletedHiddenQuestId_alreadyExists() {
        gateway.addCompletedHiddenQuestId(player.getId(), 1);

        try {
            gateway.addCompletedHiddenQuestId(player.getId(), 1);
        } catch (KeyAlreadyExists error) {
            this.error = error;
        }

        assertNotNull(error);
    }

    @Test
    public void findQuestsByIds_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findQuestsByIds(a(list(1L))));
        thenGatewayErrorIs("Failed to find quests by ids in database.");
    }

    @Test
    public void findQuestsByIds_noQuestIds() {
        List<Quest> quests = gateway.findQuestsByIds(a(list()));

        assertEquals(0, quests.size());
    }

    @Test
    public void findQuestsByIds_propertiesAreMappedCorrectly() {
        List<Quest> quests = gateway.findQuestsByIds(a(list(1L, 2L, 19L)));

        assertEquals(3, quests.size());
        assertEquals(1L, quests.get(0).getId());
        assertEquals(2L, quests.get(1).getId());
        assertEquals(19L, quests.get(2).getId());

        Quest quest = quests.get(0);
        assertEquals(7, quest.getRequiredAmount());
        assertEquals(40, quest.getReward());
        assertEquals(false, quest.isHidden());
        assertEquals("1.0.0", quest.getSinceVersion());

        quest = quests.get(2);
        assertEquals(1, quest.getRequiredAmount());
        assertEquals(250, quest.getReward());
        assertEquals(true, quest.isHidden());
        assertEquals("1.2.0", quest.getSinceVersion());
    }

    @Test
    public void removeDailyQuest_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.removeDailyQuest(player, 1L));
        thenGatewayErrorIs("Failed to remove daily quest in database.");
    }

    @Test
    public void removeDailyQuest_questDoesNotExist() {
        gateway.addDailyQuest(player, a(quest().withId(4)), new Date());

        gateway.removeDailyQuest(player, 10L);

        List<Quest> quests = gateway.findDailyQuests(player.getId());
        assertEquals(1, quests.size());
    }

    @Test
    public void removeDailyQuest_questIsRemoved() {
        gateway.addDailyQuest(player, a(quest().withId(4)), new Date());
        gateway.addDailyQuest(player, a(quest().withId(10)), new Date());

        gateway.removeDailyQuest(player, 10L);

        List<Quest> quests = gateway.findDailyQuests(player.getId());
        assertEquals(1, quests.size());
        assertEquals(4, quests.get(0).getId());
    }

    @Test
    public void replaceDailyQuest_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.replaceDailyQuest(player.getId(), 1L, 2L, a(date())));
        thenGatewayErrorIs("Failed to replace daily quest in database.");
    }

    @Test
    public void replaceDailyQuest_questIsReplaced() {
        gateway.addDailyQuest(player, a(quest().withId(4)), new Date());
        gateway.addDailyQuest(player, a(quest().withId(10)), new Date());

        gateway.replaceDailyQuest(player.getId(), 4L, 5L, a(date()));

        List<Quest> quests = gateway.findDailyQuests(player.getId());
        assertEquals(2, quests.size());
        assertEquals(5L, quests.get(0).getId());
    }

    @Test
    public void replaceDailyQuest_lastQuestCreationForPlayerIsUpdated() {
        playerGateway.addPlayer(player);
        gateway.addDailyQuest(player, a(quest().withId(4)), a(date()));

        gateway.replaceDailyQuest(player.getId(), 4L, 5L, a(date().with("2015-10-10 10:15:22")));

        thenLastQuestCreationForPlayerIs(a(date().with("2015-10-10 10:15:22")));
    }

    @Test
    public void addDailyQuest_lastQuestCreationForPlayerIsUpdated() {
        playerGateway.addPlayer(player);
        gateway.addDailyQuest(player, a(quest().withId(4)), a(date().with("2015-10-10 10:15:22")));
        thenLastQuestCreationForPlayerIs(a(date().with("2015-10-10 10:15:22")));
    }

    private void thenLastQuestCreationForPlayerIs(Date expected) {
        Player p = playerGateway.findPlayerByKey(player.getKey());
        assertEquals(expected, p.getLastQuestCreation());
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

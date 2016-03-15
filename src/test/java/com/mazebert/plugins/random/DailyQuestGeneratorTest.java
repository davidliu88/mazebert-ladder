package com.mazebert.plugins.random;

import com.mazebert.builders.QuestBuilder;
import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.entities.Version;
import com.mazebert.gateways.mocks.FoilCardGatewayMock;
import com.mazebert.gateways.mocks.QuestGatewayMock;
import com.mazebert.plugins.random.mocks.RandomNumberGeneratorMock;
import com.mazebert.plugins.time.mocks.CurrentDatePluginMock;
import com.mazebert.plugins.time.TimeZoneParser;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static com.mazebert.builders.BuilderFactory.*;
import static org.junit.Assert.*;
import static org.jusecase.Builders.*;

public class DailyQuestGeneratorTest {
    private DailyQuestGenerator questGenerator;

    private QuestGatewayMock questGateway = new QuestGatewayMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();
    private RandomNumberGeneratorMock randomNumberGenerator = new RandomNumberGeneratorMock();

    private Player player;
    private Version version;
    private int timeZoneOffset;

    private Quest quest;

    @Before
    public void setUp() throws Exception {
        questGenerator = new DailyQuestGenerator(questGateway, foilCardGateway, currentDatePlugin, randomNumberGenerator);

        player = a(player().casid());
        version = new Version("2.0.0");

        questGateway.givenQuests(a(list(
                a(goldenQuest()))
        ));
    }

    @Test
    public void hiddenQuest() {
        questGateway.givenQuests(a(list(
                a(goldenQuest().hidden()))
        ));
        whenTryToGenerateDailyQuest();
        thenNoQuestIsGenerated();
    }

    @Test
    public void incompatibleQuestVersion() {
        version = new Version("1.0.0");
        questGateway.givenQuests(a(list(
                a(goldenQuest().withSinceVersion("1.0.1")))
        ));
        whenTryToGenerateDailyQuest();
        thenNoQuestIsGenerated();
    }

    @Test
    public void questAlreadyOwnedByPlayer() {
        questGateway.givenQuests(a(list(
                a(goldenQuest().withId(200)))
        ));
        questGateway.givenDailyQuestsForPlayer(player, a(list(
                a(goldenQuest().withId(200))
        )));
        whenTryToGenerateDailyQuest();
        thenNoQuestIsGenerated();
    }

    @Test
    public void playerAlreadyHasMaximumAmountOfQuests() {
        questGateway.givenDailyQuestsForPlayer(player, a(list(
                a(goldenQuest().withId(1)),
                a(goldenQuest().withId(2)),
                a(goldenQuest().withId(3))
        )));
        whenTryToGenerateDailyQuest();
        thenNoQuestIsGenerated();
    }

    @Test
    public void completelyNewPlayer() {
        player = a(player().casid().withLastQuestCreation(null));
        whenTryToGenerateDailyQuest();
        thenQuestIsGenerated();
    }

    @Test
    public void playerAlreadyCreatedOneQuestToday() {
        player = a(player().casid().withLastQuestCreation(a(date().with("2016-02-02 15:00:00"))));
        currentDatePlugin.givenCurrentDate(a(date().with("2016-02-02 19:00:00")));
        whenTryToGenerateDailyQuest();
        thenNoQuestIsGenerated();
    }

    @Test
    public void playerCreatedOneQuestYesterday() {
        player = a(player().casid().withLastQuestCreation(a(date().with("2016-02-02 15:00:00"))));
        currentDatePlugin.givenCurrentDate(a(date().with("2016-02-03 00:00:01")));
        whenTryToGenerateDailyQuest();
        thenOneQuestIsGenerated();
    }

    @Test
    public void playerCreatedOneQuestOneWeekBefore() {
        player = a(player().casid().withLastQuestCreation(a(date().with("2016-01-26 15:00:00"))));
        currentDatePlugin.givenCurrentDate(a(date().with("2016-02-02 19:00:00")));
        whenTryToGenerateDailyQuest();
        thenOneQuestIsGenerated();
    }

    @Test
    public void timeZoneIsRespectedForTodayCalculation() {
        timeZoneOffset = -1;
        player = a(player().casid().withLastQuestCreation(a(date().with("2016-02-02 15:00:00"))));
        currentDatePlugin.givenCurrentDate(a(date().with("2016-02-03 00:00:01")));
        whenTryToGenerateDailyQuest();
        thenNoQuestIsGenerated();
    }

    @Test
    public void randomQuestIsGenerated() {
        questGateway.givenQuests(a(list(
                a(goldenQuest().withId(200)),
                a(goldenQuest().withId(201)),
                a(goldenQuest().withId(203))
        )));
        randomNumberGenerator.givenRandomIntegers(2);

        whenTryToGenerateDailyQuest();

        assertEquals(203, quest.getId());
    }

    @Test
    public void playerHasNoBowlingBall() {
        questGateway.givenQuests(a(list(
                a(quest().rollStrikesWithBowlingBall()))
        ));
        whenTryToGenerateDailyQuest();
        thenNoQuestIsGenerated();
    }

    @Test
    public void playerHasBowlingBall() {
        Quest expected = a(quest().rollStrikesWithBowlingBall());
        questGateway.givenQuests(a(list(expected)));
        foilCardGateway.givenFoilCardsForPlayer(player, a(list(
                a(foilCard().bowlingBall())
        )));
        whenTryToGenerateDailyQuest();
        thenQuestIsGenerated(expected);
    }

    private QuestBuilder goldenQuest() {
        return new QuestBuilder()
                .withIsHidden(false)
                .withSinceVersion("0.9.0");
    }

    private void thenNoQuestIsGenerated() {
        assertNull(quest);
    }

    private void thenOneQuestIsGenerated() {
        assertNotNull(quest);
    }

    private void thenQuestIsGenerated() {
        assertNotNull(quest);
    }

    private void thenQuestIsGenerated(Quest expected) {
        assertEquals(expected, quest);
    }

    private void whenTryToGenerateDailyQuest() {
        TimeZone timeZone = new TimeZoneParser().parseOffset(timeZoneOffset);
        quest = questGenerator.tryToGenerateDailyQuest(player, version, timeZone);
    }
}
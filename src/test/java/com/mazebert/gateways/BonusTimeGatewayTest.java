package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.PlayerBonusTime;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.GetBonusTimesTest;
import com.mazebert.usecases.bonustime.UpdateBonusTime;
import com.mazebert.usecases.bonustime.UpdateBonusTimeTest;
import org.junit.Test;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;

public abstract class BonusTimeGatewayTest extends GatewayTest<BonusTimeGateway> {
    protected PlayerGateway playerGateway;

    private List<PlayerBonusTime> bonusTimes;

    @Test
    public void findBonusTimes_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findBonusTimes(a(findRequest())));
        thenGatewayErrorIs("Could not find bonus times in database.");
    }

    @Test
    public void findBonusTimes_noScores() {
        List<PlayerBonusTime> times = gateway.findBonusTimes(a(findRequest()));
        assertEquals(0, times.size());
    }

    @Test
    public void findBonusTimes_oneScore_propertiesAreFilled() {
        givenPlayerExists(a(player().casid()
                .withName("Lonely Wolf")
        ));
        givenBonusTimeExists(a(updateRequest()
                .withPlayerId(1)
                .withSecondsSurvived(2531)
        ));

        whenFindingBonusTimes(a(findRequest()));

        assertEquals(1, bonusTimes.size());
        PlayerBonusTime entry = bonusTimes.get(0);
        assertEquals(1, entry.getId());
        assertEquals("Lonely Wolf", entry.getName());
        assertEquals(2531, entry.getBonusTime());
    }

    @Test
    public void findBonusTimes_rankIsCalculated() {
        givenDefaultEntriesAndPlayers();

        whenFindingBonusTimes(a(findRequest()));

        assertEquals(3, bonusTimes.size());
        assertEquals(1, bonusTimes.get(0).getRank());
        assertEquals(2, bonusTimes.get(1).getRank());
        assertEquals(3, bonusTimes.get(2).getRank());
    }

    @Test
    public void findBonusTimes_startOffset_rankIsCalculated() {
        givenDefaultEntriesAndPlayers();

        whenFindingBonusTimes(a(findRequest().withStart(1)));

        assertEquals(2, bonusTimes.size());
        assertEquals(2, bonusTimes.get(0).getRank());
        assertEquals(3, bonusTimes.get(1).getRank());
    }

    @Test
    public void findBonusTimes_limitIsConsidered() {
        givenDefaultEntriesAndPlayers();
        whenFindingBonusTimes(a(findRequest().withLimit(1)));
        assertEquals(1, bonusTimes.size());
    }

    @Test
    public void findBonusTimes_orderedBySurvivalTime() {
        givenDefaultEntriesAndPlayers();

        whenFindingBonusTimes(a(findRequest()));

        assertEquals("Pro", bonusTimes.get(0).getName());
        assertEquals("Advanced", bonusTimes.get(1).getName());
        assertEquals("Casual", bonusTimes.get(2).getName());
    }

    @Test
    public void findBonusTimes_sameTime_orderedByPlayerId() {
        givenPlayerExists(a(player().casid().withKey("a").withName("1st signed up")));
        givenPlayerExists(a(player().casid().withKey("b").withName("2nd signed up")));
        givenPlayerExists(a(player().casid().withKey("c").withName("3rd signed up")));

        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withSecondsSurvived(100)));
        givenBonusTimeExists(a(updateRequest().withPlayerId(2).withSecondsSurvived(100)));
        givenBonusTimeExists(a(updateRequest().withPlayerId(3).withSecondsSurvived(100)));

        whenFindingBonusTimes(a(findRequest()));

        assertEquals("1st signed up", bonusTimes.get(0).getName());
        assertEquals("2nd signed up", bonusTimes.get(1).getName());
        assertEquals("3rd signed up", bonusTimes.get(2).getName());
    }

    @Test
    public void findBonusTimes_maxBonusTimeIsCalculatedCorrectly() {
        givenPlayerExists(a(player().casid()));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withDifficultyType(2).withSecondsSurvived(100)));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withDifficultyType(0).withSecondsSurvived(400)));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withDifficultyType(1).withSecondsSurvived(200)));

        whenFindingBonusTimes(a(findRequest()));

        assertEquals(1, bonusTimes.size());
        assertEquals(400, bonusTimes.get(0).getBonusTime());
    }

    @Test
    public void findBonusTimes_difficulty_bonusTimeForDifficultyIsCalculatedCorrectly_0() {
        givenPlayerExists(a(player().casid()));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withDifficultyType(0).withSecondsSurvived(100)));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withDifficultyType(1).withSecondsSurvived(400)));

        whenFindingBonusTimes(a(findRequest().withDifficultyType("0")));

        assertEquals(1, bonusTimes.size());
        assertEquals(100, bonusTimes.get(0).getBonusTime());
    }

    @Test
    public void findBonusTimes_difficulty_bonusTimeForDifficultyIsCalculatedCorrectly_1() {
        givenPlayerExists(a(player().casid()));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withDifficultyType(0).withSecondsSurvived(100)));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withDifficultyType(1).withSecondsSurvived(400)));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withDifficultyType(1).withSecondsSurvived(50)));

        whenFindingBonusTimes(a(findRequest().withDifficultyType("1")));

        assertEquals(1, bonusTimes.size());
        assertEquals(400, bonusTimes.get(0).getBonusTime());
    }

    @Test
    public void findBonusTimes_wave_bonusTimeForWaveIsCalculatedCorrectly_0() {
        givenPlayerExists(a(player().casid()));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withWaveAmountType(0).withSecondsSurvived(100)));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withWaveAmountType(1).withSecondsSurvived(400)));

        whenFindingBonusTimes(a(findRequest().withWaveAmountType("0")));

        assertEquals(1, bonusTimes.size());
        assertEquals(100, bonusTimes.get(0).getBonusTime());
    }

    @Test
    public void findBonusTimes_wave_bonusTimeForDifficultyIsCalculatedCorrectly_1() {
        givenPlayerExists(a(player().casid()));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withWaveAmountType(0).withSecondsSurvived(100)));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withWaveAmountType(1).withSecondsSurvived(400)));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withWaveAmountType(1).withSecondsSurvived(50)));

        whenFindingBonusTimes(a(findRequest().withWaveAmountType("1")));

        assertEquals(1, bonusTimes.size());
        assertEquals(400, bonusTimes.get(0).getBonusTime());
    }

    @Test
    public void findBonusTimes_waveAndDifficulty() {
        givenPlayerExists(a(player().casid()));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withWaveAmountType(1).withDifficultyType(2).withSecondsSurvived(100)));

        whenFindingBonusTimes(a(findRequest().withWaveAmountType("1").withDifficultyType("2")));

        assertEquals(1, bonusTimes.size());
        assertEquals(100, bonusTimes.get(0).getBonusTime());
    }

    @Test
    public void cheatersAreIgnored() {
        givenPlayerExists(a(player().cheater()));
        givenBonusTimeExists(a(updateRequest().withPlayerId(1).withSecondsSurvived(999999)));

        whenFindingBonusTimes(a(findRequest()));

        thenNoBonusTimesWereFound();
    }

    @Test
    public void zeroScoresAreIgnored() {
        givenPlayerExists(a(player().casid()));
        givenBonusTimeExists(a(updateRequest()
                .withPlayerId(1)
                .withDifficultyType(0)
                .withWaveAmountType(0)
                .withSecondsSurvived(10)));

        whenFindingBonusTimes(a(findRequest().withDifficultyType("1")));

        thenNoBonusTimesWereFound();
    }

    @Test
    public void mapIsConsidered() {
        givenPlayerExists(a(player().casid()));
        givenBonusTimeExists(a(updateRequest().withMapId(3)));

        whenFindingBonusTimes(a(findRequest().withMapId(2)));

        thenNoBonusTimesWereFound();
    }



    @Test
    public void versionIsConsidered() {
        // TODO depends on table name!
    }

    @Test
    public void updateBonusTime_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.updateBonusTime(a(updateRequest())));
        thenGatewayErrorIs("Could not update bonus time in database.");
    }

    private void givenBonusTimeExists(UpdateBonusTime.Request updateRequest) {
        gateway.updateBonusTime(updateRequest);
    }

    private void givenPlayerExists(Player player) {
        playerGateway.addPlayer(player);
    }

    private void givenDefaultEntriesAndPlayers() {
        givenPlayerExists(a(player().casid()
                .withName("Casual")
                .withKey("a")
        ));
        givenPlayerExists(a(player().casid()
                .withName("Pro")
                .withKey("b")
        ));
        givenPlayerExists(a(player().casid()
                .withName("Advanced")
                .withKey("c")
        ));

        givenBonusTimeExists(a(updateRequest()
                .withPlayerId(1)
                .withSecondsSurvived(12)
        ));
        givenBonusTimeExists(a(updateRequest()
                .withPlayerId(2)
                .withSecondsSurvived(9200)
        ));
        givenBonusTimeExists(a(updateRequest()
                .withPlayerId(3)
                .withSecondsSurvived(240)
        ));
    }

    private void whenFindingBonusTimes(GetBonusTimes.Request request) {
        bonusTimes = gateway.findBonusTimes(request);
    }

    private void thenNoBonusTimesWereFound() {
        assertEquals(0, bonusTimes.size());
    }

    private GetBonusTimesTest.RequestBuilder findRequest() {
        return new GetBonusTimesTest.RequestBuilder()
                .withMapId(1)
                .withWaveAmountType("*")
                .withDifficultyType("*")
                .withAppVersion("1.0.0")
                .withStart(0)
                .withLimit(500);
    }

    private UpdateBonusTimeTest.RequestBuilder updateRequest() {
        return new UpdateBonusTimeTest.RequestBuilder()
                .withPlayerId(1)
                .withMapId(1)
                .withDifficultyType(0)
                .withWaveAmountType(0)
                .withSecondsSurvived(100);
    }
}

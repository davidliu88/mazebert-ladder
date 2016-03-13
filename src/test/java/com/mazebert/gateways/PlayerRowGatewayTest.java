package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.PlayerRow;
import com.mazebert.entities.PlayerRowSimple;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.date;

public abstract class PlayerRowGatewayTest extends GatewayTest<PlayerRowGateway> {
    protected PlayerGateway playerGateway;

    private int start = 0;
    private int limit = 500;
    private Date updatedSince;

    private List<PlayerRow> playerRows;
    private List<PlayerRowSimple> playerRowsSimple;

    @Test
    public void findPlayers_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findPlayers(start, limit));
        thenGatewayErrorIs("Failed to select player rows from database");
    }

    @Test
    public void findPlayers_noPlayers() {
        whenFindPlayers();
        assertEquals(0, playerRows.size());
    }

    @Test
    public void findPlayers_propertiesAreFetched() {
        givenPlayerExists(a(player().casid()));

        whenFindPlayers();

        assertEquals(1, playerRows.size());
        PlayerRow player = playerRows.get(0);
        assertTrue(player.getId() > 0);
        assertEquals("casid", player.getName());
        assertEquals(99, player.getLevel());
        assertEquals(99999, player.getExperience());
    }

    @Test
    public void findPlayers_sortedByExperienceAndName() {
        givenDefaultPlayersExist();

        whenFindPlayers();

        assertEquals(6, playerRows.size());
        assertEquals("Blofeld", playerRows.get(0).getName());
        assertEquals("casid", playerRows.get(1).getName());
        assertEquals("Garfield", playerRows.get(2).getName());
        assertEquals("Gibby", playerRows.get(3).getName());
        assertEquals("Nap", playerRows.get(4).getName());
        assertEquals("SubZero", playerRows.get(5).getName());
    }

    @Test
    public void findPlayers_rankIsFilled() {
        givenDefaultPlayersExist();

        whenFindPlayers();

        assertEquals(1, playerRows.get(0).getRank());
        assertEquals(2, playerRows.get(1).getRank());
        assertEquals(3, playerRows.get(2).getRank());
        assertEquals(4, playerRows.get(3).getRank());
        assertEquals(5, playerRows.get(4).getRank());
        assertEquals(6, playerRows.get(5).getRank());
    }

    @Test
    public void findPlayers_startIsRespected() {
        givenDefaultPlayersExist();
        start = 4;

        whenFindPlayers();

        assertEquals(2, playerRows.size());
        assertEquals(5, playerRows.get(0).getRank());
        assertEquals(6, playerRows.get(1).getRank());
    }

    @Test
    public void findPlayers_limitIsRespected() {
        givenDefaultPlayersExist();
        limit = 3;

        whenFindPlayers();

        assertEquals(3, playerRows.size());
    }

    @Test
    public void findPlayersUpdatedSince_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findPlayersUpdatedSince(a(date())));
        thenGatewayErrorIs("Failed to select players updated since from database");
    }

    @Test
    public void findPlayersUpdatedSince_noPlayers() {
        whenFindPlayersUpdatedSince();
        assertEquals(0, playerRowsSimple.size());
    }

    @Test
    public void findPlayersUpdatedSince_matchingPlayersSortedByName() {
        givenPlayerExists(a(player().withKey("a").withName("Homer").withLastUpdate(a(date().with("2016-02-06 17:00:00")))));
        givenPlayerExists(a(player().withKey("b").withName("Marge").withLastUpdate(a(date().with("2016-02-06 16:50:00")))));
        givenPlayerExists(a(player().withKey("c").withName("Maggy").withLastUpdate(a(date().with("2016-02-06 16:50:00")))));
        givenPlayerExists(a(player().withKey("d").withName("casid").withLastUpdate(a(date().with("2016-02-06 16:50:00")))));
        givenPlayerExists(a(player().withKey("e").withName("Bart" ).withLastUpdate(a(date().with("2016-02-06 16:49:59")))));
        givenPlayerExists(a(player().withKey("f").withName("Lisa" ).withLastUpdate(a(date().with("2015-01-01 00:00:00")))));

        updatedSince = a(date().with("2016-02-06 16:50:00"));

        whenFindPlayersUpdatedSince();

        assertEquals(4, playerRowsSimple.size());
        assertEquals("casid", playerRowsSimple.get(0).getName());
        assertEquals("Homer", playerRowsSimple.get(1).getName());
        assertEquals("Maggy", playerRowsSimple.get(2).getName());
        assertEquals("Marge", playerRowsSimple.get(3).getName());
    }

    @Test
    public void playerCount_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.getTotalPlayerCount());
        thenGatewayErrorIs("Failed to select amount of players from database");
    }

    @Test
    public void playerCount_noPlayers() {
        assertEquals(0, gateway.getTotalPlayerCount());
    }

    @Test
    public void playerCount_onePlayer() {
        givenPlayerExists(a(player().casid()));
        assertEquals(1, gateway.getTotalPlayerCount());
    }

    private void givenPlayerExists(Player player) {
        playerGateway.addPlayer(player);
    }

    private void givenDefaultPlayersExist() {
        givenPlayerExists(a(player().withKey("a").withName("Garfield").withExperience(100)));
        givenPlayerExists(a(player().withKey("b").withName("Nap").withExperience(10)));
        givenPlayerExists(a(player().withKey("c").withName("Gibby").withExperience(20)));
        givenPlayerExists(a(player().withKey("d").withName("SubZero").withExperience(5)));
        givenPlayerExists(a(player().withKey("e").withName("casid").withExperience(100)));
        givenPlayerExists(a(player().withKey("f").withName("Blofeld").withExperience(100)));
        givenPlayerExists(a(player().cheater())); // Cheater must be ignored!
    }

    private void whenFindPlayers() {
        playerRows = gateway.findPlayers(start, limit);
    }

    private void whenFindPlayersUpdatedSince() {
        playerRowsSimple = gateway.findPlayersUpdatedSince(updatedSince);
    }
}
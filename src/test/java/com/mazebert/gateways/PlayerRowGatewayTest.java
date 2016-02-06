package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.PlayerRow;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.date;

public abstract class PlayerRowGatewayTest extends GatewayTest<PlayerRowGateway> {
    protected PlayerGateway playerGateway;

    private int start = 0;
    private int limit = 500;
    private Date updatedSince;

    private List<PlayerRow> playerRows;

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
    public void findPlayersNowPlaying_noPlayers() {
        whenFindPlayersNowPlaying();
        assertEquals(0, playerRows.size());
    }

    @Test
    public void findPlayersNowPlaying_matchingPlayersSortedByName() {
        givenPlayerExists(a(player().casid().withName("Homer").withLastUpdate(a(date().with("2016-02-06 17:00:00")))));
        givenPlayerExists(a(player().casid().withName("Marge").withLastUpdate(a(date().with("2016-02-06 16:50:00")))));
        givenPlayerExists(a(player().casid().withName("Maggy").withLastUpdate(a(date().with("2016-02-06 16:50:00")))));
        givenPlayerExists(a(player().casid().withName("casid").withLastUpdate(a(date().with("2016-02-06 16:50:00")))));
        givenPlayerExists(a(player().casid().withName("Bart" ).withLastUpdate(a(date().with("2016-02-06 16:49:59")))));
        givenPlayerExists(a(player().casid().withName("Lisa" ).withLastUpdate(a(date().with("2015-01-01 00:00:00")))));

        updatedSince = a(date().with("2016-02-06 16:50:00"));

        whenFindPlayersNowPlaying();

        assertEquals(4, playerRows.size());
        assertEquals("casid", playerRows.get(0).getName());
        assertEquals("Homer", playerRows.get(1).getName());
        assertEquals("Maggy", playerRows.get(2).getName());
        assertEquals("Marge", playerRows.get(3).getName());
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
        givenPlayerExists(a(player().casid().withName("Garfield").withExperience(100)));
        givenPlayerExists(a(player().casid().withName("Nap").withExperience(10)));
        givenPlayerExists(a(player().casid().withName("Gibby").withExperience(20)));
        givenPlayerExists(a(player().casid().withName("SubZero").withExperience(5)));
        givenPlayerExists(a(player().casid().withName("casid").withExperience(100)));
        givenPlayerExists(a(player().casid().withName("Blofeld").withExperience(100)));
    }

    private void whenFindPlayers() {
        playerRows = gateway.findPlayers(start, limit);
    }

    private void whenFindPlayersNowPlaying() {
        playerRows = gateway.findPlayersUpdatedSince(updatedSince);
    }
}
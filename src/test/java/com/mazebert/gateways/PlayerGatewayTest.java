package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.gateways.error.KeyAlreadyExists;
import org.junit.Test;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.*;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.date;

public abstract class PlayerGatewayTest extends GatewayTest<PlayerGateway> {

    @Test
    public void addPlayer_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.addPlayer(a(player())));
        thenGatewayErrorIs("Failed to insert player into database.");
    }

    @Test
    public void addPlayer_idIsReturned() {
        Player player = gateway.addPlayer(a(player()
                .withName("Ontrose")
                .withKey("abcdef")
                .withLevel(99)
                .withExperience(1000)));


        assertTrue(player.getId() > 0);
    }

    @Test
    public void addPlayer_playerIsPersisted() {
        Player expected = a(player()
                .withName("Ontrose")
                .withKey("abcdef")
                .withLevel(99)
                .withExperience(1000));

        gateway.addPlayer(expected);

        Player actual = gateway.findPlayerByKey("abcdef");
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getExperience(), actual.getExperience());
        assertEquals(expected.getLevel(), actual.getLevel());
    }

    @Test
    public void addPlayer_playerKeyAlreadyExists() {
        gateway.addPlayer(a(player().casid()));

        try {
            gateway.addPlayer(a(player().casid()));
        } catch (KeyAlreadyExists error) {
            this.error = error;
        }

        assertNotNull(error);
    }

    @Test
    public void findPlayerByKey_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findPlayerByKey("?"));
        thenGatewayErrorIs("Failed to find player by key in database");
    }

    @Test
    public void findPlayerByKey_valuesAreReturned() {
        Player expected = gateway.addPlayer(a(player().casid()));
        Player actual = gateway.findPlayerByKey("abcdef");
        thenPlayerIsEqualTo(expected, actual);
    }

    @Test
    public void findPlayerByEmail_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findPlayerByEmail("?"));
        thenGatewayErrorIs("Failed to find player by email in database");
    }

    @Test
    public void findPlayerByEmail_notFound() {
        gateway.addPlayer(a(player().casid().withEmail("player@mazebert.com")));
        Player actual = gateway.findPlayerByEmail("someone.else@mazebert.com");
        assertNull(actual);
    }

    @Test
    public void findPlayerByEmail_valuesAreReturned() {
        Player expected = gateway.addPlayer(a(player().casid().withEmail("player@mazebert.com")));
        Player actual = gateway.findPlayerByEmail("player@mazebert.com");
        thenPlayerIsEqualTo(expected, actual);
    }

    @Test
    public void findPlayerByEmail_emailCheckIsCaseInsensitive() {
        Player expected = gateway.addPlayer(a(player().casid().withEmail("player@MAZEBERT.COM")));
        Player actual = gateway.findPlayerByEmail("PLAYER@mazebert.com");
        thenPlayerIsEqualTo(expected, actual);
    }

    @Test
    public void findPlayerRank_gatewayError() {
        assertEquals(0 , errorGateway.findPlayerRank(16));
    }

    @Test
    public void findPlayerRank_playerDoesNotExist() {
        assertEquals(0 , gateway.findPlayerRank(1234));
    }

    @Test
    public void findPlayerRank_sortedByExperience() {
        Player p1 = gateway.addPlayer(a(player().casid().withKey("a").withExperience(100)));
        Player p2 = gateway.addPlayer(a(player().casid().withKey("b").withExperience(1213)));
        Player p3 = gateway.addPlayer(a(player().casid().withKey("c").withExperience(12)));
        Player p4 = gateway.addPlayer(a(player().casid().withKey("d").withExperience(223)));

        assertEquals(1, gateway.findPlayerRank(p2.getId()));
        assertEquals(2, gateway.findPlayerRank(p4.getId()));
        assertEquals(3, gateway.findPlayerRank(p1.getId()));
        assertEquals(4, gateway.findPlayerRank(p3.getId()));
    }

    @Test
    public void findPlayerRank_equalExperience_sortedByName() {
        Player p1 = gateway.addPlayer(a(player().casid().withName("Homer").withKey("a")));
        Player p2 = gateway.addPlayer(a(player().casid().withName("Marge").withKey("b")));
        Player p3 = gateway.addPlayer(a(player().casid().withName("Bart").withKey("c")));
        Player p4 = gateway.addPlayer(a(player().casid().withName("Lisa").withKey("d")));
        Player p5 = gateway.addPlayer(a(player().casid().withName("casid").withKey("e")));

        assertEquals(1, gateway.findPlayerRank(p3.getId()));
        assertEquals(2, gateway.findPlayerRank(p5.getId()));
        assertEquals(3, gateway.findPlayerRank(p1.getId()));
        assertEquals(4, gateway.findPlayerRank(p4.getId()));
        assertEquals(5, gateway.findPlayerRank(p2.getId()));
    }

    @Test
    public void findPlayerRank_cheater() {
        Player player = gateway.addPlayer(a(player().cheater()));
        assertEquals(0, gateway.findPlayerRank(player.getId()));
    }

    @Test
    public void updatePlayer_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.updatePlayer(a(player())));
        thenGatewayErrorIs("Failed to update player in database");
    }

    @Test
    public void updatePlayer_valuesAreUpdated() {
        Player existing = gateway.addPlayer(a(player().casid()));

        gateway.updatePlayer(a(player().casid()
                .withId(existing.getId())
                .withLevel(200)
                .withExperience(100000000)
                .withLastUpdate(a(date().with("2016-02-02 23:00:11")))
                .withEmail("anonymous@mazebert.com")
        ));

        Player updated = gateway.findPlayerByKey(existing.getKey());
        assertEquals(200, updated.getLevel());
        assertEquals(100000000, updated.getExperience());
        assertEquals(a(date().with("2016-02-02 23:00:11")), updated.getLastUpdate());
        assertEquals("anonymous@mazebert.com", updated.getEmail());
    }

    private void thenPlayerIsEqualTo(Player expected, Player actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLevel(), actual.getLevel());
        assertEquals(expected.getExperience(), actual.getExperience());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getSupporterLevel(), actual.getSupporterLevel());
        assertEquals(expected.getRelics(), actual.getRelics());
        assertEquals(expected.getLastUpdate(), actual.getLastUpdate());
        assertEquals(expected.getLastQuestCreation(), actual.getLastQuestCreation());
    }
}

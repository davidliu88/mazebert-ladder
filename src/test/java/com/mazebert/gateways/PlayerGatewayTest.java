package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.gateways.error.KeyAlreadyExists;
import org.junit.Test;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.jusecase.builders.BuilderFactory.a;

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

        Player actual = gateway.findPlayer("abcdef");
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
    public void findPlayer_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findPlayer("?"));
        thenGatewayErrorIs("Failed to find player by key in database");
    }
}

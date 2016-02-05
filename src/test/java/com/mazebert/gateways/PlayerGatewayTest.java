package com.mazebert.gateways;

import com.mazebert.entities.Player;
import org.junit.Test;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.jusecase.builders.BuilderFactory.a;

public abstract class PlayerGatewayTest extends GatewayTest<PlayerGateway> {

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
}

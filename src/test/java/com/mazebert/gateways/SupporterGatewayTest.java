package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.Supporter;
import org.junit.Test;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;

public abstract class SupporterGatewayTest extends GatewayTest<SupporterGateway> {
    protected PlayerGateway playerGateway;

    @Test
    public void findAllSupporters_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findAllSupporters());
        thenGatewayErrorIs("Failed to find all supporters.");
    }

    @Test
    public void findAllSupporters_noPlayers() {
        assertEquals(0, gateway.findAllSupporters().size());
    }

    @Test
    public void findAllSupporters_zeroSupporterLevel() {
        givenPlayerExists(a(player().casid().withSupporterLevel(0)));
        assertEquals(0, gateway.findAllSupporters().size());
    }

    @Test
    public void findAllSupporters_propertiesAreMapped() {
        givenPlayerExists(a(player().casid()
                .withName("Name")
                .withSupporterLevel(7)));

        List<Supporter> supporters = gateway.findAllSupporters();

        assertEquals(1, supporters.size());
        assertEquals(1, supporters.get(0).getId());
        assertEquals("Name", supporters.get(0).getName());
        assertEquals(7, supporters.get(0).getSupporterLevel());
    }

    @Test
    public void findAllSupporters_sortedByLevel() {
        givenPlayerExists(a(player().withKey("1").withSupporterLevel(4).withName("Player 1")));
        givenPlayerExists(a(player().withKey("2").withSupporterLevel(1).withName("Player 2")));
        givenPlayerExists(a(player().withKey("3").withSupporterLevel(7).withName("Player 3")));

        List<Supporter> supporters = gateway.findAllSupporters();

        assertEquals(3, supporters.size());
        assertEquals("Player 3", supporters.get(0).getName());
        assertEquals("Player 1", supporters.get(1).getName());
        assertEquals("Player 2", supporters.get(2).getName());
    }

    @Test
    public void findAllSupporters_sameLevel_sortedByNameLowercase() {
        givenPlayerExists(a(player().withKey("1").withSupporterLevel(7).withName("maggie")));
        givenPlayerExists(a(player().withKey("2").withSupporterLevel(7).withName("Marge")));
        givenPlayerExists(a(player().withKey("3").withSupporterLevel(7).withName("Homer")));

        List<Supporter> supporters = gateway.findAllSupporters();

        assertEquals(3, supporters.size());
        assertEquals("Homer", supporters.get(0).getName());
        assertEquals("maggie", supporters.get(1).getName());
        assertEquals("Marge", supporters.get(2).getName());
    }

    private void givenPlayerExists(Player player) {
        playerGateway.addPlayer(player);
    }
}

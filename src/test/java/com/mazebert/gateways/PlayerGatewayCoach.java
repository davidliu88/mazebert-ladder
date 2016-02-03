package com.mazebert.gateways;

import com.mazebert.entities.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerGatewayCoach implements PlayerGateway {
    private int nextPlayerId;
    private Player addedPlayer;
    private List<RuntimeException> exceptions = new ArrayList<RuntimeException>();

    public Player addPlayer(Player player) {
        if (exceptions.size() > 0) {
            throw exceptions.remove(0);
        }

        player.setId(nextPlayerId);
        addedPlayer = player;
        return player;
    }

    public void givenNextPlayerId(int id) {
        nextPlayerId = id;
    }

    public Player getAddedPlayer() {
        return addedPlayer;
    }

    public void givenOperationFailsWithException(RuntimeException ... exceptions) {
        this.exceptions.addAll(Arrays.asList(exceptions));
    }
}

package com.mazebert.gateways;

import com.mazebert.entities.Player;

public class PlayerGatewayCoach implements PlayerGateway {
    private int nextPlayerId;
    private Player addedPlayer;

    public Player addPlayer(Player player) {
        player.id = nextPlayerId;
        addedPlayer = player;
        return player;
    }

    public void givenNextPlayerId(int id) {
        nextPlayerId = id;
    }

    public Player getAddedPlayer() {
        return addedPlayer;
    }
}

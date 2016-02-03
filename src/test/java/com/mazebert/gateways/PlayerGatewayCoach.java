package com.mazebert.gateways;

import com.mazebert.entities.Player;

import java.util.*;

public class PlayerGatewayCoach implements PlayerGateway {
    private int nextPlayerId;
    private Player addedPlayer;
    private List<RuntimeException> exceptions = new ArrayList<RuntimeException>();

    private Map<String, List<Player>> players = new HashMap<String, List<Player>>();

    public Player addPlayer(Player player) {
        if (exceptions.size() > 0) {
            throw exceptions.remove(0);
        }

        player.setId(nextPlayerId);
        addedPlayer = player;
        return player;
    }

    public List<Player> findPlayers(int start, int limit) {
        return players.get(start + "" + limit);
    }

    public void givenNextPlayerId(int id) {
        nextPlayerId = id;
    }

    public void givenPlayers(int start, int limit, List<Player> players) {
        this.players.put(start + "" + limit, players);
    }

    public Player getAddedPlayer() {
        return addedPlayer;
    }

    public void givenOperationFailsWithException(RuntimeException ... exceptions) {
        this.exceptions.addAll(Arrays.asList(exceptions));
    }
}

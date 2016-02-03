package com.mazebert.gateways;

import com.mazebert.entities.Player;

import java.util.*;

public class PlayerGatewayCoach implements PlayerGateway {
    private int nextPlayerId;
    private Player addedPlayer;
    private Player updatedPlayer;
    private List<RuntimeException> exceptions = new ArrayList<RuntimeException>();

    private Map<String, List<Player>> playersInRange = new HashMap<String, List<Player>>();
    private Map<String, Player> playerByKey = new HashMap<String, Player>();

    public Player addPlayer(Player player) {
        if (exceptions.size() > 0) {
            throw exceptions.remove(0);
        }

        player.setId(nextPlayerId);
        addedPlayer = player;
        return player;
    }

    public List<Player> findPlayers(int start, int limit) {
        return playersInRange.get(start + "" + limit);
    }

    public Player findPlayer(String key) {
        return playerByKey.get(key);
    }

    public void updatePlayer(Player player) {
        updatedPlayer = player;
    }

    public void givenNextPlayerId(int id) {
        nextPlayerId = id;
    }

    public void givenPlayers(int start, int limit, List<Player> players) {
        this.playersInRange.put(start + "" + limit, players);
    }

    public Player getAddedPlayer() {
        return addedPlayer;
    }

    public Player getUpdatedPlayer() {
        return updatedPlayer;
    }

    public void givenOperationFailsWithException(RuntimeException ... exceptions) {
        this.exceptions.addAll(Arrays.asList(exceptions));
    }

    public void givenPlayer(Player player) {
        playerByKey.put(player.getKey(), player);
    }
}

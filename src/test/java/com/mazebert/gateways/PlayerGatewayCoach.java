package com.mazebert.gateways;

import com.mazebert.entities.Player;

import java.util.*;

public class PlayerGatewayCoach implements PlayerGateway {
    private int nextPlayerId;
    private Player addedPlayer;
    private Player updatedPlayer;
    private List<RuntimeException> exceptions = new ArrayList<>();

    private Map<String, Player> playerByKey = new HashMap<>();
    private Map<Long, Integer> playerRankById = new HashMap<>();

    public Player addPlayer(Player player) {
        if (exceptions.size() > 0) {
            throw exceptions.remove(0);
        }

        player.setId(nextPlayerId);
        addedPlayer = player;
        return player;
    }

    public Player findPlayer(String key) {
        return playerByKey.get(key);
    }

    @Override
    public int findPlayerRank(long id) {
        Integer result = playerRankById.get(id);
        return result == null ? 0 : result;
    }

    public void updatePlayer(Player player) {
        updatedPlayer = player;
    }

    public void givenNextPlayerId(int id) {
        nextPlayerId = id;
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

    public void givenPlayerRank(int rank, Player player) {
        playerRankById.put(player.getId(), rank);
    }
}

package com.mazebert.gateways.mocks;

import com.mazebert.entities.Player;
import com.mazebert.gateways.PlayerGateway;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class PlayerGatewayMock implements PlayerGateway {
    private int nextPlayerId;
    private Player addedPlayer;
    private Player updatedPlayer;
    private List<RuntimeException> exceptions = new ArrayList<>();

    private Map<Long, Player> playerById = new HashMap<>();
    private Map<String, Player> playerByKey = new HashMap<>();
    private Map<String, Player> playerByEmail = new HashMap<>();
    private Map<Long, Integer> playerRankById = new HashMap<>();

    public Player addPlayer(Player player) {
        if (exceptions.size() > 0) {
            throw exceptions.remove(0);
        }

        player.setId(nextPlayerId);
        addedPlayer = player;
        return player;
    }

    @Override
    public Player findPlayerById(long id) {
        return playerById.get(id);
    }

    public Player findPlayerByKey(String key) {
        return playerByKey.get(key);
    }

    @Override
    public Player findPlayerByEmail(String email) {
        return playerByEmail.get(email);
    }

    @Override
    public int findPlayerRank(long id) {
        Integer result = playerRankById.get(id);
        return result == null ? 0 : result;
    }

    public void updatePlayer(Player player) {
        updatedPlayer = player;
    }

    @Override
    public int getRelics(long playerId) {
        Player player = playerById.get(playerId);
        return player == null ? 0 : player.getRelics();
    }

    @Override
    public void addRelics(long playerId, int relics) {
        Player player = playerById.get(playerId);
        player.setRelics(player.getRelics() + relics);
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

    public void givenPlayerExists(Player player) {
        playerById.put(player.getId(), player);
        playerByKey.put(player.getKey(), player);
        playerByEmail.put(player.getEmail(), player);
    }

    public void givenNoPlayerExists() {
        playerById.clear();
        playerByKey.clear();
        playerByEmail.clear();
    }

    public void givenPlayerRank(int rank, Player player) {
        playerRankById.put(player.getId(), rank);
    }

    public void thenSupporterLevelIs(Player player, int supporterLevel) {
        assertEquals(supporterLevel, playerById.get(player.getId()).getSupporterLevel());
    }
}

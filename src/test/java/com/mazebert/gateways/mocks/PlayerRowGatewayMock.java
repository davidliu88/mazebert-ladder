package com.mazebert.gateways.mocks;

import com.mazebert.entities.PlayerRow;
import com.mazebert.entities.PlayerRowSimple;
import com.mazebert.gateways.PlayerRowGateway;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRowGatewayMock implements PlayerRowGateway {
    private Map<String, List<PlayerRow>> playersInRange = new HashMap<>();
    private Map<String, List<PlayerRowSimple>> playersNowPlaying = new HashMap<>();
    private int totalPlayerCount;

    @Override
    public List<PlayerRow> findPlayers(int start, int limit) {
        return playersInRange.get(start + "" + limit);
    }

    @Override
    public List<PlayerRowSimple> findPlayersUpdatedSince(Date date) {
        return playersNowPlaying.get("" + date);
    }

    @Override
    public int getTotalPlayerCount() {
        return totalPlayerCount;
    }

    public void givenPlayers(int start, int limit, List<PlayerRow> players) {
        this.playersInRange.put(start + "" + limit, players);
    }

    public void givenTotalPlayerCount(int count) {
        totalPlayerCount = count;
    }

    public void givenPlayerUpdatedSince(Date date, List<PlayerRowSimple> players) {
        playersNowPlaying.put("" + date,  players);
    }
}

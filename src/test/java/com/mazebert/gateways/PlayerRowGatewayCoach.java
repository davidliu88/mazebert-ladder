package com.mazebert.gateways;

import com.mazebert.entities.PlayerRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRowGatewayCoach implements PlayerRowGateway {
    private Map<String, List<PlayerRow>> playersInRange = new HashMap<>();
    private Map<String, List<PlayerRow>> playersNowPlaying = new HashMap<>();
    private int totalPlayerCount;

    @Override
    public List<PlayerRow> findPlayers(int start, int limit) {
        return playersInRange.get(start + "" + limit);
    }

    @Override
    public List<PlayerRow> findPlayersNowPlaying(int toleranceInMinutes) {
        return playersNowPlaying.get("" + toleranceInMinutes);
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

    public void givenPlayersNowPlaying(int toleranceInMinutes, List<PlayerRow> players) {
        playersNowPlaying.put("" + toleranceInMinutes,  players);
    }
}

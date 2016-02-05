package com.mazebert.gateways;

import com.mazebert.entities.PlayerRow;

import java.util.List;

public interface PlayerRowGateway {
    List<PlayerRow> findPlayers(int start, int limit);
    List<PlayerRow> findPlayersNowPlaying(int toleranceInMinutes);
    int getTotalPlayerCount();
}

package com.mazebert.gateways.mysql;

import com.mazebert.entities.PlayerRow;
import com.mazebert.gateways.PlayerRowGateway;

import java.util.List;

public class MySqlPlayerRowGateway implements PlayerRowGateway {
    @Override
    public List<PlayerRow> findPlayers(int start, int limit) {
        return null;
    }

    @Override
    public List<PlayerRow> findPlayersNowPlaying(int toleranceInMinutes) {
        return null;
    }

    @Override
    public int getTotalPlayerCount() {
        return 0;
    }
}

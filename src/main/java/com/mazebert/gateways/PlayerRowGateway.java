package com.mazebert.gateways;

import com.mazebert.entities.PlayerRow;
import com.mazebert.entities.PlayerRowSimple;

import java.util.Date;
import java.util.List;

public interface PlayerRowGateway {
    List<PlayerRow> findPlayers(int start, int limit);
    List<PlayerRowSimple> findPlayersUpdatedSince(Date updatedSince);
    int getTotalPlayerCount();
}

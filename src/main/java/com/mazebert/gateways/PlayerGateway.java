package com.mazebert.gateways;

import com.mazebert.entities.Player;

import java.util.List;

public interface PlayerGateway {
    Player addPlayer(Player player);

    List<Player> findPlayers(int start, int limit);
}

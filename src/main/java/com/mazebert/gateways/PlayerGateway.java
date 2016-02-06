package com.mazebert.gateways;

import com.mazebert.entities.Player;

public interface PlayerGateway {
    Player addPlayer(Player player);

    Player findPlayer(String key);

    int findPlayerRank(long id);

    void updatePlayer(Player player);
}

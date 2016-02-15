package com.mazebert.gateways;

import com.mazebert.entities.Player;

public interface PlayerGateway {
    Player addPlayer(Player player);

    Player findPlayerByKey(String key);
    Player findPlayerByEmail(String email);

    int findPlayerRank(long id);

    void updatePlayer(Player player);
}

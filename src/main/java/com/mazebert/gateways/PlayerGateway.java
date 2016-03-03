package com.mazebert.gateways;

import com.mazebert.entities.Player;

public interface PlayerGateway {
    Player addPlayer(Player player);

    Player findPlayerById(long id);
    Player findPlayerByKey(String key);
    Player findPlayerByEmail(String email);

    int findPlayerRank(long id);

    void updatePlayer(Player player);

    int getRelics(long playerId);
    void addRelics(long playerId, int relics);
}

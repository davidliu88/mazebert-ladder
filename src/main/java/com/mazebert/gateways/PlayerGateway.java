package com.mazebert.gateways;

import com.mazebert.entities.Player;

import java.util.List;

public interface PlayerGateway {
    Player addPlayer(Player player);

    Player findPlayer(String key);

    void updatePlayer(Player player);
}

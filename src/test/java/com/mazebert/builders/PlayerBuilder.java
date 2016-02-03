package com.mazebert.builders;

import com.mazebert.entities.Player;
import org.jusecase.builders.Builder;

public class PlayerBuilder implements Builder<Player> {
    private Player player = new Player();

    public Player build() {
        return player;
    }
}

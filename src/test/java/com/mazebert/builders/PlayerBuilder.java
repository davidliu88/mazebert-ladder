package com.mazebert.builders;

import com.mazebert.entities.Player;
import org.jusecase.builders.Builder;

import java.util.Date;

public class PlayerBuilder implements Builder<Player> {
    private Player player = new Player();

    public PlayerBuilder casid() {
        return this
                .withId(115)
                .withKey("abcdef")
                .withName("casid")
                .withLevel(99)
                .withExperience(99999);
    }

    public PlayerBuilder withId(long value) {
        player.setId(value);
        return this;
    }

    public PlayerBuilder withKey(String value) {
        player.setKey(value);
        return this;
    }

    public PlayerBuilder withName(String value) {
        player.setName(value);
        return this;
    }

    public PlayerBuilder withLevel(int value) {
        player.setLevel(value);
        return this;
    }

    public PlayerBuilder withExperience(long value) {
        player.setExperience(value);
        return this;
    }

    public PlayerBuilder withLastUpdate(Date value) {
        player.setLastUpdate(value);
        return this;
    }

    @Override
    public Player build() {
        return player;
    }
}

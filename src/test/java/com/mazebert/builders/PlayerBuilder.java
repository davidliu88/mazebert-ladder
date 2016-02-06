package com.mazebert.builders;

import com.mazebert.entities.Player;
import org.jusecase.builders.*;

import java.util.Date;

import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.date;

public class PlayerBuilder implements Builder<Player> {
    private Player player = new Player();

    public PlayerBuilder casid() {
        return this
                .withId(115)
                .withKey("abcdef")
                .withName("casid")
                .withLevel(99)
                .withExperience(99999)
                .withEmail("andy@mazebert.com")
                .withSupporterLevel(7)
                .withRelics(300)
                .withLastUpdate(a(date().with("2016-02-10 22:22:22")));
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

    public PlayerBuilder withEmail(String value) {
        player.setEmail(value);
        return this;
    }

    public PlayerBuilder withSupporterLevel(int value) {
        player.setSupporterLevel(value);
        return this;
    }

    public PlayerBuilder withRelics(int value) {
        player.setRelics(value);
        return this;
    }

    @Override
    public Player build() {
        return player;
    }
}

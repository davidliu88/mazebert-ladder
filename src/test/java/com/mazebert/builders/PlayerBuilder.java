package com.mazebert.builders;

import com.mazebert.entities.Player;
import org.jusecase.builders.Builder;

import java.util.Date;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.date;

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
                .withLastUpdate(a(date().with("2016-02-10 22:22:22")))
                .withLastQuestCreation(a(date().with("2016-02-05 17:00:00")));
    }

    public PlayerBuilder cheater() {
        return this
                .withId(11111)
                .withKey("cheatr")
                .withName("cheat0r")
                .withLevel(999)
                .withExperience(99999999)
                .withEmail("cheater@gmail.com")
                .withSupporterLevel(0)
                .withRelics(30000)
                .withLastUpdate(a(date().with("2016-07-13 11:11:11")))
                .withLastQuestCreation(a(date().with("2016-02-05 17:00:00")))
                .withIsCheater(true);
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

    private PlayerBuilder withIsCheater(boolean value) {
        player.setIsCheater(value);
        return this;
    }

    public PlayerBuilder withLastQuestCreation(Date value) {
        player.setLastQuestCreation(value);
        return this;
    }

    @Override
    public Player build() {
        return player;
    }
}

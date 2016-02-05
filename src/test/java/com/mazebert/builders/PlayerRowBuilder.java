package com.mazebert.builders;

import com.mazebert.entities.PlayerRow;
import org.jusecase.builders.Builder;

public class PlayerRowBuilder implements Builder<PlayerRow> {

    private PlayerRow playerRow = new PlayerRow();

    public PlayerRowBuilder withId(int value) {
        playerRow.setId(value);
        return this;
    }

    public PlayerRowBuilder withName(String value) {
        playerRow.setName(value);
        return this;
    }

    public PlayerRowBuilder withLevel(int value) {
        playerRow.setLevel(value);
        return this;
    }

    public PlayerRowBuilder withExperience(long value) {
        playerRow.setExperience(value);
        return this;
    }

    @Override
    public PlayerRow build() {
        return playerRow;
    }
}

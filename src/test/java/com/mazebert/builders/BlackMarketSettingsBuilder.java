package com.mazebert.builders;

import com.mazebert.entities.BlackMarketSettings;
import org.jusecase.builders.Builder;

public class BlackMarketSettingsBuilder implements Builder<BlackMarketSettings> {
    private BlackMarketSettings settings = new BlackMarketSettings();

    public BlackMarketSettingsBuilder withPrice(int price) {
        settings.setPrice(price);
        return this;
    }

    @Override
    public BlackMarketSettings build() {
        return settings;
    }
}

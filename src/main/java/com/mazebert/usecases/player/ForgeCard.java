package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;

public class ForgeCard extends AbstractBuyCard<ForgeCard.Request, AbstractBuyCard.Response> {

    public ForgeCard(PlayerGateway playerGateway, FoilCardGateway foilCardGateway) {
        super(playerGateway, foilCardGateway);
    }

    @Override
    protected AbstractBuyCard.Response doTransaction(Player player, Request request) {
        return null;
    }

    @Override
    protected String getRequiredVersion() {
        return "1.0.0";
    }

    public static class Request extends AbstractBuyCard.Request {
    }
}

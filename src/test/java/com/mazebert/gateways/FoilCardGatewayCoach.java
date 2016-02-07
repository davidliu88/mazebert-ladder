package com.mazebert.gateways;

import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoilCardGatewayCoach implements FoilCardGateway {
    private Map<Long, List<FoilCard>> foilCardsForPlayer = new HashMap<>();

    @Override
    public List<FoilCard> getFoilCardsForPlayerId(long id) {
        return foilCardsForPlayer.get(id);
    }

    public void givenFoilCardsForPlayer(Player player, List<FoilCard> foilCards) {
        foilCardsForPlayer.put(player.getId(), foilCards);
    }
}

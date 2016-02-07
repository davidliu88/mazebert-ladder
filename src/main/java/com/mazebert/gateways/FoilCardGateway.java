package com.mazebert.gateways;

import com.mazebert.entities.FoilCard;

import java.util.List;

public interface FoilCardGateway {
    List<FoilCard> getFoilCardsForPlayerId(long id);
}
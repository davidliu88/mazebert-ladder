package com.mazebert.gateways;

import com.mazebert.entities.Card;

public interface CardGateway {
    Card findCard(long cardId, int cardType);
}

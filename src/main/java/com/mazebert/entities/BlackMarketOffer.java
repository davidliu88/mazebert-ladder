package com.mazebert.entities;

public class BlackMarketOffer {
    private long id;
    private long cardId;
    private int cardType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getCardType() {
        return cardType;
    }
}

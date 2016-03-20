package com.mazebert.entities;

public class FoilCard {
    private long cardId;
    private int cardType;
    private int amount;

    public FoilCard() {
    }

    public FoilCard(long cardId, int cardType) {
        this(cardId, cardType, 1);
    }

    public FoilCard(long cardId, int cardType, int amount) {
        this.cardId = cardId;
        this.cardType = cardType;
        this.amount = amount;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

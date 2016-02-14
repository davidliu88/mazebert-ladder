package com.mazebert.entities;

import java.util.Date;

public class BlackMarketOffer {
    private long id;
    private long cardId;
    private int cardType;
    private Date expirationDate;

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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}

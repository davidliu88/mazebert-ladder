package com.mazebert.entities;

public class Purchase {

    private String productId;
    private long playerId;
    private String store;
    private String data;
    private String signature;
    private String appVersion;

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getStore() {
        return store;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}

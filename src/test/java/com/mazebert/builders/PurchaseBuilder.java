package com.mazebert.builders;

import com.mazebert.entities.Purchase;
import org.jusecase.builders.Builder;

public class PurchaseBuilder implements Builder<Purchase> {
    private Purchase purchase = new Purchase();

    @Override
    public Purchase build() {
        return purchase;
    }

    public PurchaseBuilder googlePlay() {
        return this
            .withStore("GooglePlay")
            .withData("{\"orderId\":\"10999763161054705758.1333116792295335\",\"packageName\":\"air.com.mazebert.MazebertTD\",\"productId\":\"com.mazebert.whisky\",\"purchaseTime\":1418993253103,\"purchaseState\":0,\"developerPayload\":\"116-com.mazebert.whisky\",\"purchaseToken\":\"eloeambngfciphlilagdbhjm.A1-J1OwyJegaUKXM3BKJwTjxESevdX7ulp0GC_PcvjAh44OPAQa627Whhj0IS-_Tc5I1yU5KNgKLToPne5qUKXjrfCeKQgJsYfgQ613e5lA6PgeESy07Dy27oOEOWb_UF-t2-OnVEhblOaJq_cxmbPcRQUTITWlozw\"}")
            .withSignature("AvtXYyeLSHkqVbe5A7SZg6HdfIW9bc1P1FnhMgH8bWkoS6agXUw6EDm2ecFNTs0zoUJnmbp7qfHMLDIfcrM3xxJqbo8yYGdcxMcuowzfQkWiNEoBVCg/7JaBR9Wok01aB/gCxHdB33JOX8o/aQY1hhAHX/jcsLjFc5MbI1n9f/wLMOYK6zzScnH3EmqjDR+NZs53asXikozfYBy/2YLQn8dx+WnsMm/GeC3MTKPNeBoDn1Cj95FSUjEr5WcrEdrSUNGEi+B3qOHxelWyAl6g9Fb8xRGuYT7am77cRzTdES9rsc/yREwhonTe8Wfm7XKBA6dDmXEaWPGUoRGk9essOA==");
    }

    public PurchaseBuilder googlePlayCookie() {
        return this
                .googlePlay()
                .withProductId("com.mazebert.cookie");
    }

    public PurchaseBuilder googlePlayBeer() {
        return this
                .googlePlay()
                .withProductId("com.mazebert.beer");
    }

    public PurchaseBuilder googlePlayWhisky() {
        return this
                .googlePlay()
                .withProductId("com.mazebert.whisky");
    }

    public PurchaseBuilder withProductId(String value) {
        purchase.setProductId(value);
        return this;
    }

    public PurchaseBuilder withPlayerId(long value) {
        purchase.setPlayerId(value);
        return this;
    }

    public PurchaseBuilder withStore(String value) {
        purchase.setStore(value);
        return this;
    }

    public PurchaseBuilder withData(String value) {
        purchase.setData(value);
        return this;
    }

    public PurchaseBuilder withSignature(String value) {
        purchase.setSignature(value);
        return this;
    }
}

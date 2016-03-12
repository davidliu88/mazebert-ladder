package com.mazebert.usecases.shop;

import com.mazebert.entities.Player;
import com.mazebert.entities.Purchase;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.FoilCardGatewayMock;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.gateways.mocks.PurchaseGatewayMock;
import com.mazebert.plugins.security.mocks.GooglePlayPurchaseVerifierMock;
import com.mazebert.usecases.shop.CommitShopTransaction.Request;
import com.mazebert.usecases.shop.CommitShopTransaction.Request.Transaction;
import com.mazebert.usecases.shop.CommitShopTransaction.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.*;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;

public class CommitShopTransactionTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();
    private PurchaseGatewayMock purchaseGateway = new PurchaseGatewayMock();
    private GooglePlayPurchaseVerifierMock googlePlayPurchaseVerifier = new GooglePlayPurchaseVerifierMock();

    private Player player = a(player().casid());

    @Before
    public void setUp() {
        usecase = new CommitShopTransaction(playerGateway, foilCardGateway, purchaseGateway, googlePlayPurchaseVerifier);
    }

    @Test
    public void nullAppVersion() {
        givenRequest(a(request().withAppVersion(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("App version must not be null."));
    }

    @Test
    public void tooLowAppVersion() {
        givenRequest(a(request().withAppVersion("0.9.1")));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("At least app version 1.0.0 is required for this request."));
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Key must not be null."));
    }

    @Test
    public void nullStore() {
        givenRequest(a(request().withStore(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Store must not be null."));
    }

    @Test
    public void nullTransactions() {
        givenRequest(a(request().withTransactions(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Transactions must not be null."));
    }

    @Test
    public void playerNotFound() {
        givenRequest(a(request().withKey("unknown")));
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("Player does not exist."));
    }

    @Test
    public void storeNotFound() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withStore("unknown")));

        whenRequestIsExecuted();

        thenErrorIs(new NotFound("Store does not exist."));
    }

    @Test
    public void emptyTransactionsList() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withTransactions(a(list()))));

        whenRequestIsExecuted();

        thenNoProductsAreAdded();
    }

    @Test
    public void googlePlayTransactionSignatureCannotBeVerified() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withTransactions(a(list(
                a(transaction()
                        .cookieWithValidSignature() // OrderId instead of orderId
                        .withData("{\"OrderId\":\"12999763169054705758.1315551969063111\",\"packageName\":\"air.com.mazebert.MazebertTD\",\"productId\":\"com.mazebert.cookie\",\"purchaseTime\":1417163551553,\"purchaseState\":0,\"developerPayload\":\"TODO\",\"purchaseToken\":\"afeomijmllhbmfhpdliiapoi.AO-J1OxO6NXH_mNlTNhQG0nx0Aqd8sma7orKaY7FBaqOpYCqiRmBjc__C2WnbPYeHluIhO8-xMCaf0odRPRSb8IW3a1TI88p17xOMLSRiF0371Rwfct6y6OMWtDefsT4W_Ef7Hnmv5lIy_Ra-wcTlfvnHV0vuikiVg\"}")
                )
        )))));
        googlePlayPurchaseVerifier.givenRealVerificationIsDone();

        whenRequestIsExecuted();

        thenNoProductsAreAdded();
    }

    @Test
    public void googlePlayTransactionSignatureCanBeVerified() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withTransactions(a(list(
                a(transaction().cookieWithValidSignature())
        )))));
        googlePlayPurchaseVerifier.givenRealVerificationIsDone();

        whenRequestIsExecuted();

        thenProductsAreAdded(a(list(
                "com.mazebert.cookie"
        )));
    }

    @Test
    public void invalidPackageName() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withTransactions(a(list(
                a(transaction().cookieWithValidSignature()
                        .withData("{\"orderId\":\"12999763169054705758.1315551969063111\",\"packageName\":\"air.com.mazebert.NotMazebertTD\",\"productId\":\"com.mazebert.cookie\",\"purchaseTime\":1417163551553,\"purchaseState\":0,\"developerPayload\":\"TODO\",\"purchaseToken\":\"afeomijmllhbmfhpdliiapoi.AO-J1OxO6NXH_mNlTNhQG0nx0Aqd8sma7orKaY7FBaqOpYCqiRmBjc__C2WnbPYeHluIhO8-xMCaf0odRPRSb8IW3a1TI88p17xOMLSRiF0371Rwfct6y6OMWtDefsT4W_Ef7Hnmv5lIy_Ra-wcTlfvnHV0vuikiVg\"}")
                )
        )))));

        whenRequestIsExecuted();

        thenNoProductsAreAdded();
    }

    @Test
    public void invalidProductId() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withTransactions(a(list(
                a(transaction().cookieWithValidSignature()
                        .withData("{\"orderId\":\"12999763169054705758.1315551969063111\",\"packageName\":\"air.com.mazebert.MazebertTD\",\"productId\":\"com.mazebert.notcookie\",\"purchaseTime\":1417163551553,\"purchaseState\":0,\"developerPayload\":\"TODO\",\"purchaseToken\":\"afeomijmllhbmfhpdliiapoi.AO-J1OxO6NXH_mNlTNhQG0nx0Aqd8sma7orKaY7FBaqOpYCqiRmBjc__C2WnbPYeHluIhO8-xMCaf0odRPRSb8IW3a1TI88p17xOMLSRiF0371Rwfct6y6OMWtDefsT4W_Ef7Hnmv5lIy_Ra-wcTlfvnHV0vuikiVg\"}")
                )
        )))));

        whenRequestIsExecuted();

        thenNoProductsAreAdded();
    }

    @Test
    public void nullProductId() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withTransactions(a(list(
                a(transaction().cookieWithValidSignature()
                        .withProductId(null)
                )
        )))));

        whenRequestIsExecuted();

        thenNoProductsAreAdded();
    }

    @Test
    public void cookieReward() {
        playerGateway.givenPlayerExists(player);
        givenRequest(a(request().withTransactions(a(list(
                a(transaction().cookieWithValidSignature())
        )))));

        whenRequestIsExecuted();

        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(hero().cookieMonster()));
    }

    @Test
    public void beerReward() {
        playerGateway.givenPlayerExists(player);
        givenRequest(a(request().withTransactions(a(list(
                a(transaction().beer())
        )))));

        whenRequestIsExecuted();

        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(hero().innKeeper()));
    }

    @Test
    public void whiskyReward() {
        playerGateway.givenPlayerExists(player);
        givenRequest(a(request().withTransactions(a(list(
                a(transaction().whisky())
        )))));

        whenRequestIsExecuted();

        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(potion().angelicElixir()));
    }

    @Test
    public void purchasesArePersisted() {
        List<Transaction> transactions = a(list(
                a(transaction().beer()),
                a(transaction().whisky())
        ));
        playerGateway.givenPlayerExists(player);
        givenRequest(a(request().withTransactions(transactions)));

        whenRequestIsExecuted();

        thenPurchasesAreAddedForTransactions(transactions);
    }

    private void thenPurchasesAreAddedForTransactions(List<Transaction> transactions) {
        List<Purchase> addedPurchases = purchaseGateway.getPurchasesForPlayer(player);
        assertEquals(transactions.size(), addedPurchases.size());

        for (int i = 0; i < transactions.size(); ++i) {
            Transaction transaction = transactions.get(i);
            Purchase purchase = addedPurchases.get(i);

            assertEquals(player.getId(), purchase.getPlayerId());
            assertEquals(request.store, purchase.getStore());
            assertEquals(transaction.productId, purchase.getProductId());
            assertEquals(transaction.data, purchase.getData());
            assertEquals(transaction.signature, purchase.getSignature());
        }
    }

    @Test
    public void productAlreadyPurchased() {
        // TODO no reward is added
    }

    private void thenNoProductsAreAdded() {
        thenProductsAreAdded(a(list()));
        foilCardGateway.thenNoFoilCardsWereAddedToPlayer(player);
    }

    private void thenProductsAreAdded(List<String> expected) {
        assertEquals(expected, response.verifiedProductIds);
        assertEquals(expected.size(), purchaseGateway.getPurchasesForPlayer(player).size());
    }

    private RequestBuilder request() {
        return new RequestBuilder()
                .withAppVersion("1.0.0")
                .withKey("abcdef")
                .withStore("GooglePlay")
                .withTransactions(a(list(
                        a(transaction())
                )));
    }

    private TransactionBuilder transaction() {
        return new TransactionBuilder();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        @Override
        public Request build() {
            return request;
        }

        public RequestBuilder withAppVersion(String value) {
            request.appVersion = value;
            return this;
        }

        public RequestBuilder withKey(String value) {
            request.key = value;
            return this;
        }

        public RequestBuilder withStore(String value) {
            request.store = value;
            return this;
        }

        public RequestBuilder withTransactions(List<Transaction> value) {
            request.transactions = value;
            return this;
        }
    }

    private static class TransactionBuilder implements Builder<Transaction> {
        private Transaction transaction = new Transaction();

        public TransactionBuilder cookieWithValidSignature() {
            return this
                    .withProductId("com.mazebert.cookie")
                    .withData("{\"orderId\":\"12999763169054705758.1315551969063111\",\"packageName\":\"air.com.mazebert.MazebertTD\",\"productId\":\"com.mazebert.cookie\",\"purchaseTime\":1417163551553,\"purchaseState\":0,\"developerPayload\":\"TODO\",\"purchaseToken\":\"afeomijmllhbmfhpdliiapoi.AO-J1OxO6NXH_mNlTNhQG0nx0Aqd8sma7orKaY7FBaqOpYCqiRmBjc__C2WnbPYeHluIhO8-xMCaf0odRPRSb8IW3a1TI88p17xOMLSRiF0371Rwfct6y6OMWtDefsT4W_Ef7Hnmv5lIy_Ra-wcTlfvnHV0vuikiVg\"}")
                    .withSignature("sp4Qwws1hJ3PuZZPopibvAIjITBdyof69DxQUE6am7wl2YtWFEM19Lpmq+sD2qLHrg7OBjklQ8iQmRGa8b8qPtjhWQ30cVoHJFQMI2NR1nLiOU9/hXP8eYz0nq46L4wvd0UYYpYUVXo0/oM22fC/JSuHWURJhfE4+ptJokedEH9hTpwC+86zl1zLMZ4NoE5krmlV9GDDLN8xBcR/7gsXUh7hM9X0zc/BNsLimsJZVEClQcxsqZykCVLglaFtqS4CyCCKv5jiCHOYxXvQ7zQlPe0tUaRUYijIK2HVKQolh3Y5dTV0lsCAdDy/5099FgUXvAs9ARaTFVvMPislrVnfYA==");
        }

        public TransactionBuilder beer() {
            return this
                    .withProductId("com.mazebert.beer")
                    .withData("{\"orderId\":\"12999763169054705758.1315551969063111\",\"packageName\":\"air.com.mazebert.MazebertTD\",\"productId\":\"com.mazebert.beer\",\"purchaseTime\":1417163551553,\"purchaseState\":0,\"developerPayload\":\"TODO\",\"purchaseToken\":\"afeomijmllhbmfhpdliiapoi.AO-J1OxO6NXH_mNlTNhQG0nx0Aqd8sma7orKaY7FBaqOpYCqiRmBjc__C2WnbPYeHluIhO8-xMCaf0odRPRSb8IW3a1TI88p17xOMLSRiF0371Rwfct6y6OMWtDefsT4W_Ef7Hnmv5lIy_Ra-wcTlfvnHV0vuikiVg\"}")
                    .withSignature("-");
        }

        public TransactionBuilder whisky() {
            return this
                    .withProductId("com.mazebert.whisky")
                    .withData("{\"orderId\":\"12999763169054705758.1315551969063111\",\"packageName\":\"air.com.mazebert.MazebertTD\",\"productId\":\"com.mazebert.whisky\",\"purchaseTime\":1417163551553,\"purchaseState\":0,\"developerPayload\":\"TODO\",\"purchaseToken\":\"afeomijmllhbmfhpdliiapoi.AO-J1OxO6NXH_mNlTNhQG0nx0Aqd8sma7orKaY7FBaqOpYCqiRmBjc__C2WnbPYeHluIhO8-xMCaf0odRPRSb8IW3a1TI88p17xOMLSRiF0371Rwfct6y6OMWtDefsT4W_Ef7Hnmv5lIy_Ra-wcTlfvnHV0vuikiVg\"}")
                    .withSignature("-");
        }

        public TransactionBuilder withProductId(String value) {
            transaction.productId = value;
            return this;
        }

        public TransactionBuilder withData(String value) {
            transaction.data = value;
            return this;
        }

        public TransactionBuilder withSignature(String value) {
            transaction.signature = value;
            return this;
        }

        @Override
        public Transaction build() {
            return transaction;
        }
    }
}
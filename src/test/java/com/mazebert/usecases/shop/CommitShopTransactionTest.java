package com.mazebert.usecases.shop;

import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.plugins.security.mocks.GooglePlayPurchaseVerifierMock;
import com.mazebert.usecases.shop.CommitShopTransaction.Request;
import com.mazebert.usecases.shop.CommitShopTransaction.Request.Transaction;
import com.mazebert.usecases.shop.CommitShopTransaction.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.player;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;

public class CommitShopTransactionTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private GooglePlayPurchaseVerifierMock googlePlayPurchaseVerifier = new GooglePlayPurchaseVerifierMock();

    @Before
    public void setUp() {
        usecase = new CommitShopTransaction(playerGateway, googlePlayPurchaseVerifier);
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

        thenNoProductsAreVerified();
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

        thenNoProductsAreVerified();
    }

    @Test
    public void googlePlayTransactionSignatureCanBeVerified() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withTransactions(a(list(
                a(transaction().cookieWithValidSignature())
        )))));
        googlePlayPurchaseVerifier.givenRealVerificationIsDone();

        whenRequestIsExecuted();

        thenVerifiedProductIdsAre(a(list(
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
        googlePlayPurchaseVerifier.givenRealVerificationIsDone();

        whenRequestIsExecuted();

        thenNoProductsAreVerified();
    }

    @Test
    public void invalidProductId() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withTransactions(a(list(
                a(transaction().cookieWithValidSignature()
                        .withData("{\"orderId\":\"12999763169054705758.1315551969063111\",\"packageName\":\"air.com.mazebert.MazebertTD\",\"productId\":\"com.mazebert.notcookie\",\"purchaseTime\":1417163551553,\"purchaseState\":0,\"developerPayload\":\"TODO\",\"purchaseToken\":\"afeomijmllhbmfhpdliiapoi.AO-J1OxO6NXH_mNlTNhQG0nx0Aqd8sma7orKaY7FBaqOpYCqiRmBjc__C2WnbPYeHluIhO8-xMCaf0odRPRSb8IW3a1TI88p17xOMLSRiF0371Rwfct6y6OMWtDefsT4W_Ef7Hnmv5lIy_Ra-wcTlfvnHV0vuikiVg\"}")
                )
        )))));
        googlePlayPurchaseVerifier.givenRealVerificationIsDone();

        whenRequestIsExecuted();

        thenNoProductsAreVerified();
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

        thenNoProductsAreVerified();
    }

    @Test
    public void productNotYetPurchased() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withTransactions(a(list(
                a(transaction().cookieWithValidSignature())
        )))));
        googlePlayPurchaseVerifier.givenRealVerificationIsDone();

        whenRequestIsExecuted();

        // TODO reward is added
        // TODO purchase is added to gateway
    }

    @Test
    public void productAlreadyPurchased() {
        // TODO no reward is added
    }

    private void thenNoProductsAreVerified() {
        thenVerifiedProductIdsAre(a(list()));
    }

    private void thenVerifiedProductIdsAre(List<String> expected) {
        Assert.assertEquals(expected, response.verifiedProductIds);
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
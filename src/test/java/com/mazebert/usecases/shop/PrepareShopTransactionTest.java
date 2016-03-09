package com.mazebert.usecases.shop;

import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.usecases.shop.PrepareShopTransaction.Request;
import com.mazebert.usecases.shop.PrepareShopTransaction.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;

public class PrepareShopTransactionTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();

    @Before
    public void setUp() {
        usecase = new PrepareShopTransaction(playerGateway);
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
    public void nullProductId() {
        givenRequest(a(request().withProductId(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Product ID must not be null."));
    }

    @Test
    public void playerNotFound() {
        playerGateway.givenPlayerExists(a(player().casid()));
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
    public void googlePlayDeveloperPayload() {
        playerGateway.givenPlayerExists(a(player().casid().withId(1000)));
        givenRequest(a(request().withProductId("com.mazebert.beer")));

        whenRequestIsExecuted();

        thenDeveloperPayloadIs("1000-com.mazebert.beer");
    }

    private void thenDeveloperPayloadIs(String expected) {
        assertEquals(expected, response.developerPayload);
    }

    private RequestBuilder request() {
        return new RequestBuilder()
                .withAppVersion("1.0.0")
                .withKey("abcdef")
                .withStore("GooglePlay")
                .withProductId("com.mazebert.cookie");
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

        public RequestBuilder withProductId(String value) {
            request.productId = value;
            return this;
        }
    }
}
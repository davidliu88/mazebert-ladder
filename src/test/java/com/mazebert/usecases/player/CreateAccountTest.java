package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.gateways.error.KeyAlreadyExists;
import com.mazebert.plugins.random.mocks.PlayerKeyGeneratorMock;
import com.mazebert.usecases.player.CreateAccount.Request;
import com.mazebert.usecases.player.CreateAccount.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;

public class CreateAccountTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private PlayerKeyGeneratorMock keyGenerator = new PlayerKeyGeneratorMock();

    @Before
    public void setUp() {
        usecase = new CreateAccount(playerGateway, keyGenerator);
    }

    @Test
    public void nullAppVersion() {
        givenRequest(a(request().withAppVersion(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("App version must not be null."));
    }

    @Test
    public void tooLowAppVersion() {
        givenRequest(a(request().withAppVersion("0.0.1")));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("At least app version 0.4.0 is required for this request."));
    }

    @Test
    public void nullName() {
        givenRequest(a(request().withName(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player name must not be null"));
    }

    @Test
    public void emptyName() {
        givenRequest(a(request().withName("")));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player name must not be empty"));
    }

    @Test
    public void zeroLevel() {
        givenRequest(a(request().withLevel(0)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player level must be greater than 0"));
    }

    @Test
    public void negativeLevel() {
        givenRequest(a(request().withLevel(-1)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player level must be greater than 0"));
    }

    @Test
    public void zeroExperience() {
        givenRequest(a(request().withExperience(0)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player experience must be greater than 0"));
    }

    @Test
    public void negativeExperience() {
        givenRequest(a(request().withExperience(-1)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player experience must be greater than 0"));
    }

    @Test
    public void playerIsCreatedAccordingToRequest() {
        givenRequest(a(request()
            .withName("Herul")
            .withLevel(99)
            .withExperience(10000000)));

        whenRequestIsExecuted();

        Player addedPlayer = playerGateway.getAddedPlayer();
        assertEquals("Herul", addedPlayer.getName());
        assertEquals(99, addedPlayer.getLevel());
        assertEquals(10000000, addedPlayer.getExperience());
    }

    @Test
    public void responseContainsPlayerId() {
        givenRequest(a(request()));
        playerGateway.givenNextPlayerId(255);

        whenRequestIsExecuted();

        assertEquals(255, response.id);
    }

    @Test
    public void playerKeyInResponse_equalToPlayerKeyAddedToGateway() {
        givenRequest(a(request()));
        keyGenerator.givenPlayerKeysWillBeGenerated("abcdef", "123456");

        whenRequestIsExecuted();

        assertEquals("abcdef", playerGateway.getAddedPlayer().getKey());
        assertEquals("abcdef", response.key);
    }

    @Test
    public void keyIsGeneratedUntilPlayerIsUnique() {
        givenRequest(a(request()));
        playerGateway.givenOperationFailsWithException(
                new KeyAlreadyExists(),
                new KeyAlreadyExists()
        );
        keyGenerator.givenPlayerKeysWillBeGenerated("abcdef", "123456", "9gagtv");

        whenRequestIsExecuted();

        assertEquals("9gagtv", response.key);
    }


    private RequestBuilder request() {
        return new RequestBuilder().golden();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        public RequestBuilder golden() {
            return this
                    .withAppVersion("1.0.0")
                    .withName("Ontrose")
                    .withLevel(4)
                    .withExperience(1000);
        }

        public RequestBuilder withName(String value) {
            request.name = value;
            return this;
        }

        public RequestBuilder withLevel(int value) {
            request.level = value;
            return this;
        }

        public RequestBuilder withExperience(long value) {
            request.experience = value;
            return this;
        }

        public RequestBuilder withAppVersion(String value) {
            request.appVersion = value;
            return this;
        }

        public Request build() {
            return request;
        }
    }
}
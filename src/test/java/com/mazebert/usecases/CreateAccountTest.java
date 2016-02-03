package com.mazebert.usecases;

import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.error.KeyAlreadyExists;
import com.mazebert.gateways.PlayerGatewayCoach;
import com.mazebert.plugins.random.PlayerKeyGeneratorCoach;
import com.mazebert.usecases.CreateAccount.Request;
import com.mazebert.usecases.CreateAccount.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;

public class CreateAccountTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayCoach playerGateway;
    private PlayerKeyGeneratorCoach keyGenerator;

    @Before
    public void setUp() {
        playerGateway = new PlayerGatewayCoach();
        keyGenerator = new PlayerKeyGeneratorCoach();
        usecase = new CreateAccount(playerGateway, keyGenerator);
    }

    @Test
    public void nullName() {
        givenRequest(a(request().withName(null)));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Player name must not be null"));
    }

    @Test
    public void emptyName() {
        givenRequest(a(request().withName("")));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Player name must not be empty"));
    }

    @Test
    public void zeroLevel() {
        givenRequest(a(request().withLevel(0)));
        whenRequestIsExecuted();
        thenErrorMessageIs("Player level must be greater than 0");
        thenErrorIs(new Error(Type.BAD_REQUEST, "Player level must be greater than 0"));
    }

    @Test
    public void negativeLevel() {
        givenRequest(a(request().withLevel(-1)));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Player level must be greater than 0"));
    }

    @Test
    public void zeroExperience() {
        givenRequest(a(request().withExperience(0)));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Player experience must be greater than 0"));
    }

    @Test
    public void negativeExperience() {
        givenRequest(a(request().withExperience(-1)));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Player experience must be greater than 0"));
    }

    @Test
    public void playerIsCreatedAccordingToRequest() {
        givenRequest(a(request()
            .withName("Herul")
            .withLevel(99)
            .withExperience(10000000)));

        whenRequestIsExecuted();

        Player addedPlayer = playerGateway.getAddedPlayer();
        assertEquals("Herul", addedPlayer.name);
        assertEquals(99, addedPlayer.level);
        assertEquals(10000000, addedPlayer.experience);
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

        assertEquals("abcdef", playerGateway.getAddedPlayer().key);
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

        public Request build() {
            return request;
        }
    }
}
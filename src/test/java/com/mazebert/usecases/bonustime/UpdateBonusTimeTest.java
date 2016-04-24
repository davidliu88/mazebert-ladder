package com.mazebert.usecases.bonustime;

import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.error.Unauthorized;
import com.mazebert.gateways.mocks.BonusTimeGatewayMock;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.usecases.bonustime.UpdateBonusTime.Request;
import com.mazebert.usecases.bonustime.UpdateBonusTime.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;

public class UpdateBonusTimeTest extends UsecaseTest<Request, Response> {
    private BonusTimeGatewayMock bonusTimeGateway = new BonusTimeGatewayMock();
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();

    @Before
    public void setUp() {
        usecase = new UpdateBonusTime(bonusTimeGateway, playerGateway);
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player key is required."));
    }

    @Test
    public void invalidDifficultyType_tooSmall() {
        givenRequest(a(request().withDifficultyType(-1)));
        whenRequestIsExecuted();
        thenDifficultyTypeIsInvalid();
    }

    @Test
    public void invalidDifficultyType_tooLarge() {
        givenRequest(a(request().withDifficultyType(3)));
        whenRequestIsExecuted();
        thenDifficultyTypeIsInvalid();
    }

    @Test
    public void waveAmountType_tooSmall() {
        givenRequest(a(request().withWaveAmountType(-1)));
        whenRequestIsExecuted();
        thenWaveAmountTypeIsInvalid();
    }

    @Test
    public void waveAmountType_tooLarge() {
        givenRequest(a(request().withWaveAmountType(4)));
        whenRequestIsExecuted();
        thenWaveAmountTypeIsInvalid();
    }

    @Test
    public void zeroSecondsSurvived() {
        givenRequest(a(request().withSecondsSurvived(0)));
        whenRequestIsExecuted();
        thenSurvivalTimeIsInvalid();
    }

    @Test
    public void negativeSecondsSurvived() {
        givenRequest(a(request().withSecondsSurvived(-10)));
        whenRequestIsExecuted();
        thenSurvivalTimeIsInvalid();
    }

    @Test
    public void playerNotFound() {
        playerGateway.givenNoPlayerExists();
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenErrorIs(new NotFound("A player with this key could not be found."));
    }

    @Test
    public void incompatibleVersion() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withAppVersion("1.3.0")));
        whenRequestIsExecuted();
        thenErrorIs(new Unauthorized("This game version can no longer submit bonus round scores."));
    }

    @Test
    public void zeroMapId() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withMapId(0)));

        whenRequestIsExecuted();

        UpdateBonusTime.Request updateRequest = bonusTimeGateway.getUpdateRequest();
        assertEquals(1, updateRequest.mapId);
    }

    @Test
    public void positiveMapId() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().withMapId(3)));

        whenRequestIsExecuted();

        UpdateBonusTime.Request updateRequest = bonusTimeGateway.getUpdateRequest();
        assertEquals(3, updateRequest.mapId);
    }

    @Test
    public void bonusTimeIsUpdated() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        UpdateBonusTime.Request updateRequest = bonusTimeGateway.getUpdateRequest();
        assertEquals(request, updateRequest);
        assertEquals("Request needs to be extended by player id", 115, updateRequest.playerId);
        thenResponseIsNotNull();
    }

    private void thenDifficultyTypeIsInvalid() {
        thenErrorIs(new BadRequest("Difficulty type must be in range [0, 2]"));
    }

    private void thenWaveAmountTypeIsInvalid() {
        thenErrorIs(new BadRequest("Wave amount type must be in range [0, 3]"));
    }

    private void thenSurvivalTimeIsInvalid() {
        thenErrorIs(new BadRequest("Survival time must be positive"));
    }

    private RequestBuilder request() {
        return new RequestBuilder()
                .withKey("abcdef")
                .withAppVersion("1.4.0")
                .withDifficultyType(0)
                .withWaveAmountType(0)
                .withMapId(1)
                .withSecondsSurvived(100);
    }

    public static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        @Override
        public Request build() {
            return request;
        }

        public RequestBuilder withPlayerId(long value) {
            request.playerId = value;
            return this;
        }

        public RequestBuilder withKey(String value) {
            request.key = value;
            return this;
        }

        public RequestBuilder withAppVersion(String value) {
            request.appVersion = value;
            return this;
        }

        public RequestBuilder withMapId(int value) {
            request.mapId = value;
            return this;
        }

        public RequestBuilder withSecondsSurvived(int value) {
            request.secondsSurvived = value;
            return this;
        }

        public RequestBuilder withDifficultyType(int difficultyType) {
            request.difficultyType = difficultyType;
            return this;
        }

        public RequestBuilder withWaveAmountType(int waveAmountType) {
            request.waveAmountType = waveAmountType;
            return this;
        }
    }
}
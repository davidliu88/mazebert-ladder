package com.mazebert.usecases.bonustime;

import com.mazebert.entities.PlayerBonusTime;
import com.mazebert.error.BadRequest;
import com.mazebert.gateways.mocks.BonusTimeGatewayMock;
import com.mazebert.usecases.bonustime.GetBonusTimes.Request;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.List;

import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.listWith;

public class GetBonusTimesTest extends UsecaseTest<Request, List<PlayerBonusTime>> {
    private BonusTimeGatewayMock bonusTimeGateway = new BonusTimeGatewayMock();

    private static List<PlayerBonusTime> playerBonusTimesOnFirstMap = a(listWith(
    ));

    @Before
    public void setUp() {
        usecase = new GetBonusTimes(bonusTimeGateway);
    }

    @Test
    public void zeroMapId() {
        bonusTimeGateway.givenBonusTimes("M1D*W*V1.0.0", playerBonusTimesOnFirstMap);
        givenRequest(a(request().withMapId(0)));
        whenRequestIsExecuted();
        thenResponseIs(playerBonusTimesOnFirstMap);
    }

    @Test
    public void nullDifficultyType() {
        givenRequest(a(request().withDifficultyType(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Difficulty type must not be null."));
    }

    @Test
    public void nullWaveAmountType() {
        givenRequest(a(request().withWaveAmountType(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Wave amount type must not be null."));
    }

    @Test
    public void negativeStart() {
        givenRequest(a(request().withStart(-3)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Start must not be negative."));
    }

    @Test
    public void negativeLimit() {
        givenRequest(a(request().withLimit(-10)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Limit must not be negative."));
    }

    @Test
    public void parametersArePassedToGateway() {
        bonusTimeGateway.givenBonusTimes("M1D2W3V1.2.3", playerBonusTimesOnFirstMap);
        givenRequest(a(request()
                .withMapId(1)
                .withDifficultyType("2")
                .withWaveAmountType("3")
                .withAppVersion("1.2.3")
        ));
        whenRequestIsExecuted();
        thenResponseIs(playerBonusTimesOnFirstMap);
    }

    private RequestBuilder request() {
        return new RequestBuilder()
                .withMapId(1)
                .withDifficultyType("*")
                .withWaveAmountType("*")
                .withAppVersion("1.0.0");
    }

    public static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        @Override
        public Request build() {
            return request;
        }

        public RequestBuilder withMapId(int value) {
            request.mapId = value;
            return this;
        }

        public RequestBuilder withDifficultyType(String value) {
            request.difficultyType = value;
            return this;
        }

        public RequestBuilder withWaveAmountType(String value) {
            request.waveAmountType = value;
            return this;
        }

        public RequestBuilder withAppVersion(String value) {
            request.appVersion = value;
            return this;
        }

        public RequestBuilder withStart(int value) {
            request.start = value;
            return this;
        }

        public RequestBuilder withLimit(int value) {
            request.limit = value;
            return this;
        }
    }
}
package com.mazebert.usecases.bonustime;

import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;
import com.mazebert.usecases.bonustime.UpdateBonusTime.Request;
import com.mazebert.usecases.bonustime.UpdateBonusTime.Response;

import static org.jusecase.builders.BuilderFactory.a;

public class UpdateBonusTimeTest extends UsecaseTest<Request, Response> {

    @Before
    public void setUp() {
        usecase = new UpdateBonusTime();
    }

    @Test
    public void firstTest() {
        givenRequest(a(request()));
        whenRequestIsExecuted();
        // TODO make this test red!
    }

    private RequestBuilder request() {
        return new RequestBuilder();
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
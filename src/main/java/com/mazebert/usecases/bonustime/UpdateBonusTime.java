package com.mazebert.usecases.bonustime;

import org.jusecase.Usecase;

public class UpdateBonusTime implements Usecase<UpdateBonusTime.Request, UpdateBonusTime.Response> {
    public Response execute(Request request) {
        // TODO write a failing unit test
        return null;
    }

    public static class Request {
        public long playerId;
        public int mapId;
        public int secondsSurvived;
        public int difficultyType;
        public int waveAmountType;
    }

    public static class Response {
    }
}

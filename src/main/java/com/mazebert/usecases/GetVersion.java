package com.mazebert.usecases;

import org.jusecase.Usecase;

public class GetVersion implements Usecase<GetVersion.Request, GetVersion.Response> {
    public static class Request {
    }

    public static class Response {
        public String version;
    }

    public Response execute(Request request) {
        Response response = new Response();
        response.version = "todo";
        return response;
    }
}
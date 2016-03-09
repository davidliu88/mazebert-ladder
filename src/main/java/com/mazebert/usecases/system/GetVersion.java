package com.mazebert.usecases.system;

import org.jusecase.Usecase;

import javax.inject.Singleton;

@Singleton
public class GetVersion implements Usecase<GetVersion.Request, GetVersion.Response> {
    public static class Request {
    }

    public static class Response {
        public String version;
    }

    public Response execute(Request request) {
        // TODO grab version from pom.xml and implement me!

        Response response = new Response();
        response.version = "todo";
        return response;
    }
}
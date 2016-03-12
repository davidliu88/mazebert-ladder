package com.mazebert.usecases.system;

import com.mazebert.error.NotFound;
import org.jusecase.Usecase;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.Properties;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

@Singleton
public class GetVersion implements Usecase<GetVersion.Request, GetVersion.Response> {
    public static class Request {
    }

    public static class Response {
        public String version;
    }

    public Response execute(Request request) {
        Properties properties = new Properties();
        try {
            properties.load(a(inputStream().withResource("application.properties")));
        } catch (IOException e) {
            throw new NotFound("Failed to read version property file. Error was: " + e.getMessage());
        }

        Response response = new Response();
        response.version = properties.getProperty("version");
        return response;
    }
}
package com.mazebert.usecases.security;

import com.mazebert.plugins.security.ServerContentSigner;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.InputStream;

@Singleton
public class SignServerResponse implements Usecase<SignServerResponse.Request, SignServerResponse.Response> {
    private final ServerContentSigner contentSigner;

    @Inject
    public SignServerResponse(ServerContentSigner contentSigner) {
        this.contentSigner = contentSigner;
    }

    public Response execute(Request request) {
        Response response = new Response();
        response.signature = contentSigner.sign(request.content);
        return response;
    }

    public static class Request {
        public InputStream content;
    }

    public static class Response {
        public String signature;
    }
}

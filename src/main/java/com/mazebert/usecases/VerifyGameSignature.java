package com.mazebert.usecases;

import com.mazebert.error.Error;
import com.mazebert.error.Type;
import org.jusecase.Usecase;

import java.io.InputStream;

public class VerifyGameSignature implements Usecase<VerifyGameSignature.Request, Void> {
    public static class Request {
        public InputStream body;
        public String signature;
    }

    public static class Response {
    }

    public Void execute(Request request) {
        validate(request);

        return null;
    }

    private void validate(Request request) {
        if (request.body == null) {
            failAuthentication("Request body must not be null.");
        } else if (request.signature == null) {
            failAuthentication("Request signature must not be null.");
        }
    }

    private void failAuthentication(String message) {
        throw new Error(Type.UNAUTHORIZED, "The given request was not sent by a valid game client. " + message);
    }
}

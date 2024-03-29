package com.mazebert.usecases.security;

import com.mazebert.error.Unauthorized;
import com.mazebert.plugins.security.GameContentVerifier;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.InputStream;

@Singleton
public class VerifyGameRequest implements Usecase<VerifyGameRequest.Request, Void> {
    private final GameContentVerifier gameContentVerifier;

    @Inject
    public VerifyGameRequest(GameContentVerifier gameContentVerifier) {
        this.gameContentVerifier = gameContentVerifier;
    }

    public Void execute(Request request) {
        validate(request);

        if (! gameContentVerifier.verify(request.body, request.signature)) {
            failAuthentication("Request signature is invalid.");
        }

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
        throw new Unauthorized("The given request was not sent by a valid game client. " + message);
    }

    public static class Request {
        public InputStream body;
        public String signature;
    }
}

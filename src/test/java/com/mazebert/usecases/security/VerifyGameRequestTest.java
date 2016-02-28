package com.mazebert.usecases.security;

import com.mazebert.error.Unauthorized;
import com.mazebert.plugins.security.GameContentVerifier;
import com.mazebert.usecases.security.VerifyGameRequest.Request;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class VerifyGameRequestTest extends UsecaseTest<Request, Void> {
    private static GameContentVerifier gameContentVerifier = new GameContentVerifier();

    @Before
    public void setUp() {
        usecase = new VerifyGameRequest(gameContentVerifier);
    }

    @Test
    public void nullBody() {
        givenRequest(a(request().withBody((InputStream) null)));
        whenRequestIsExecuted();
        thenAuthorizationFails("Request body must not be null.");
    }

    @Test
    public void nullSignature() {
        givenRequest(a(request().withSignature(null)));
        whenRequestIsExecuted();
        thenAuthorizationFails("Request signature must not be null.");
    }

    @Test
    public void invalidSignatureFormat() {
        givenRequest(a(request()
                .withBody("{\"level\":68,\"ladderVersion\":2,\"experience\":306801,\"appVersion\":\"0.10.1\",\"name\":\"casid\"}")
                .withSignature("§%tE")
        ));
        whenRequestIsExecuted();
        thenAuthorizationFails("Request signature is invalid.");
    }

    @Test
    public void invalidContent() {
        givenRequest(a(request()
                .withBody("{\"level\":68,\"ladderVersion\":2,\"experience\":306801,\"appVersion\":\"0.10.1\",\"name\":\"casid\"}")
                .withSignature("401af98ed9f9cd47e363fc4a583845c74d8843d3d5f5c1ec77a1fcabd3ed86ac")
        ));
        whenRequestIsExecuted();
        thenAuthorizationFails("Request signature is invalid.");
    }

    @Test
    public void validContent() {
        givenRequest(a(request()
                .withBody("{\"ladderVersion\":2,\"level\":1,\"appVersion\":\"0.10.1\",\"name\":\"Hodor\",\"experience\":0}")
                .withSignature("0a75ba768a6b123d6daf78b26b16bc5447738535a929e4c91bac7dd58fbe1120")
        ));
        whenRequestIsExecuted();
        thenContentIsVerified();
    }

    private void thenAuthorizationFails(String message) {
        thenErrorIs(new Unauthorized("The given request was not sent by a valid game client. " + message));
    }

    private void thenContentIsVerified() {
        assertNull(error);
    }

    private RequestBuilder request() {
        return new RequestBuilder().golden();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        public RequestBuilder golden() {
            return this
                    .withBody(a(inputStream()))
                    .withSignature("1234");
        }

        public RequestBuilder withBody(InputStream value) {
            request.body = value;
            return this;
        }

        public RequestBuilder withBody(String value) {
            request.body = a(inputStream().withString(value));
            return this;
        }

        public RequestBuilder withSignature(String value) {
            request.signature = value;
            return this;
        }

        public Request build() {
            return request;
        }
    }
}
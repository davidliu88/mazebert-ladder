package com.mazebert.usecases.security;

import com.mazebert.error.Unauthorized;
import com.mazebert.usecases.security.VerifyGameSignature.Request;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.io.InputStream;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class VerifyGameSignatureTest extends UsecaseTest<Request, Void> {

    @Before
    public void setUp() {
        usecase = new VerifyGameSignature();
    }

    @Test
    public void nullBody() {
        givenRequest(a(request().withBody(null)));
        whenRequestIsExecuted();
        thenAuthorizationFails("Request body must not be null.");
    }

    @Test
    public void nullSignature() {
        givenRequest(a(request().withSignature(null)));
        whenRequestIsExecuted();
        thenAuthorizationFails("Request signature must not be null.");
    }

    private void thenAuthorizationFails(String message) {
        thenErrorIs(new Unauthorized("The given request was not sent by a valid game client. " + message));
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

        public RequestBuilder withSignature(String value) {
            request.signature = value;
            return this;
        }

        public Request build() {
            return request;
        }
    }
}
package com.mazebert.presenters.jaxrs;

import com.mazebert.error.Error;
import com.mazebert.error.Type;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class ErrorExceptionMapperTest {
    private ErrorExceptionMapper exceptionMapper = new ErrorExceptionMapper();
    private Error error;
    private Response response;

    @Test
    public void nullError() {
        givenError(null);
        whenErrorIsMapped();
        thenResponseStatusCodeIs(500);
        thenResponseMessageIs("Received an error without a descriptive message!");
    }

    @Test
    public void statusCodeIsMappedCorrectly_badRequest() {
        givenError(new Error(Type.BAD_REQUEST));
        whenErrorIsMapped();
        thenResponseStatusCodeIs(400);
    }

    @Test
    public void statusCodeIsMappedCorrectly_unauthorized() {
        givenError(new Error(Type.UNAUTHORIZED));
        whenErrorIsMapped();
        thenResponseStatusCodeIs(401);
    }

    @Test
    public void statusCodeIsMappedCorrectly_notFound() {
        givenError(new Error(Type.NOT_FOUND));
        whenErrorIsMapped();
        thenResponseStatusCodeIs(404);
    }

    @Test
    public void statusCodeIsMappedCorrectly_internalServerError() {
        givenError(new Error(Type.INTERNAL_SERVER_ERROR));
        whenErrorIsMapped();
        thenResponseStatusCodeIs(500);
    }

    @Test
    public void messageIsMappedCorrectly() {
        givenError(new Error(Type.BAD_REQUEST, "Something bad happened!"));
        whenErrorIsMapped();
        thenResponseMessageIs("Something bad happened!");
    }

    private void givenError(Error error) {
        this.error = error;
    }

    private void whenErrorIsMapped() {
        response = exceptionMapper.toResponse(error);
    }

    private void thenResponseStatusCodeIs(int statusCode) {
        assertEquals(statusCode, response.getStatus());
        thenFixedResponseParametersAreSetCorrectly();
    }

    private void thenResponseMessageIs(String message) {
        ErrorResponse content = (ErrorResponse) response.getEntity();
        assertEquals(message, content.error);
        thenFixedResponseParametersAreSetCorrectly();
    }

    private void thenFixedResponseParametersAreSetCorrectly() {
        ErrorResponse content = (ErrorResponse) response.getEntity();
        assertEquals("error", content.status);
    }
}
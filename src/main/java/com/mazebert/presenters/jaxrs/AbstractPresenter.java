package com.mazebert.presenters.jaxrs;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mazebert.Logic;
import com.mazebert.error.Error;
import com.mazebert.error.InternalServerError;
import com.mazebert.presenters.jaxrs.response.ErrorExceptionMapper;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.presenters.jaxrs.response.stream.MergeStatusWithResponse;
import com.mazebert.presenters.jaxrs.response.stream.PlainResponse;
import com.mazebert.presenters.jaxrs.response.stream.SignedResponseStream;
import com.mazebert.presenters.jaxrs.response.stream.WrapStatusAndResponse;
import com.mazebert.usecases.security.SecureRequest;
import com.mazebert.usecases.security.SecureResponse;
import com.mazebert.usecases.security.VerifyGameRequest;
import org.jusecase.UsecaseExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractPresenter {

    private final UsecaseExecutor usecaseExecutor;

    private final ObjectMapper objectMapper;

    @Context
    HttpServletRequest servletRequest;

    @Context
    UriInfo uriInfo;

    protected AbstractPresenter() {
        usecaseExecutor = getUsecaseExecutor();

        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    protected UsecaseExecutor getUsecaseExecutor() {
        return Logic.instance;
    }

    public <Request> Response execute(Request request) {
        if (isVerificationRequired(request)) {
            verifyRequest();
        }

        try {
            return createResponse(request, usecaseExecutor.execute(request));
        } catch (Error error) {
            return createErrorResponse(request, error);
        } catch (Throwable throwable) {
            return createErrorResponse(request, new InternalServerError("Unexpected error: " + throwable.getMessage(), throwable));
        }
    }

    private <Request> Response createResponse(Request request, Object response) {
        return Response
                .status(Response.Status.OK)
                .entity(createResponseStream(request, response))
                .build();
    }

    private StreamingOutput createResponseStream(Object request, Object response) {
        if (isSignatureRequired(request)) {
            return createSignedResponseStream(request, response);
        } else {
            return createUnsignedResponseStream(request, response);
        }
    }

    private Response createErrorResponse(Object request, Error error) {
        Response errorResponse = new ErrorExceptionMapper().toResponse(error);
        return Response
                .status(error.getStatusCode())
                .entity(createErrorResponseStream(request, errorResponse))
                .build();
    }

    private StreamingOutput createErrorResponseStream(Object request, Response errorResponse) {
        StreamingOutput output = new PlainResponse(objectMapper.getFactory(), errorResponse.getEntity());
        if (isSignatureRequired(request)) {
            output = new SignedResponseStream(usecaseExecutor, output);
        }
        return output;
    }

    private StreamingOutput createSignedResponseStream(Object request, Object response) {
        StreamingOutput output = createUnsignedResponseStream(request, response);
        return new SignedResponseStream(usecaseExecutor, output);
    }

    private StreamingOutput createUnsignedResponseStream(Object request, Object response) {
        JsonFactory jsonFactory = objectMapper.getFactory();

        if (request.getClass().isAnnotationPresent(StatusResponse.class)) {
            StatusResponse statusResponseAnnotation = request.getClass().getAnnotation(StatusResponse.class);
            if (statusResponseAnnotation.field().isEmpty()) {
                return new MergeStatusWithResponse(jsonFactory, response);
            } else {
                return new WrapStatusAndResponse(jsonFactory, response, statusResponseAnnotation.field());
            }
        } else {
            return new PlainResponse(jsonFactory, response);
        }
    }

    private boolean isSignatureRequired(Object request) {
        return request.getClass().isAnnotationPresent(SecureResponse.class);
    }

    private boolean isVerificationRequired(Object request) {
        return request.getClass().isAnnotationPresent(SecureRequest.class);
    }

    private void verifyRequest() {
        VerifyGameRequest.Request request = new VerifyGameRequest.Request();
        request.body = getRequestBodyStream();
        request.signature = getRequestSignature();

        usecaseExecutor.execute(request);
    }

    private InputStream getRequestBodyStream() {
        try {
            return servletRequest.getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    private String getRequestSignature() {
        try {
            return uriInfo.getQueryParameters(true).get("signature").get(0);
        } catch (Throwable e) {
            return null;
        }
    }
}

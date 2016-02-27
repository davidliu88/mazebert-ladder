package com.mazebert.presenters.jaxrs;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mazebert.Logic;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.presenters.jaxrs.response.stream.MergeStatusWithResponse;
import com.mazebert.presenters.jaxrs.response.stream.PlainResponse;
import com.mazebert.presenters.jaxrs.response.stream.WrapStatusAndResponse;
import com.mazebert.usecases.security.SecureRequest;
import com.mazebert.usecases.security.VerifyGameSignature;
import org.jusecase.UsecaseExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractPresenter {

    UsecaseExecutor usecaseExecutor = Logic.instance;

    private final ObjectMapper objectMapper;

    @Context
    HttpServletRequest servletRequest;

    @Context
    UriInfo uriInfo;

    protected AbstractPresenter() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public <Request> Response execute(Request request) {
        if (isVerificationRequired(request)) {
            verifyRequest();
        }

        Object response = usecaseExecutor.execute(request);

        return Response
                .status(Response.Status.OK)
                .entity(createResponseStream(request, response))
                .build();
    }

    private StreamingOutput createResponseStream(Object request, Object response) {
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

    private boolean isVerificationRequired(Object request) {
        return request.getClass().isAnnotationPresent(SecureRequest.class);
    }

    private void verifyRequest() {
        VerifyGameSignature.Request request = new VerifyGameSignature.Request();
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

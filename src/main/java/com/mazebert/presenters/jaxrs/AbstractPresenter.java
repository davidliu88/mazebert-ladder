package com.mazebert.presenters.jaxrs;

import com.mazebert.Logic;
import com.mazebert.usecases.SecureRequest;
import com.mazebert.usecases.VerifyGameSignature;
import org.jusecase.UsecaseExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractPresenter implements UsecaseExecutor {

    UsecaseExecutor usecaseExecutor = Logic.instance;

    @Context
    HttpServletRequest servletRequest;

    @Context
    UriInfo uriInfo;

    public <Request, Response> Response execute(Request request) {
        if (request instanceof SecureRequest) {
            verifyRequest();
        }

        return usecaseExecutor.execute(request);
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

package com.mazebert.presenters.jaxrs.response;

import com.mazebert.error.Error;
import com.mazebert.error.InternalServerError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Currently this is not wired via @Provided annotation.
 * Due to legacy from original PHP implementation,
 * errors are mapped directly in @class{com.mazebert.presenters.jaxrs.AbstractPresenter}.
 */
public class ErrorExceptionMapper implements ExceptionMapper<Error> {
    @Override
    public Response toResponse(Error error) {
        if (error == null) {
            error = new InternalServerError("Received an error without a descriptive message!");
        }

        return Response
                .status(error.getStatusCode())
                .entity(createResponse(error))
                .build();
    }

    private ErrorResponse createResponse(Error error) {
        ErrorResponse content = new ErrorResponse();
        content.error = error.getMessage();
        content.status = "error";
        return content;
    }


}

package com.mazebert.presenters.jaxrs;

import com.mazebert.error.Error;
import com.mazebert.error.Type;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ErrorExceptionMapper implements ExceptionMapper<Error> {
    @Override
    public Response toResponse(Error error) {
        if (error == null) {
            error = new Error(Type.INTERNAL_SERVER_ERROR, "Received an error without a descriptive message!");
        }

        return Response
                .status(error.getType().getStatusCode())
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

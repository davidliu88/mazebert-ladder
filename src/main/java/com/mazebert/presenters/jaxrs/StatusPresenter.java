package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.GetStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/status")
public class StatusPresenter extends AbstractPresenter {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GetStatus.Response getStatus() {
        return execute(new GetStatus.Request());
    }

}

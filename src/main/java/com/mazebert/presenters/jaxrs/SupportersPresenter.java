package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.supporters.GetSupporters;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/supporters")
public class SupportersPresenter extends AbstractPresenter {
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupporters() {
        return execute(new GetSupporters.Request());
    }
}

package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.system.GetVersion;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/version")
public class VersionPresenter extends AbstractPresenter {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVersion() {
        return execute(new GetVersion.Request());
    }

}

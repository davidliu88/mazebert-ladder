package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.player.CreateAccount;
import com.mazebert.usecases.player.GetPlayers;
import com.mazebert.usecases.player.UpdateProgress;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/players")
public class PlayersPresenter extends AbstractPresenter {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayers(@QueryParam("start") int start, @QueryParam("limit") int limit) {
        GetPlayers.Request request = new GetPlayers.Request();
        request.start = start;
        request.limit = limit;
        return execute(request);
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(CreateAccount.Request request) {
        return execute(request);
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProgress(UpdateProgress.Request request) {
        return execute(request);
    }
}

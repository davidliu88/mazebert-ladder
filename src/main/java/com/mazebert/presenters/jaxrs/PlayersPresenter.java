package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.player.CreateAccount;
import com.mazebert.usecases.player.GetPlayers;
import com.mazebert.usecases.player.UpdateProgress;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/players")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlayersPresenter extends AbstractPresenter {

    @POST
    @Path("/new")
    public CreateAccount.Response createAccount(CreateAccount.Request request) {
        return execute(request);
    }

    @GET
    @Path("/")
    public GetPlayers.Response getPlayers(@QueryParam("start") int start, @QueryParam("limit") int limit) {
        GetPlayers.Request request = new GetPlayers.Request();
        request.start = start;
        request.limit = limit;
        return execute(request);
    }

    @POST
    @Path("/update")
    public UpdateProgress.Response createAccount(UpdateProgress.Request request) {
        return execute(request);
    }
}

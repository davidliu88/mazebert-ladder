package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.CreateAccount;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/players")
public class PlayersPresenter extends AbstractPresenter {

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CreateAccount.Response createAccount(CreateAccount.Request request) {
        return execute(request);
    }
}

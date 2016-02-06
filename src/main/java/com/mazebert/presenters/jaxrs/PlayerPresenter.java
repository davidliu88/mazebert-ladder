package com.mazebert.presenters.jaxrs;

import com.mazebert.entities.Player;
import com.mazebert.usecases.player.GetPlayer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/player")
public class PlayerPresenter extends AbstractPresenter {
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Player getPlayer(@QueryParam("key") String key) {
        GetPlayer.Request request = new GetPlayer.Request();
        request.key = key;
        return execute(request);
    }
}

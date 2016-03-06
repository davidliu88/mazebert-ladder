package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.player.TradeDuplicateCards;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/trade")
public class TradePresenter extends AbstractPresenter {
    @POST
    @Path("/duplicates")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response tradeDuplicates(TradeDuplicateCards.Request request) {
        return execute(request);
    }
}

package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.blackmarket.BuyBlackMarketOffer;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/black-market")
public class BlackMarketPresenter extends AbstractPresenter {
    @POST
    @Path("/buy-offer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buyOffer(BuyBlackMarketOffer.Request request) {
        return execute(request);
    }
}

package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.shop.PrepareShopTransaction;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/shop")
public class ShopPresenter extends AbstractPresenter {
    @POST
    @Path("/prepare-transaction")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response prepareTransaction(PrepareShopTransaction.Request request) {
        return execute(request);
    }
}

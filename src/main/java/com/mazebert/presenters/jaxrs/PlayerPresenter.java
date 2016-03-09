package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.player.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/player")
public class PlayerPresenter extends AbstractPresenter {
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayer(@QueryParam("key") String key) {
        GetPlayer.Request request = new GetPlayer.Request();
        request.key = key;
        return execute(request);
    }

    @GET
    @Path("/synchronize")
    @Produces(MediaType.APPLICATION_JSON)
    public Response synchronize(
            @QueryParam("key") String key,
            @QueryParam("appVersion") String appVersion,
            @QueryParam("timezoneOffset") int timeZoneOffset) {
        SynchronizePlayer.Request request = new SynchronizePlayer.Request();
        request.key = key;
        request.appVersion = appVersion;
        request.timeZoneOffset = timeZoneOffset;

        return execute(request);
    }

    @POST
    @Path("/forgot-savecode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response forgotSavecode(ForgotSavecode.Request request) {
        return execute(request);
    }

    @POST
    @Path("/register-email")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerEmail(RegisterEmail.Request request) {
        return execute(request);
    }

    @GET
    @Path("/profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@QueryParam("id") long id) {
        GetPlayerProfile.Request request = new GetPlayerProfile.Request();
        request.id = id;
        return execute(request);
    }

    @POST
    @Path("/forge-card")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response forgeCard(ForgeCard.Request request) {
        return execute(request);
    }
}

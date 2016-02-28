package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bonus")
public class BonusTimePresenter extends AbstractPresenter {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBonusTimes(
            @QueryParam("appVersion") String appVersion,
            @QueryParam("mapId") int mapId,
            @QueryParam("difficultyType") String difficultyType,
            @QueryParam("waveAmountType") String waveAmountType,
            @QueryParam("start") int start,
            @QueryParam("limit") int limit) {

        GetBonusTimes.Request request = new GetBonusTimes.Request();
        request.appVersion = appVersion;
        request.mapId = mapId;
        request.difficultyType = difficultyType;
        request.waveAmountType = waveAmountType;
        request.start = start;
        request.limit = limit;

        return execute(request);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public Response updateBonusTime(UpdateBonusTime.Request request) {
        return execute(request);
    }
}

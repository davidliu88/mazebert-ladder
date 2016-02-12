package com.mazebert.presenters.jaxrs;

import com.mazebert.entities.Player;
import com.mazebert.usecases.player.GetPlayer;
import com.mazebert.usecases.player.SynchronizePlayer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

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

    @GET
    @Path("/synchronize")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> synchronize(
            @QueryParam("key") String key,
            @QueryParam("appVersion") String appVersion,
            @QueryParam("timezoneOffset") int timeZoneOffset) {
        SynchronizePlayer.Request request = new SynchronizePlayer.Request();
        request.key = key;
        request.appVersion = appVersion;
        request.timeZoneOffset = timeZoneOffset;

        SynchronizePlayer.Response response = execute(request);
        Map<String, Object> result = new HashMap<>();
        result.put("player", response);

        return result;
    }
}

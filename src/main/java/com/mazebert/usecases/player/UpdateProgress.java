package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.plugins.time.CurrentDatePlugin;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.VerifyRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UpdateProgress implements Usecase<UpdateProgress.Request, UpdateProgress.Response> {
    private final PlayerGateway playerGateway;
    private final CurrentDatePlugin currentDatePlugin;

    @Inject
    public UpdateProgress(PlayerGateway playerGateway, CurrentDatePlugin currentDatePlugin) {
        this.playerGateway = playerGateway;
        this.currentDatePlugin = currentDatePlugin;
    }

    public Response execute(Request request) {
        if (request.key == null) {
            throw new BadRequest("Player key must not be null.");
        }

        Player player = playerGateway.findPlayerByKey(request.key);
        if (player == null) {
            throw new NotFound("A player with this key does not exist.");
        }

        player.setLevel(Math.max(player.getLevel(), request.level));
        player.setExperience(Math.max(player.getExperience(), request.experience));
        player.setLastUpdate(currentDatePlugin.getCurrentDate());
        playerGateway.updatePlayer(player);

        return new Response();
    }

    @VerifyRequest
    @StatusResponse
    public static class Request {
        public String key;
        public int level;
        public long experience;

        /**
         * This property is still sent by old game clients but ignored on server side.
         * Will be removed in a future version.
         */
        @Deprecated
        public String name;
    }

    public static class Response {
    }
}

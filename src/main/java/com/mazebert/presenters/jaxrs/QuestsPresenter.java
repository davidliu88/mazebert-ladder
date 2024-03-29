package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.quest.CompleteQuests;
import com.mazebert.usecases.quest.ReplaceQuest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/quests")
public class QuestsPresenter extends AbstractPresenter {
    @POST
    @Path("/complete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeQuests(CompleteQuests.Request request) {
        return execute(request);
    }

    @POST
    @Path("/replace")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response replaceQuest(ReplaceQuest.Request request) {
        return execute(request);
    }
}

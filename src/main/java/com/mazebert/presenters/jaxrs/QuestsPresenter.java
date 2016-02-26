package com.mazebert.presenters.jaxrs;

import com.mazebert.usecases.quest.CompleteQuests;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/quests")
public class QuestsPresenter extends AbstractPresenter {
    @POST
    @Path("/complete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CompleteQuests.Response createAccount(CompleteQuests.Request request) {
        return execute(request);
    }
}

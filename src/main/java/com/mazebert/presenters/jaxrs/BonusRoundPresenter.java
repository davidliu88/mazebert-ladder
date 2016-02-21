package com.mazebert.presenters.jaxrs;

import com.mazebert.entities.PlayerBonusTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/bonus")
public class BonusRoundPresenter extends AbstractPresenter {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlayerBonusTime> getBonusTimes() {
        return null;
    }
}

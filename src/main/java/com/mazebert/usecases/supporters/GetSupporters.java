package com.mazebert.usecases.supporters;

import com.mazebert.entities.Supporter;
import com.mazebert.gateways.SupporterGateway;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GetSupporters implements Usecase<GetSupporters.Request, List<Supporter>> {
    private final SupporterGateway supporterGateway;

    @Inject
    public GetSupporters(SupporterGateway supporterGateway) {
        this.supporterGateway = supporterGateway;
    }

    public List<Supporter> execute(Request request) {
        return supporterGateway.findAllSupporters();
    }

    @StatusResponse(field = "players")
    public static class Request {
    }
}

package com.mazebert.usecases.supporters;

import com.mazebert.entities.Supporter;
import com.mazebert.gateways.SupporterGateway;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GetSupporters implements Usecase<GetSupporters.Request, GetSupporters.Response> {
    private final SupporterGateway supporterGateway;

    @Inject
    public GetSupporters(SupporterGateway supporterGateway) {
        this.supporterGateway = supporterGateway;
    }

    public Response execute(Request request) {
        Response response = new Response();
        response.players = supporterGateway.findAllSupporters();
        return response;
    }

    @StatusResponse
    public static class Request {
    }

    public static class Response {
        public List<Supporter> players;
    }
}

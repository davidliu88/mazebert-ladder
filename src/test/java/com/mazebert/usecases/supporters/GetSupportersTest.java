package com.mazebert.usecases.supporters;

import com.mazebert.entities.Supporter;
import com.mazebert.gateways.mocks.SupporterGatewayMock;
import com.mazebert.usecases.supporters.GetSupporters.Request;
import com.mazebert.usecases.supporters.GetSupporters.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;

public class GetSupportersTest extends UsecaseTest<Request, Response> {
    private SupporterGatewayMock supporterGateway = new SupporterGatewayMock();

    @Before
    public void setUp() {
        usecase = new GetSupporters(supporterGateway);
    }

    @Test
    public void gatewayIsAskedForSupporters() {
        List<Supporter> expected = a(list());
        supporterGateway.givenSupporters(expected);
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals(expected, response.players);
    }

    private RequestBuilder request() {
        return new RequestBuilder();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        @Override
        public Request build() {
            return request;
        }
    }
}
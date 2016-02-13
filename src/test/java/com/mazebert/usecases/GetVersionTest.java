package com.mazebert.usecases;

import com.mazebert.usecases.GetVersion.Request;
import com.mazebert.usecases.GetVersion.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;

public class GetVersionTest extends UsecaseTest<Request, Response> {

    @Before
    public void setUp() {
        usecase = new GetVersion();
    }

    @Test
    public void todo() {
        givenRequest(a(request()));
        whenRequestIsExecuted();
        assertEquals("todo", response.version);
    }

    private RequestBuilder request() {
        return new RequestBuilder().golden();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        public RequestBuilder golden() {
            return this;
        }

        public Request build() {
            return request;
        }
    }
}
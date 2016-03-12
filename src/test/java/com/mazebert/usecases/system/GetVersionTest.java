package com.mazebert.usecases.system;

import com.mazebert.entities.Version;
import com.mazebert.usecases.system.GetVersion.Request;
import com.mazebert.usecases.system.GetVersion.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static org.junit.Assert.assertTrue;
import static org.jusecase.Builders.a;

public class GetVersionTest extends UsecaseTest<Request, Response> {

    @Before
    public void setUp() {
        usecase = new GetVersion();
    }

    @Test
    public void versionIsTakenFromPropertiesFile() {
        givenRequest(a(request()));
        whenRequestIsExecuted();

        Version version = new Version(response.version);
        assertTrue(version.getMajor() > 0);
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
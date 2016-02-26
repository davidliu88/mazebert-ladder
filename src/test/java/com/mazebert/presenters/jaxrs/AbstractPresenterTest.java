package com.mazebert.presenters.jaxrs;

import com.mazebert.error.Error;
import com.mazebert.error.Unauthorized;
import com.mazebert.usecases.security.SecureRequest;
import com.mazebert.usecases.security.VerifyGameSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jusecase.UsecaseExecutor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractPresenterTest {
    private AbstractPresenter presenter;

    @Mock private HttpServletRequest servletRequest;
    @Mock private UriInfo uriInfo;

    private List<Object> sentRequests = new ArrayList<>();

    @SecureRequest
    private class DummySecureRequest {
    }

    @Before
    public void setUp() {
        presenter = new AbstractPresenter() {};
        presenter.servletRequest = servletRequest;
        presenter.uriInfo = uriInfo;
        presenter.usecaseExecutor = new UsecaseExecutor() {
            public <Request, Response> Response execute(Request request) {
                sentRequests.add(request);
                return null;
            }
        };
    }

    @Test
    public void secureRequest_bodyIsPassedForClientValidation() throws Exception {
        ServletInputStream body = mock(ServletInputStream.class);
        when(servletRequest.getInputStream()).thenReturn(body);

        whenSecureRequestIsExecuted();

        thenVerificationBodyIs(body);
    }

    @Test
    public void secureRequest_nullSignature() throws Exception {
        when(uriInfo.getQueryParameters(anyBoolean())).thenReturn(null);

        whenSecureRequestIsExecuted();

        thenVerificationSignatureIs(null);
    }

    @Test
    public void secureRequest_missingSignature() throws Exception {
        when(uriInfo.getQueryParameters(anyBoolean())).thenReturn(new MultivaluedHashMap<>());

        whenSecureRequestIsExecuted();

        thenVerificationSignatureIs(null);
    }

    @Test
    public void secureRequest_signatureIsPassedForClientVerification() throws Exception {
        MultivaluedHashMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.put("signature", a(list("hash")));
        when(uriInfo.getQueryParameters(anyBoolean())).thenReturn(parameters);

        whenSecureRequestIsExecuted();

        thenVerificationSignatureIs("hash");
    }

    @Test
    public void secureRequest_clientSignatureCorrect_actualRequestIsCalled() throws Exception {
        whenSecureRequestIsExecuted();
        assertEquals(2, sentRequests.size());
        assertTrue(sentRequests.get(1) instanceof DummySecureRequest);
    }

    @Test
    public void secureRequest_clientSignatureIncorrect_actualRequestIsNotCalled() throws Exception {
        presenter.usecaseExecutor = new UsecaseExecutor() {
            public <Request, Response> Response execute(Request request) {
                throw new Unauthorized("Invalid client signature.");
            }
        };

        try {
            whenSecureRequestIsExecuted();
        } catch (Error error) {
            assertEquals(Unauthorized.class, error.getClass());
            assertEquals("Invalid client signature.", error.getMessage());
        }
    }

    private void whenSecureRequestIsExecuted() {
        presenter.execute(new DummySecureRequest());
    }

    private void thenVerificationBodyIs(ServletInputStream expected) {
        VerifyGameSignature.Request request = (VerifyGameSignature.Request)sentRequests.get(0);
        assertEquals(expected, request.body);
    }

    private void thenVerificationSignatureIs(String expected) {
        VerifyGameSignature.Request request = (VerifyGameSignature.Request)sentRequests.get(0);
        assertEquals(expected, request.signature);
    }
}
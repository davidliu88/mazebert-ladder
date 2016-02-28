package com.mazebert.presenters.jaxrs;

import com.mazebert.error.Error;
import com.mazebert.error.Unauthorized;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;
import com.mazebert.usecases.player.CreateAccount;
import com.mazebert.usecases.player.GetPlayer;
import com.mazebert.usecases.security.SecureRequest;
import com.mazebert.usecases.security.VerifyGameSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jusecase.UsecaseExecutor;
import org.jusecase.executors.manual.Factory;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mazebert.builders.BuilderFactory.player;
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
    private Object usecaseRequest;
    private Object usecaseResponse;
    private Response presenterResponse;

    @SecureRequest
    private class DummySecureRequest {
    }

    @Before
    public void setUp() {
        givenPresenter(() -> new UsecaseExecutor() {
            @SuppressWarnings("unchecked")
            public <RequestType, ResponseType> ResponseType execute(RequestType request) {
                sentRequests.add(request);
                return (ResponseType) usecaseResponse;
            }
        });
    }

    private void givenPresenter(final Factory<UsecaseExecutor> usecaseExecutorFactory) {
        presenter = new AbstractPresenter() {
            @Override
            protected UsecaseExecutor getUsecaseExecutor() {
                return usecaseExecutorFactory.create();
            }
        };
        presenter.servletRequest = servletRequest;
        presenter.uriInfo = uriInfo;
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
        givenPresenter(() -> new UsecaseExecutor() {
            public <RequestType, ResponseType> ResponseType execute(RequestType request) {
                throw new Unauthorized("Invalid client signature.");
            }
        });

        try {
            whenSecureRequestIsExecuted();
        } catch (Error error) {
            assertEquals(Unauthorized.class, error.getClass());
            assertEquals("Invalid client signature.", error.getMessage());
        }
    }

    @Test
    public void responseStatus() throws Exception {
        givenUsecaseRequest(new CreateAccount.Request());
        whenRequestIsExecuted();
        assertEquals(200, presenterResponse.getStatus());
    }

    @Test
    public void responseWithMergedStatus() {
        givenUsecaseRequest(new CreateAccount.Request());
        givenUsecaseResponse(new CreateAccount.Response());
        whenRequestIsExecuted();
        thenResponseJsonIs("{\"status\":\"ok\",\"id\":0,\"key\":null}");
    }

    @Test
    public void responseWithMergedStatus_responseIsNull() {
        givenUsecaseRequest(new CreateAccount.Request());
        givenUsecaseResponse(null);
        whenRequestIsExecuted();
        thenResponseJsonIs("{\"status\":\"ok\"}");
    }

    @Test
    public void responseWithMergedStatus_responseIsEmpty() {
        givenUsecaseRequest(new UpdateBonusTime.Request());
        givenUsecaseResponse(new UpdateBonusTime.Response());
        whenRequestIsExecuted();
        thenResponseJsonIs("{\"status\":\"ok\"}");
    }

    @Test
    public void responseContainingStatusAndUsecaseResponse() {
        givenUsecaseRequest(new GetPlayer.Request());
        givenUsecaseResponse(a(player().casid()));
        whenRequestIsExecuted();
        thenResponseJsonStartsWith("{\"status\":\"ok\",\"player\":{");
    }

    @Test
    public void responseWithoutStatusAddition() {
        givenUsecaseRequest(new GetBonusTimes.Request());
        givenUsecaseResponse(a(list()));
        whenRequestIsExecuted();
        thenResponseJsonIs("[]");
    }

    private void givenUsecaseRequest(Object usecaseRequest) {
        this.usecaseRequest = usecaseRequest;
    }

    private void givenUsecaseResponse(Object response) {
        this.usecaseResponse = response;
    }

    private void whenSecureRequestIsExecuted() {
        givenUsecaseRequest(new DummySecureRequest());
        whenRequestIsExecuted();
    }

    private void whenRequestIsExecuted() {
        presenterResponse = presenter.execute(usecaseRequest);
    }

    private void thenVerificationBodyIs(ServletInputStream expected) {
        VerifyGameSignature.Request request = (VerifyGameSignature.Request)sentRequests.get(0);
        assertEquals(expected, request.body);
    }

    private void thenVerificationSignatureIs(String expected) {
        VerifyGameSignature.Request request = (VerifyGameSignature.Request)sentRequests.get(0);
        assertEquals(expected, request.signature);
    }

    private void thenResponseJsonIs(String expected) {
        assertEquals(expected, getResponseJson());
    }

    private void thenResponseJsonStartsWith(String expected) {
        String json = getResponseJson().substring(0, expected.length());
        assertEquals(expected, json);
    }

    private String getResponseJson() {
        StreamingOutput output = (StreamingOutput)presenterResponse.getEntity();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            output.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to test output stream.", e);
        }

        return outputStream.toString();
    }
}
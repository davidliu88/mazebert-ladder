package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.plugins.message.EmailMessage;
import com.mazebert.plugins.message.mocks.EmailMessagePluginMock;
import com.mazebert.usecases.player.ForgotSavecode.Request;
import com.mazebert.usecases.player.ForgotSavecode.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.jusecase.builders.BuilderFactory.a;

public class ForgotSavecodeTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private EmailMessagePluginMock emailMessagePlugin = new EmailMessagePluginMock();

    @Before
    public void setUp() {
        usecase = new ForgotSavecode(playerGateway, emailMessagePlugin);
    }

    @Test
    public void nullEmail() {
        givenRequest(a(request().withEmail(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Email must not be null"));
    }

    @Test
    public void emptyEmail() {
        givenRequest(a(request().withEmail("")));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Email must not be empty"));
    }

    @Test
    public void unknownEmail() {
        givenRequest(a(request().withEmail("player@mazebert.com")));
        playerGateway.givenPlayerExists(a(player().casid().withEmail("mr.x@mazebert.com")));

        whenRequestIsExecuted();

        thenNoEmailIsSent();
    }

    @Test
    public void knownEmail() {
        givenRequest(a(request().withEmail("player@mazebert.com")));
        Player player = a(player().casid().withEmail("player@mazebert.com"));
        playerGateway.givenPlayerExists(player);

        whenRequestIsExecuted();

        thenEmailIsSentToPlayer(player);
    }

    private void thenNoEmailIsSent() {
        assertFalse(response.emailSent);
    }

    private void thenEmailIsSentToPlayer(Player player) {
        assertTrue(response.emailSent);

        EmailMessage message = emailMessagePlugin.getSentMessage(0);
        assertEquals(player.getEmail(), message.getReceiver());
        assertEquals("Your Mazebert TD savecode", message.getSubject());
        assertEquals("Dear " + player.getName() + ",\n\nyour savecode is " + player.getKey() + "\n\nHappy building!\nYour Mazebert TD Team", message.getMessage());
    }

    private RequestBuilder request() {
        return new RequestBuilder().golden();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        @Override
        public Request build() {
            return request;
        }

        public RequestBuilder golden() {
            return this;
        }

        public RequestBuilder withEmail(String value) {
            request.email = value;
            return this;
        }
    }
}
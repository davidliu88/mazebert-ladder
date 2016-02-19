package com.mazebert.usecases.player;

import com.mazebert.builders.PlayerBuilder;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.plugins.message.EmailMessage;
import com.mazebert.plugins.message.mocks.EmailMessagePluginMock;
import com.mazebert.usecases.player.RegisterEmail.Request;
import com.mazebert.usecases.player.RegisterEmail.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.jusecase.builders.BuilderFactory.a;

public class RegisterEmailTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private EmailMessagePluginMock emailMessagePlugin = new EmailMessagePluginMock();

    @Before
    public void setUp() {
        usecase = new RegisterEmail(playerGateway, emailMessagePlugin);
        playerGateway.givenPlayerExists(a(goldenPlayer()));
    }

    @Test
    public void nullEmail() {
        givenRequest(a(request().withEmail(null)));
        whenRequestIsExecuted();
        thenErrorIsInvalidEmail();
    }

    @Test
    public void invalidEmail() {
        givenRequest(a(request().withEmail("notanemail")));
        whenRequestIsExecuted();
        thenErrorIsInvalidEmail();
    }

    @Test
    public void validEmail() {
        givenRequest(a(request().withEmail("valid.email@mazebert.com")));
        whenRequestIsExecuted();
        thenResponseIsOk();
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Key must not be null."));
    }

    @Test
    public void playerNotFound() {
        playerGateway.givenPlayerExists(a(goldenPlayer().withKey("abc")));
        givenRequest(a(request().withKey("123")));
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("Player does not exist."));
    }

    @Test
    public void emailAlreadyTakenByAnotherPlayer() {
        playerGateway.givenPlayerExists(a(goldenPlayer()
                .withId(124014)
                .withKey("2rrjew")
                .withEmail("occupied@mazebert.com")));
        givenRequest(a(request().withEmail("occupied@mazebert.com")));
        whenRequestIsExecuted();
        thenResponseIsEmailAlreadyTaken();
    }

    @Test
    public void emailAlreadyTakenBySamePlayer() {
        playerGateway.givenPlayerExists(a(goldenPlayer()
                .withEmail("occupied@mazebert.com")));
        givenRequest(a(request().withEmail("occupied@mazebert.com")));
        whenRequestIsExecuted();
        thenResponseIsOk();
    }

    @Test
    public void emailIsUpdated() {
        givenRequest(a(request().withEmail("brand.new@mazebert.com")));
        whenRequestIsExecuted();
        assertEquals("brand.new@mazebert.com", playerGateway.getUpdatedPlayer().getEmail());
    }

    @Test
    public void mailIsSentToPlayer() {
        givenRequest(a(request().withEmail("brand.new@mazebert.com")));
        whenRequestIsExecuted();

        EmailMessage sentMessage = emailMessagePlugin.getSentMessage(0);
        assertEquals("brand.new@mazebert.com", sentMessage.getReceiver());
        assertEquals("Your Mazebert TD savecode", sentMessage.getSubject());
        assertEquals("Dear casid,\n\nthanks for adding an e-mail address to your ladder profile!\n\nYour savecode is abcdef.\n\nHappy building!\nYour Mazebert TD Team", sentMessage.getMessage());
    }

    private void thenResponseIsOk() {
        assertEquals("OK", response.registrationStatus);
    }

    private void thenResponseIsEmailAlreadyTaken() {
        assertEquals("EMAIL_ALREADY_EXISTS", response.registrationStatus);
        assertNull(playerGateway.getUpdatedPlayer());
    }

    private void thenErrorIsInvalidEmail() {
        thenErrorIs(new BadRequest("Please enter a valid email address."));
    }

    private PlayerBuilder goldenPlayer() {
        return player().casid();
    }

    private RequestBuilder request() {
        return new RequestBuilder()
                .withEmail("player@mazebert.com")
                .withKey(a(goldenPlayer()).getKey());
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        public Request build() {
            return request;
        }

        public RequestBuilder withEmail(String email) {
            request.email = email;
            return this;
        }

        public RequestBuilder withKey(String key) {
            request.key = key;
            return this;
        }
    }
}
package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.plugins.message.EmailMessage;
import com.mazebert.plugins.message.EmailMessagePlugin;
import com.mazebert.plugins.validation.VersionValidator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.VerifyRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ForgotSavecode implements Usecase<ForgotSavecode.Request, ForgotSavecode.Response> {
    private final VersionValidator versionValidator = new VersionValidator("1.0.0");
    private final PlayerGateway playerGateway;
    private final EmailMessagePlugin emailMessagePlugin;

    @Inject
    public ForgotSavecode(PlayerGateway playerGateway, EmailMessagePlugin emailMessagePlugin) {
        this.playerGateway = playerGateway;
        this.emailMessagePlugin = emailMessagePlugin;
    }

    public Response execute(Request request) {
        verifyRequest(request);

        Response response = new Response();
        Player player = playerGateway.findPlayerByEmail(request.email);
        if (player != null) {
            sendEmailToPlayer(player);
            response.emailSent = true;
        }

        return response;
    }

    private void sendEmailToPlayer(Player player) {
        EmailMessage message = new EmailMessage();
        message.setReceiver(player.getEmail());
        message.setSubject("Your Mazebert TD savecode");
        message.setMessage("Dear " + player.getName() + ",\n\nyour savecode is " + player.getKey() + "\n\nHappy building!\nYour Mazebert TD Team");
        emailMessagePlugin.sendEmail(message);
    }

    private void verifyRequest(Request request) {
        if (request.email == null) {
            throw new BadRequest("Email must not be null");
        }

        if (request.email.isEmpty()) {
            throw new BadRequest("Email must not be empty");
        }

        versionValidator.validate(request.appVersion);
    }

    @VerifyRequest
    @StatusResponse
    public static class Request {
        public String appVersion;
        public String email;
    }

    public static class Response {
        public boolean emailSent;
    }
}

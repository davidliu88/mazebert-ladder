package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.plugins.message.EmailMessage;
import com.mazebert.plugins.message.EmailMessagePlugin;
import com.mazebert.plugins.validation.EmailValidator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.SecureRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;

public class RegisterEmail implements Usecase<RegisterEmail.Request, RegisterEmail.Response> {
    private final PlayerGateway playerGateway;
    private final EmailMessagePlugin emailMessagePlugin;
    private final EmailValidator emailValidator;

    @Inject
    public RegisterEmail(PlayerGateway playerGateway, EmailMessagePlugin emailMessagePlugin) {
        this.playerGateway = playerGateway;
        this.emailMessagePlugin = emailMessagePlugin;
        emailValidator = new EmailValidator();
    }

    public Response execute(Request request) {
        validateRequest(request);

        Player player = findPlayer(request);
        if (isEmailAlreadyTaken(player, request.email)) {
            return alreadyTaken();
        }

        updateEmail(request, player);
        return success();
    }

    private void updateEmail(Request request, Player player) {
        player.setEmail(request.email);
        playerGateway.updatePlayer(player);

        EmailMessage message = new EmailMessage();
        message.setReceiver(request.email);
        message.setSubject("Your Mazebert TD savecode");
        message.setMessage("Dear " + player.getName() + ",\n\nthanks for adding an e-mail address to your ladder profile!\n\nYour savecode is " + player.getKey() + ".\n\nHappy building!\nYour Mazebert TD Team");
        emailMessagePlugin.sendEmail(message);
    }

    private Player findPlayer(Request request) {
        Player player = playerGateway.findPlayerByKey(request.key);
        if (player == null) {
            throw new NotFound("Player does not exist.");
        }
        return player;
    }

    private boolean isEmailAlreadyTaken(Player player, String newEmail) {
        Player playerWithEmail = playerGateway.findPlayerByEmail(newEmail);
        if (playerWithEmail == null) {
            return false;
        }

        return player.getId() != playerWithEmail.getId();
    }

    private void validateRequest(Request request) {
        if (!emailValidator.isValid(request.email)) {
            throw new BadRequest("Please enter a valid email address.");
        }

        if (request.key == null) {
            throw new BadRequest("Key must not be null.");
        }
    }

    private Response success() {
        Response response = new Response();
        response.registrationStatus = "OK";
        return response;
    }

    private Response alreadyTaken() {
        Response response = new Response();
        response.registrationStatus = "EMAIL_ALREADY_EXISTS";
        return response;
    }

    @SecureRequest
    @StatusResponse
    public static class Request {
        public String email;
        public String key;
    }

    public static class Response {
        public String registrationStatus;
    }
}

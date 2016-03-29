package com.mazebert.usecases.shop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazebert.entities.CardType;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.entities.Purchase;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.PurchaseGateway;
import com.mazebert.gateways.error.KeyAlreadyExists;
import com.mazebert.plugins.message.EmailMessage;
import com.mazebert.plugins.message.EmailMessagePlugin;
import com.mazebert.plugins.security.GooglePlayPurchaseVerifier;
import com.mazebert.plugins.time.CurrentDatePlugin;
import com.mazebert.plugins.validation.VersionValidator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.SignResponse;
import com.mazebert.usecases.security.VerifyRequest;
import com.mazebert.usecases.shop.CommitShopTransactions.Request.Transaction;
import org.jusecase.Usecase;
import org.jusecase.transaction.TransactionRunner;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CommitShopTransactions implements Usecase<CommitShopTransactions.Request, CommitShopTransactions.Response> {
    private final VersionValidator versionValidator = new VersionValidator("1.0.0");
    private final PlayerGateway playerGateway;
    private final FoilCardGateway foilCardGateway;
    private final PurchaseGateway purchaseGateway;
    private final GooglePlayPurchaseVerifier googlePlayPurchaseVerifier;
    private final TransactionRunner transactionRunner;
    private final CurrentDatePlugin currentDatePlugin;
    private final EmailMessagePlugin emailMessagePlugin;
    private final Logger logger;

    @Inject
    public CommitShopTransactions(PlayerGateway playerGateway, FoilCardGateway foilCardGateway, PurchaseGateway purchaseGateway,
                                  GooglePlayPurchaseVerifier googlePlayPurchaseVerifier, TransactionRunner transactionRunner,
                                  CurrentDatePlugin currentDatePlugin, EmailMessagePlugin emailMessagePlugin, Logger logger) {
        this.playerGateway = playerGateway;
        this.foilCardGateway = foilCardGateway;
        this.purchaseGateway = purchaseGateway;
        this.googlePlayPurchaseVerifier = googlePlayPurchaseVerifier;
        this.transactionRunner = transactionRunner;
        this.currentDatePlugin = currentDatePlugin;
        this.emailMessagePlugin = emailMessagePlugin;
        this.logger = logger;
    }

    @Override
    public Response execute(Request request) {
        validateRequest(request);
        Player player = playerGateway.findPlayerByKey(request.key);
        if (player == null) {
            throw new NotFound("Player does not exist.");
        }

        if ("GooglePlay".equals(request.store)) {
            return createGooglePlayResponse(request, player);
        }

        throw new NotFound("Store does not exist.");
    }

    private Response createGooglePlayResponse(Request request, Player player) {
        Response response = new Response();
        response.verifiedProductIds = commitGooglePlayTransactions(request, player);
        return response;
    }

    private List<String> commitGooglePlayTransactions(Request request, Player player) {
        List<String> verifiedProductIds = new ArrayList<>();

        for (Transaction transaction : request.transactions) {
            if (isGooglePlayTransactionValid(transaction)) {
                try {
                    transactionRunner.runAsTransaction(() -> {
                        storePurchase(request, player, transaction);
                        unlockPurchaseReward(player, transaction);
                        verifiedProductIds.add(transaction.productId);
                    });
                } catch (KeyAlreadyExists error) {
                    // It is not possible to purchase a product twice.
                }
            }
        }

        if (verifiedProductIds.size() > 0) {
            sendMailToDeveloper(player, verifiedProductIds);
        }

        return verifiedProductIds;
    }

    private void sendMailToDeveloper(Player player, List<String> verifiedProductIds) {
        EmailMessage message = new EmailMessage();
        message.setReceiver("andy@mazebert.com");
        message.setSubject("New donation!");
        message.setMessage(createMailContent(player, verifiedProductIds));

        try {
            emailMessagePlugin.sendEmail(message);
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private String createMailContent(Player player, List<String> verifiedProductIds) {
        String content = "Yay!\n\nthe player " + player.getName() + " donated a ";
        for (int i = 0; i < verifiedProductIds.size(); ++i) {
            String product = verifiedProductIds.get(i);
            product = product.substring(product.lastIndexOf(".") + 1);
            if (i > 0) {
                if (i == verifiedProductIds.size() - 1) {
                    content += " and ";
                } else {
                    content += ", ";
                }
            }
            content += product;
        }
        content += " :-)\n\nCheers!";
        return content;
    }

    private void storePurchase(Request request, Player player, Transaction transaction) {
        Purchase purchase = new Purchase();
        purchase.setPlayerId(player.getId());
        purchase.setProductId(transaction.productId);
        purchase.setStore(request.store);
        purchase.setData(transaction.data);
        purchase.setSignature(transaction.signature);
        purchase.setPlayerId(player.getId());
        purchase.setAppVersion(request.appVersion);
        purchase.setPurchaseDate(currentDatePlugin.getCurrentDate());
        purchaseGateway.addPurchase(purchase);
    }

    private void unlockPurchaseReward(Player player, Transaction transaction) {
        if ("com.mazebert.cookie".equals(transaction.productId)) {
            foilCardGateway.addFoilCardToPlayer(player.getId(), getCookieReward());
            addSupporterLevel(player, 1);
        } else if ("com.mazebert.beer".equals(transaction.productId)) {
            foilCardGateway.addFoilCardToPlayer(player.getId(), getBeerReward());
            addSupporterLevel(player, 2);
        } else if ("com.mazebert.whisky".equals(transaction.productId)) {
            foilCardGateway.addFoilCardToPlayer(player.getId(), getWhiskyReward());
            addSupporterLevel(player, 4);
        }
    }

    private void addSupporterLevel(Player player, int supporterLevel) {
        player.setSupporterLevel(player.getSupporterLevel() + supporterLevel);
        playerGateway.updatePlayer(player);
    }

    private FoilCard getCookieReward() {
        FoilCard cookieMonster = new FoilCard();
        cookieMonster.setCardId(5);
        cookieMonster.setCardType(CardType.HERO);
        cookieMonster.setAmount(1);
        return cookieMonster;
    }

    private FoilCard getBeerReward() {
        FoilCard innKeeper = new FoilCard();
        innKeeper.setCardId(6);
        innKeeper.setCardType(CardType.HERO);
        innKeeper.setAmount(1);
        return innKeeper;
    }

    private FoilCard getWhiskyReward() {
        FoilCard innKeeper = new FoilCard();
        innKeeper.setCardId(19);
        innKeeper.setCardType(CardType.POTION);
        innKeeper.setAmount(1);
        return innKeeper;
    }

    private boolean isGooglePlayTransactionValid(Transaction transaction) {
        return transaction.productId != null &&
                isGooglePlayTransactionSignatureValid(transaction) &&
                isGooglePlayTransactionPayloadValid(transaction);
    }

    private boolean isGooglePlayTransactionSignatureValid(Transaction transaction) {
        return googlePlayPurchaseVerifier.isSignatureValid(transaction.data, transaction.signature);
    }

    @SuppressWarnings("unchecked")
    private boolean isGooglePlayTransactionPayloadValid(Transaction transaction) {
        try {
            // We *could* additionally use the player id + product id payload at this point.
            // However, this will screw over players who bought items and lost their game key!
            Map<String, Object> payload = new ObjectMapper().getFactory().createParser(transaction.data.getBytes()).readValueAs(HashMap.class);
            return "air.com.mazebert.MazebertTD".equals(payload.get("packageName")) &&
                    transaction.productId.equals(payload.get("productId"));
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Failed to parse Google Play transaction payload.");
            logger.log(Level.SEVERE, "Exception message was: " + e.getMessage());
            return false;
        }
    }

    private void validateRequest(Request request) {
        versionValidator.validate(request.appVersion);
        if (request.key == null) {
            throw new BadRequest("Key must not be null.");
        }
        if (request.store == null) {
            throw new BadRequest("Store must not be null.");
        }
        if (request.transactions == null) {
            throw new BadRequest("Transactions must not be null.");
        }
    }

    @VerifyRequest
    @StatusResponse
    @SignResponse
    public static class Request {
        public String appVersion;
        public String key;
        public String store;
        public List<Transaction> transactions;

        public static class Transaction {
            public String productId;
            public String data;
            public String signature;
        }
    }

    public static class Response {
        public List<String> verifiedProductIds;
    }
}

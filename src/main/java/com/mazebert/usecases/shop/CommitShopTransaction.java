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
import com.mazebert.plugins.security.GooglePlayPurchaseVerifier;
import com.mazebert.plugins.validation.VersionValidator;
import com.mazebert.usecases.shop.CommitShopTransaction.Request.Transaction;
import org.jusecase.Usecase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommitShopTransaction implements Usecase<CommitShopTransaction.Request, CommitShopTransaction.Response> {
    private final VersionValidator versionValidator = new VersionValidator("1.0.0");
    private final PlayerGateway playerGateway;
    private final FoilCardGateway foilCardGateway;
    private final PurchaseGateway purchaseGateway;
    private final GooglePlayPurchaseVerifier googlePlayPurchaseVerifier;

    public CommitShopTransaction(PlayerGateway playerGateway, FoilCardGateway foilCardGateway, PurchaseGateway purchaseGateway, GooglePlayPurchaseVerifier googlePlayPurchaseVerifier) {
        this.playerGateway = playerGateway;
        this.foilCardGateway = foilCardGateway;
        this.purchaseGateway = purchaseGateway;
        this.googlePlayPurchaseVerifier = googlePlayPurchaseVerifier;
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
                    storePurchase(request, player, transaction);
                    unlockPurchaseReward(player, transaction);
                    verifiedProductIds.add(transaction.productId);
                } catch (KeyAlreadyExists error) {
                    // It is not possible to purchase a product twice.
                }
            }
        }

        return verifiedProductIds;
    }

    private void storePurchase(Request request, Player player, Transaction transaction) {
        Purchase purchase = new Purchase();
        purchase.setPlayerId(player.getId());
        purchase.setProductId(transaction.productId);
        purchase.setStore(request.store);
        purchase.setData(transaction.data);
        purchase.setSignature(transaction.signature);
        purchase.setPlayerId(player.getId());
        purchaseGateway.addPurchase(purchase);
    }

    private void unlockPurchaseReward(Player player, Transaction transaction) {
        if ("com.mazebert.cookie".equals(transaction.productId)) {
            foilCardGateway.addFoilCardToPlayer(player.getId(), getCookieReward());
        } else if ("com.mazebert.beer".equals(transaction.productId)) {
            foilCardGateway.addFoilCardToPlayer(player.getId(), getBeerReward());
        } else if ("com.mazebert.whisky".equals(transaction.productId)) {
            foilCardGateway.addFoilCardToPlayer(player.getId(), getWhiskyReward());
        }
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
            // TODO log
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

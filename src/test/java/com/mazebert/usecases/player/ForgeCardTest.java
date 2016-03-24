package com.mazebert.usecases.player;

import com.mazebert.entities.Card;
import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.error.ServiceUnavailable;
import com.mazebert.gateways.mocks.CardGatewayMock;
import com.mazebert.gateways.mocks.FoilCardGatewayMock;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import org.jusecase.transaction.simple.mocks.TransactionRunnerMock;
import com.mazebert.plugins.random.mocks.RandomNumberGeneratorMock;
import com.mazebert.usecases.player.AbstractBuyCard.Response;
import com.mazebert.usecases.player.ForgeCard.Request;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.*;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;

public class ForgeCardTest extends UsecaseTest<Request, Response> {
    private TransactionRunnerMock transactionRunner = new TransactionRunnerMock();
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();
    private CardGatewayMock cardGateway = new CardGatewayMock();
    private RandomNumberGeneratorMock randomNumberGenerator = new RandomNumberGeneratorMock();

    private Player player = a(player().casid());

    private static final double COMMON_DICE_ROLL = 0.33;
    private static final double UNCOMMON_DICE_ROLL = 0.41;
    private static final double RARE_DICE_ROLL = 0.75;
    private static final double UNIQUE_DICE_ROLL = 0.92;
    private static final double LEGENDARY_DICE_ROLL = 0.981;

    @Before
    public void setUp() {
        usecase = new ForgeCard(transactionRunner, foilCardGateway, playerGateway, cardGateway, randomNumberGenerator);

        playerGateway.givenPlayerExists(player);
    }

    @Test
    public void nullAppVersion() {
        givenRequest(a(request().withAppVersion(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("App version must not be null."));
    }

    @Test
    public void tooLowAppVersion() {
        givenRequest(a(request().withAppVersion("0.9.1")));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("At least app version 1.0.0 is required for this request."));
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Key must not be null."));
    }

    @Test
    public void playerNotFound() {
        givenRequest(a(request().withKey("unknown")));
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("Player does not exist."));
    }

    @Test
    public void notEnoughRelics() {
        cardGateway.givenCardExists(a(hero().littlefinger()));
        playerGateway.givenPlayerExists(a(player().casid().withRelics(19)));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenErrorIs(new ServiceUnavailable("Come back later when you have enough relics."));
    }

    @Test
    public void noCardsExist() {
        givenRequest(a(request()));
        whenRequestIsExecuted();
        thenRequestFailsWithNoForgeableCards();
    }

    @Test
    public void onlyCardsForNewerVersionAvailable() {
        cardGateway.givenCardsExist(a(list(
                a(hero().littlefinger().withSinceVersion("2.0")),
                a(item().babySword().withSinceVersion("1.1"))
        )));
        givenRequest(a(request().withAppVersion("1.0.0")));

        whenRequestIsExecuted();

        thenRequestFailsWithNoForgeableCards();
    }

    @Test
    public void onlyNotForgeableCardsAvailable() {
        cardGateway.givenCardsExist(a(list(
                a(item().babySword().withIsForgeable(false)),
                a(tower().huliTheMonkey().withIsForgeable(false))
        )));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenRequestFailsWithNoForgeableCards();
    }

    @Test
    public void purchasedCardIsReturned() {
        cardGateway.givenCardExists(a(item().babySword()));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenCardIsReturned(a(item().babySword()));
    }

    @Test
    public void cardIsPurchased() {
        cardGateway.givenCardExists(a(item().babySword()));
        playerGateway.givenPlayerExists(a(player().casid().withRelics(300)));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(280);
        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(item().babySword()));
    }

    @Test
    public void forgeCommonCard() {
        cardGateway.givenCardsExist(a(list(
                a(tower().herbWitch()),
                a(item().babySword())
        )));
        givenDiceRolls(COMMON_DICE_ROLL);

        whenRequestIsExecuted();

        thenCardIsReturned(a(item().babySword()));
    }

    @Test
    public void forgeUncommonCard() {
        cardGateway.givenCardsExist(a(list(
                a(item().babySword()),
                a(tower().herbWitch())
        )));
        givenDiceRolls(UNCOMMON_DICE_ROLL);

        whenRequestIsExecuted();

        thenCardIsReturned(a(tower().herbWitch()));
    }

    @Test
    public void forgeRareCard() {
        cardGateway.givenCardsExist(a(list(
                a(item().babySword()),
                a(tower().huliTheMonkey())
        )));
        givenDiceRolls(RARE_DICE_ROLL);

        whenRequestIsExecuted();

        thenCardIsReturned(a(tower().huliTheMonkey()));
    }

    @Test
    public void forgeUniqueCard() {
        cardGateway.givenCardsExist(a(list(
                a(item().babySword()),
                a(item().scepterOfTime())
        )));
        givenDiceRolls(UNIQUE_DICE_ROLL);

        whenRequestIsExecuted();

        thenCardIsReturned(a(item().scepterOfTime()));
    }

    @Test
    public void forgeLegendaryCard() {
        cardGateway.givenCardsExist(a(list(
                a(item().babySword()),
                a(item().seelenreisser())
        )));
        givenDiceRolls(LEGENDARY_DICE_ROLL);

        whenRequestIsExecuted();

        thenCardIsReturned(a(item().seelenreisser()));
    }

    @Test
    public void forgeLegendaryCard_onlyCommonCardsExist() {
        cardGateway.givenCardsExist(a(list(
                a(item().babySword())
        )));
        givenDiceRolls(LEGENDARY_DICE_ROLL);

        whenRequestIsExecuted();

        thenCardIsReturned(a(item().babySword()));
    }

    @Test
    public void forgeRareCard_noEqualOrLowerCardsExists() {
        cardGateway.givenCardsExist(a(list(
                a(item().scepterOfTime())
        )));
        givenDiceRolls(RARE_DICE_ROLL);

        whenRequestIsExecuted();

        thenCardIsReturned(a(item().scepterOfTime()));
    }

    @Test
    public void forgedCardIsPickedRandomlyFromPossibleCards() {
        cardGateway.givenCardsExist(a(list(
                a(item().scepterOfTime()),
                a(potion().tearsOfTheGods())
        )));
        givenDiceRolls(UNIQUE_DICE_ROLL);
        randomNumberGenerator.givenRandomIntegers(1);

        whenRequestIsExecuted();

        thenCardIsReturned(a(potion().tearsOfTheGods()));
        randomNumberGenerator.thenRandomIntegerCallsAre("min: 0, max: 1");
    }

    @Test
    public void forgeKiwiEgg() {
        cardGateway.givenCardsExist(a(list(
                a(tower().kiwiEgg()),
                a(tower().kiwi())
        )));
        givenDiceRolls(LEGENDARY_DICE_ROLL);
        givenRequest(a(request().withAppVersion("1.3.0")));

        whenRequestIsExecuted();

        thenCardIsReturned(a(tower().kiwiEgg()));
        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(tower().kiwi()));
    }

    @Test
    public void forgeBloodDemon() {
        cardGateway.givenCardsExist(a(list(
                a(tower().bloodDemon()),
                a(item().bloodDemonBlade())
        )));
        givenDiceRolls(LEGENDARY_DICE_ROLL);

        whenRequestIsExecuted();

        thenCardIsReturned(a(tower().bloodDemon()));
        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(item().bloodDemonBlade()));
    }

    private void givenDiceRolls(Double ... rolls) {
        randomNumberGenerator.givenRandomDoubles(rolls);
        givenRequest(a(request()));
    }

    private void thenCardIsReturned(Card card) {
        assertEquals(response.id, card.getId());
        assertEquals(response.type, card.getType());
    }

    private void thenPlayerRelicsAre(int relics) {
        assertEquals(relics, playerGateway.getRelics(player.getId()));
        assertEquals(relics, response.relics);
    }

    private void thenRequestFailsWithNoForgeableCards() {
        thenErrorIs(new ServiceUnavailable("There are no cards available that can be forged."));
    }

    private RequestBuilder request() {
        return new RequestBuilder()
                .withAppVersion("1.0.0")
                .withKey("abcdef");
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        @Override
        public Request build() {
            return request;
        }

        public RequestBuilder withAppVersion(String value) {
            request.appVersion = value;
            return this;
        }

        public RequestBuilder withKey(String value) {
            request.key = value;
            return this;
        }
    }
}
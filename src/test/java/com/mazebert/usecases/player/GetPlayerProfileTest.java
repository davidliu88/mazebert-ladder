package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.CardGatewayMock;
import com.mazebert.gateways.mocks.FoilCardGatewayMock;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.plugins.time.mocks.CurrentDatePluginMock;
import com.mazebert.usecases.player.GetPlayerProfile.Request;
import com.mazebert.usecases.player.GetPlayerProfile.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.*;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.*;

public class GetPlayerProfileTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private CardGatewayMock cardGateway = new CardGatewayMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();

    private Player player = a(player().casid());

    @Before
    public void setUp() {
        usecase = new GetPlayerProfile(playerGateway, cardGateway, foilCardGateway, currentDatePlugin);

        playerGateway.givenPlayerExists(player);
    }

    @Test
    public void playerDoesNotExist() {
        givenRequest(a(request().withId(10)));
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("A player with id '10' does not exist."));
    }

    @Test
    public void playerInformationIsAddedToResponse() {
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("casid", response.name);
        assertEquals(99, response.level);
        assertEquals(99999, response.experience);
        assertEquals(7, response.supporterLevel);
        assertEquals(300, response.relics);
    }

    @Test
    public void playerRankIsAddedToResponse() {
        playerGateway.givenPlayerRank(700, a(player().casid()));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals(700, response.rank);
    }

    @Test
    public void lastUpdateAtTimeOfRequest() {
        currentDatePlugin.givenCurrentDate(a(date().with("2016-01-21 19:21:12")));
        playerGateway.givenPlayerExists(a(player().casid().withLastUpdate(a(date().with("2016-01-21 19:21:12")))));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("Now playing", response.lastPlayed);
    }

    @Test
    public void lastUpdate12MinutesAgo() {
        currentDatePlugin.givenCurrentDate(a(date().with("2016-01-21 19:33:12")));
        playerGateway.givenPlayerExists(a(player().casid().withLastUpdate(a(date().with("2016-01-21 19:21:12")))));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("Last seen 12 minutes ago", response.lastPlayed);
    }

    @Test
    public void emptyFoilHeroes_noCards() {
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("0/0", response.foilHeroProgress);
        assertEquals(0, response.foilHeroes.size());
    }

    @Test
    public void emptyFoilHeroes_twoHeroCards() {
        cardGateway.givenCardsExist(a(list(
                a(hero()),
                a(hero()),
                a(item())
        )));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("0/2", response.foilHeroProgress);
        assertEquals(0, response.foilHeroes.size());
    }

    @Test
    public void foilHeroes_foilCardDoesNotExist() throws Throwable {
        cardGateway.givenCardsExist(a(list(
                // no cards!
        )));
        foilCardGateway.givenFoilCardsForPlayer(player, a(list(
                a(foilCard().withCard(a(hero().littlefinger())))
        )));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("0/0", response.foilHeroProgress);
        assertEquals(0, response.foilHeroes.size());
    }

    @Test
    public void foilHeroes() throws Throwable {
        cardGateway.givenCardsExist(a(list(
                a(hero().littlefinger()),
                a(item().bowlingBall())
        )));
        foilCardGateway.givenFoilCardsForPlayer(player, a(list(
                a(foilCard().withCard(a(hero().littlefinger())).withAmount(10)),
                a(foilCard().bowlingBall())
        )));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("1/1", response.foilHeroProgress);
        assertEquals(1, response.foilHeroes.size());
        assertEquals(1, response.foilHeroes.get(0).id);
        assertEquals("Sir Littlefinger", response.foilHeroes.get(0).name);
        assertEquals(1, response.foilHeroes.get(0).rarity);
        assertEquals(10, response.foilHeroes.get(0).amount);
    }

    @Test
    public void foilItems() {
        cardGateway.givenCardsExist(a(list(
                a(item().bowlingBall()),
                a(item().mjoelnir()),
                a(item().babySword()),
                a(item().scepterOfTime())
        )));
        foilCardGateway.givenFoilCardsForPlayer(player, a(list(
                a(foilCard().withCard(a(item().babySword()))),
                a(foilCard().withCard(a(item().mjoelnir()))),
                a(foilCard().withCard(a(item().scepterOfTime())))
        )));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("3/4", response.foilItemProgress);
        assertEquals(3, response.foilItems.size());
    }

    @Test
    public void foilPotions() {
        cardGateway.givenCardsExist(a(list(
                a(potion().angelicElixir()),
                a(potion().tearsOfTheGods())
        )));
        foilCardGateway.givenFoilCardsForPlayer(player, a(list(
                a(foilCard().withCard(a(potion().angelicElixir())))
        )));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("1/2", response.foilPotionProgress);
        assertEquals(1, response.foilPotions.size());
    }

    @Test
    public void foilTowers() {
        cardGateway.givenCardsExist(a(list(
                a(tower().herbWitch()),
                a(tower().huliTheMonkey())
        )));
        foilCardGateway.givenFoilCardsForPlayer(player, a(list(
                a(foilCard().withCard(a(tower().herbWitch())))
        )));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("1/2", response.foilTowerProgress);
        assertEquals(1, response.foilTowers.size());
    }

    private RequestBuilder request() {
        return new RequestBuilder().withId(115);
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        @Override
        public Request build() {
            return request;
        }

        public RequestBuilder withId(long value) {
            request.id = value;
            return this;
        }
    }
}
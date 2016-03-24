package com.mazebert.gateways.transaction;

import com.mazebert.categories.IntegrationTest;
import com.mazebert.entities.Player;
import com.mazebert.error.InternalServerError;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.mysql.MySqlFoilCardGateway;
import com.mazebert.gateways.mysql.MySqlPlayerGateway;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import org.jusecase.transaction.TransactionError;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.jusecase.transaction.simple.SimpleTransactionRunner;

import static com.mazebert.builders.BuilderFactory.foilCard;
import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;

@Category(IntegrationTest.class)
public class SimpleTransactionRunnerTest {
    private SimpleTransactionRunner transactionManager;
    private PlayerGateway playerGateway;
    private FoilCardGateway foilCardGateway;

    private Player player;

    @BeforeClass
    public static void prepareTest() throws Exception {
        clearData();
    }

    @Before
    public void setUp() throws Exception {
        transactionManager = (SimpleTransactionRunner) TestDataSourceProvider.instance.getTransactionRunner();
        playerGateway = new MySqlPlayerGateway(TestDataSourceProvider.instance.get());
        foilCardGateway = new MySqlFoilCardGateway(TestDataSourceProvider.instance.get());

        player = playerGateway.addPlayer(a(player().casid().withRelics(1000)));
    }

    @After
    public void tearDown() throws Exception {
        clearData();
    }

    private static void clearData() {
        TestDataSourceProvider.instance.clearTable("Player");
        TestDataSourceProvider.instance.clearTable("PlayerFoilCard");
    }

    @Test
    public void transactionIsCommitted() {
        transactionManager.runAsTransaction(this::addRelicsAndFoilCard);

        assertEquals(4600, playerGateway.getRelics(player.getId()));
        assertEquals(1, foilCardGateway.getFoilCardsForPlayerId(player.getId()).size());
    }

    @Test
    public void transactionIsRolledBack() {
        try {
            transactionManager.runAsTransaction(() -> {
                addRelicsAndFoilCard();
                throw new InternalServerError("Something bad happened!");
            });
        } catch (InternalServerError error) {
            assertEquals("Something bad happened!", error.getMessage());
        }

        assertEquals(1000, playerGateway.getRelics(player.getId()));
        assertEquals(0, foilCardGateway.getFoilCardsForPlayerId(player.getId()).size());
    }

    @Test(expected = TransactionError.class)
    public void nestedTransactionsAreNotSupported() {
        transactionManager.runAsTransaction(() -> transactionManager.runAsTransaction(this::addRelicsAndFoilCard));
    }

    @Test
    public void readChangedValueDuringTransaction() {
        transactionManager.runAsTransaction(() -> {
            playerGateway.addRelics(player.getId(), 1000);
            assertEquals(2000, playerGateway.getRelics(player.getId()));
            playerGateway.addRelics(player.getId(), 1000);
            assertEquals(3000, playerGateway.getRelics(player.getId()));
        });
    }

    private void addRelicsAndFoilCard() {
        playerGateway.addRelics(player.getId(), 1000);
        playerGateway.addRelics(player.getId(), 2000);
        playerGateway.addRelics(player.getId(), 500);
        playerGateway.addRelics(player.getId(), 100);

        foilCardGateway.addFoilCardToPlayer(player.getId(), a(foilCard().bowlingBall()));
    }
}
package com.mazebert.gateways.transaction.datasource;

import com.mazebert.entities.Player;
import com.mazebert.error.InternalServerError;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.mysql.MySqlFoilCardGateway;
import com.mazebert.gateways.mysql.MySqlPlayerGateway;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.transaction.TransactionError;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.mazebert.builders.BuilderFactory.foilCard;
import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.jusecase.Builders.a;
import static org.mockito.Mockito.*;

public class DataSourceTransactionManagerGatewayTest {
    private DataSourceTransactionManager transactionManager;
    private PlayerGateway playerGateway;
    private FoilCardGateway foilCardGateway;

    private Player player;

    @Before
    public void setUp() throws Exception {
        transactionManager = (DataSourceTransactionManager) TestDataSourceProvider.instance.getTransactionManager();
        playerGateway = new MySqlPlayerGateway(TestDataSourceProvider.instance.get());
        foilCardGateway = new MySqlFoilCardGateway(TestDataSourceProvider.instance.get());

        player = playerGateway.addPlayer(a(player().casid().withRelics(1000)));
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
    public void transactionInitializationError_transactionIsRolledBackAndClosed() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        doThrow(new SQLException()).when(connection).setAutoCommit(anyBoolean());
        transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);

        try {
            transactionManager.runAsTransaction(() -> {
            });
        } catch (TransactionError error) {
            assertEquals("Failed to create transaction.", error.getMessage());
        }

        verify(connection, times(1)).close();
        assertNull(transactionManager.getCurrent());
    }

    @Test
    public void commitError_transactionIsRolledBackAndClosed() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        doThrow(new SQLException()).when(connection).commit();
        transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);

        try {
            transactionManager.runAsTransaction(() -> {
            });
        } catch (TransactionError error) {
            assertEquals("Failed to commit transaction.", error.getMessage());
        }

        verify(connection, times(1)).close();
        assertNull(transactionManager.getCurrent());

        verify(connection, times(1)).rollback();
    }

    @Test
    public void rollbackError_transactionIsClosed() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        doThrow(new SQLException()).when(connection).rollback();
        transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);

        try {
            transactionManager.runAsTransaction(() -> {
                throw new InternalServerError("Must rollback!");
            });
        } catch (TransactionError error) {
            assertEquals("Failed to rollback transaction.", error.getMessage());
        }

        verify(connection, times(1)).close();
        assertNull(transactionManager.getCurrent());
    }

    private void addRelicsAndFoilCard() {
        playerGateway.addRelics(player.getId(), 1000);
        playerGateway.addRelics(player.getId(), 2000);
        playerGateway.addRelics(player.getId(), 500);
        playerGateway.addRelics(player.getId(), 100);

        foilCardGateway.addFoilCardToPlayer(player.getId(), a(foilCard().bowlingBall()));
    }
}